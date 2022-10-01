package cn.bossfriday.jmeter.sampler;

import cn.bossfriday.jmeter.AppSamplerBuilder;
import cn.bossfriday.jmeter.common.Combo3;
import cn.bossfriday.jmeter.common.PocException;
import cn.bossfriday.jmeter.entities.SamplerSetting;
import cn.bossfriday.jmeter.utils.HttpUtils;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static cn.bossfriday.jmeter.common.Const.SAMPLER_COMMON;
import static cn.bossfriday.jmeter.common.Const.SAMPLE_LOG_INTERVAL_COUNT;

/**
 * CommonHttpSampler
 *
 * @author chenx
 */
@AppSamplerBuilder.SamplerType(name = SAMPLER_COMMON)
public class CommonHttpSampler extends BaseSampler {

    private static final Logger log = LogManager.getLogger(CommonHttpSampler.class);
    private static CloseableHttpClient httpClient;

    public CommonHttpSampler(SamplerSetting config) {
        super(config);
    }

    @Override
    public void sampleStarted() {
        setStaticMembers(this.setting.isHttps());
    }

    @Override
    public SampleResult sample() throws PocException {
        SampleResult sampleResult = new SampleResult();
        HttpRequestBase httpRequest = null;
        try {
            int currentSampleIndex = this.sampleIndex.getAndIncrement();

            // 构建Http请求
            Map<String, Object> args = this.executor.getVariableMap(currentSampleIndex);
            String url = this.executor.applyExpression(this.executor.getUrlParser(), args);
            String header = this.executor.applyExpression(this.executor.getHeaderParser(), args);
            String body = this.executor.applyExpression(this.executor.getBodyParser(), args);
            httpRequest = this.executor.getHttpRequest(url, header, body);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            // 执行Http请求
            sampleResult.sampleStart();
            String httpResponse = httpClient.execute(httpRequest, responseHandler);
            sampleResult.sampleEnd();

            // 断言Http应答
            Combo3<Boolean, String, String> assertResult = this.asserter.isSuccess(httpResponse);
            sampleResult.setSuccessful(assertResult.getV1());
            sampleResult.setResponseCode(String.valueOf(assertResult.getV2()));
            sampleResult.setResponseMessage(assertResult.getV3());

            if (sampleResult.isSuccessful()) {
                // 如果采样成功则提取采样数据
                this.extractSampleData(currentSampleIndex, httpResponse);

                // 仅仅为了减少正常采样的jmeter.log日志输出量（减少疲劳测试对打压机磁盘要求）
                if (currentSampleIndex % SAMPLE_LOG_INTERVAL_COUNT == 0) {
                    log.info(this.getSampleLog(currentSampleIndex, sampleResult, url, header, body, httpResponse));
                }
            } else {
                log.error(this.getSampleLog(currentSampleIndex, sampleResult, url, header, body, httpResponse));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new PocException(ex.getMessage());
        } finally {
            if (httpRequest != null) {
                httpRequest.releaseConnection();
            }
        }

        return sampleResult;
    }

    @Override
    public void sampleEnded() throws PocException {
        try {
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (Exception ex) {
            throw new PocException(ex.getMessage());
        }
    }

    /**
     * setStaticMembers
     *
     * @param isHttps
     */
    private static synchronized void setStaticMembers(boolean isHttps) {
        httpClient = HttpUtils.getPoolingHttpClient(isHttps);
    }
}
