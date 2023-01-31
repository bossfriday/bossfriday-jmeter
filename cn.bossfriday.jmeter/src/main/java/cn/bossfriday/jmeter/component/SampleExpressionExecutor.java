package cn.bossfriday.jmeter.component;

import cn.bossfriday.jmeter.common.HttpMethod;
import cn.bossfriday.jmeter.common.PocException;
import cn.bossfriday.jmeter.entities.SamplerSetting;
import cn.bossfriday.jmeter.expression.ExpressionCalculator;
import cn.bossfriday.jmeter.expression.ExpressionParser;
import cn.bossfriday.jmeter.utils.AppSamplerUtils;
import cn.bossfriday.jmeter.utils.HttpUtils;
import cn.bossfriday.jmeter.utils.LruHashMap;
import lombok.Getter;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import static cn.bossfriday.jmeter.common.Const.ARG_NAME_SAMPLE_INDEX;
import static cn.bossfriday.jmeter.common.Const.SAMPLE_VAR_MAP_DURATION;

/**
 * 采样表达式执行元件
 *
 * @author chenx
 */
public class SampleExpressionExecutor extends BaseSamplerComponent {

    @Getter
    private LinkedHashMap<String, Object> staticConstArgsMap;

    private LinkedHashMap<String, ExpressionParser> dynamicVariableArgsMap;

    @Getter
    private ExpressionParser urlParser;

    @Getter
    private ExpressionParser headerParser;

    @Getter
    private ExpressionParser bodyParser;

    private ExpressionCalculator expExecutor;

    /**
     * 这里用一个环形的HashMap数据结构表达也可以，不过在数据量不大的情况下用环形HashMap性能收益并不大；
     */
    private LruHashMap<Integer, LinkedHashMap<String, Object>> varMap;

    public SampleExpressionExecutor(SamplerSetting setting) throws PocException {
        super(setting);

        this.setting = setting;
        this.expExecutor = new ExpressionCalculator();
    }

    @Override
    public void testStarted() throws PocException {
        // 表达式解析
        this.urlParser = new ExpressionParser(this.setting.getUrl());
        this.headerParser = new ExpressionParser(this.setting.getHeaderData());
        this.bodyParser = new ExpressionParser(this.setting.getBodyData());
        this.urlParser.parse();
        this.headerParser.parse();
        this.bodyParser.parse();

        this.initVariables();

        // 表达式预热
        Map<String, Object> args = this.getVariableMap(0);
        this.applyExpression(this.getUrlParser(), args);
        this.applyExpression(this.getHeaderParser(), args);
        this.applyExpression(this.getBodyParser(), args);
    }

    @Override
    public void testEnded() throws PocException {
        this.resetVariables();
    }

    /**
     * getVariable
     *
     * @param currentSampleIndex
     * @return
     */
    public Map<String, Object> getVariableMap(int currentSampleIndex) throws PocException {
        if (this.varMap.containsKey(currentSampleIndex)) {
            return this.varMap.get(currentSampleIndex);
        }

        // 采样常量参数及内置sampleIndex参数填充
        LinkedHashMap<String, Object> params = new LinkedHashMap<>(16);
        params.put(ARG_NAME_SAMPLE_INDEX, currentSampleIndex);
        params.putAll(this.staticConstArgsMap);

        if (MapUtils.isEmpty(this.dynamicVariableArgsMap)) {
            this.varMap.put(currentSampleIndex, params);
            return params;
        }

        // 采样变量参数填充
        for (Map.Entry<String, ExpressionParser> entry : this.dynamicVariableArgsMap.entrySet()) {
            String varName = entry.getKey();
            ExpressionParser varParser = entry.getValue();
            String varValue = this.applyExpression(varParser, params);

            if (params.putIfAbsent(varName, varValue) != null) {
                throw new PocException(String.format("duplicated variable: %s", varName));
            }
        }

        this.varMap.put(currentSampleIndex, params);
        return this.varMap.get(currentSampleIndex);
    }

    /**
     * getHttpRequest
     *
     * @param url
     * @param header
     * @param body
     * @return
     * @throws PocException
     */
    public HttpRequestBase getHttpRequest(String url, String header, String body) throws PocException {
        if (StringUtils.isEmpty(url)) {
            throw new PocException("url is empty!");
        }

        HttpRequestBase httpRequest = AppSamplerUtils.getHttpRequest(this.setting.getMethod());

        // setURI
        httpRequest.setURI(URI.create(HttpUtils.getUrl(url, this.setting.isHttps())));

        // setHeader
        Map<String, String> headerMap = AppSamplerUtils.getKvMap(header);
        if (!MapUtils.isEmpty(headerMap)) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpRequest.addHeader(entry.getKey(), entry.getValue());
            }
        }

        // setBody
        if (!StringUtils.isEmpty(body)) {
            StringEntity entity = new StringEntity(body, "UTF-8");
            if (this.setting.getMethod().equals(HttpMethod.POST.getName())) {
                ((HttpPost) httpRequest).setEntity(entity);
            }

            if (this.setting.getMethod().equals(HttpMethod.PUT.getName())) {
                ((HttpPut) httpRequest).setEntity(entity);
            }

            if (this.setting.getMethod().equals(HttpMethod.PATCH.getName())) {
                ((HttpPatch) httpRequest).setEntity(entity);
            }
        }

        return httpRequest;
    }

    /**
     * applyExpression
     *
     * @param parser
     * @return
     */
    public String applyExpression(ExpressionParser parser, Map<String, Object> params) throws PocException {
        if (parser == null) {
            throw new PocException("ExpressionParser is null!");
        }

        if (StringUtils.isEmpty(parser.getExpression())) {
            return null;
        }

        return String.valueOf(this.expExecutor.apply(parser.getExpNodeStack(), params));
    }

    /**
     * initVariables
     */
    private void initVariables() throws PocException {
        this.resetVariables();
        LinkedHashMap<String, String> varSettingMap = AppSamplerUtils.getKvMap(this.setting.getSampleVar());
        for (Map.Entry<String, String> entry : varSettingMap.entrySet()) {
            ExpressionParser expParser = new ExpressionParser(entry.getValue());
            expParser.parse();

            // 为了减少表达计算次数后续只对动态变量做表达计算，因此这里将他们分类暂存
            if (expParser.isConstant()) {
                this.staticConstArgsMap.put(entry.getKey(), entry.getValue());
            } else {
                this.dynamicVariableArgsMap.put(entry.getKey(), expParser);
            }
        }
    }

    /**
     * resetVarMap
     */
    private void resetVariables() {
        // just help GC
        this.varMap = null;
        this.staticConstArgsMap = null;
        this.dynamicVariableArgsMap = null;

        // 缓存最近2W条采样变量计算结果已经足够（几乎不可能需要用到1W+以上的线程去打压）
        this.varMap = new LruHashMap<>(20000, null, SAMPLE_VAR_MAP_DURATION);
        this.staticConstArgsMap = new LinkedHashMap<>(16);
        this.dynamicVariableArgsMap = new LinkedHashMap<>(16);
    }
}
