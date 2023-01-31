package cn.bossfriday.jmeter.sampler;

import cn.bossfriday.jmeter.AppSamplerAsserter;
import cn.bossfriday.jmeter.asserter.ISamplerAsserter;
import cn.bossfriday.jmeter.common.PocException;
import cn.bossfriday.jmeter.component.BaseSamplerComponent;
import cn.bossfriday.jmeter.component.SampleExpressionExecutor;
import cn.bossfriday.jmeter.component.SampleResultExtractor;
import cn.bossfriday.jmeter.entities.SampleExtractorEntry;
import cn.bossfriday.jmeter.entities.SamplerSetting;
import cn.bossfriday.jmeter.expression.ExpressionNodeType;
import com.jayway.jsonpath.JsonPath;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * BaseSampler
 *
 * @author chenx
 */
public abstract class BaseSampler extends BaseSamplerComponent {

    private static final Logger log = LogManager.getLogger(BaseSampler.class);

    @Getter
    @Setter
    protected boolean isStartError = false;

    protected AtomicInteger sampleIndex;
    protected List<BaseSamplerComponent> components;

    protected SampleResultExtractor extractor;
    protected SampleExpressionExecutor executor;
    protected ISamplerAsserter asserter;

    protected BaseSampler(SamplerSetting setting) {
        super(setting);

        this.components = new ArrayList<>();
        this.sampleIndex = new AtomicInteger(0);
    }

    @Override
    public void testStarted() throws PocException {
        this.asserter = AppSamplerAsserter.getAsserter(this.setting.getAssertType());
        this.executor = new SampleExpressionExecutor(this.setting);
        this.extractor = new SampleResultExtractor(this.setting);

        this.components.add(this.executor);
        this.components.add(this.extractor);
        for (BaseSamplerComponent component : this.components) {
            component.testStarted();
        }

        this.sampleStarted();
    }

    @Override
    public void testEnded() throws PocException {
        for (BaseSamplerComponent component : this.components) {
            component.testEnded();
        }

        this.sampleEnded();
    }

    /**
     * sampleStarted
     *
     * @throws PocException
     */
    public abstract void sampleStarted() throws PocException;

    /**
     * sample
     *
     * @return
     * @throws PocException
     */
    public abstract SampleResult sample() throws PocException;

    /**
     * sampleEnded
     *
     * @throws PocException
     */
    public abstract void sampleEnded() throws PocException;

    /**
     * extractSampleData
     *
     * @param currentSampleIndex
     * @param apiResponseBody
     */
    protected void extractSampleData(int currentSampleIndex, String apiResponseBody) {
        try {
            if (CollectionUtils.isEmpty(this.extractor.getExtractorEntryList())) {
                return;
            }

            String[] csvRecord = new String[this.extractor.getExtractorEntryList().size()];
            for (int i = 0; i < this.extractor.getExtractorEntryList().size(); i++) {
                SampleExtractorEntry extractorEntry = this.extractor.getExtractorEntryList().get(i);
                String value = "";
                if (extractorEntry.getExpressionNode().getNodeType() == ExpressionNodeType.VARIABLE.getType()) {
                    value = this.getVariable(extractorEntry.getExpressionNode().getExpression(), currentSampleIndex);
                } else if (extractorEntry.getExpressionNode().getNodeType() == ExpressionNodeType.CONSTANT.getType()) {
                    value = JsonPath.read(apiResponseBody, extractorEntry.getJsonPath());
                } else {
                    throw new PocException("JSON Path setting only support : constant or variable!");
                }

                if (StringUtils.isEmpty(value)) {
                    value = extractorEntry.getDefaultValue();
                }

                csvRecord[i] = value;
            }

            this.extractor.addRecord(csvRecord);
        } catch (Exception ex) {
            log.error("BaseSampler.extractSampleData() error!", ex);
        }
    }

    /**
     * getVariable
     *
     * @param varName
     * @param currentSampleIndex
     * @return
     * @throws PocException
     */
    protected String getVariable(String varName, int currentSampleIndex) throws PocException {
        // 常量无需计算直接返回
        if (this.executor.getStaticConstArgsMap().containsKey(varName)) {
            return (String) this.executor.getStaticConstArgsMap().get(varName);
        }

        Map<String, Object> args = this.executor.getVariableMap(currentSampleIndex);
        if (!args.containsKey(varName)) {
            throw new PocException(String.format("variable not existed!(%s)", varName));
        }

        return (String) args.get(varName);
    }

    /**
     * getSampleLog
     *
     * @param sampleIndex
     * @param result
     * @param url
     * @param header
     * @param body
     * @param response
     * @return
     */
    protected String getSampleLog(int sampleIndex, SampleResult result, String url, String header, String body, String response) {
        StringBuilder sb = new StringBuilder();
        if (result.isSuccessful()) {
            sb.append("sample success, sampleIndex=" + sampleIndex + ", time=" + result.getTime());
        } else {
            sb.append("sample failed, sampleIndex=" + sampleIndex + ", time=" + result.getTime());
        }

        if (this.setting.isLogRequest() || !result.isSuccessful()) {
            if (!StringUtils.isEmpty(url)) {
                sb.append(", url=" + url);
            }

            if (!StringUtils.isEmpty(header)) {
                sb.append(", header=" + header.replace('\n', ' '));
            }

            if (!StringUtils.isEmpty(body)) {
                sb.append(", body=" + body.replace('\n', ' '));
            }
        }

        boolean isLogResponse = (this.setting.isLogResponse() || !result.isSuccessful()) && !StringUtils.isEmpty(response);
        if (isLogResponse) {
            sb.append(", response=" + response.replace('\n', ' '));
        }

        return sb.toString();
    }
}
