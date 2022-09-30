package cn.bossfriday.jmeter;

import cn.bossfriday.jmeter.common.PocException;
import cn.bossfriday.jmeter.entities.SamplerSetting;
import cn.bossfriday.jmeter.sampler.BaseSampler;
import cn.bossfriday.jmeter.utils.AppSamplerUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;

import static cn.bossfriday.jmeter.common.Const.*;


/**
 * AppSampler
 *
 * @author chenx
 */
public class AppSampler extends AbstractSampler implements TestStateListener {

    private static final Logger log = LogManager.getLogger(AppSampler.class);
    private static BaseSampler sampler;
    private static SamplerSetting setting;

    public AppSampler() {
        this.setName(GUI_SAMPLER_NAME);
    }

    @Override
    public void testStarted() {
        try {
            setSamplerSetting(this);
            setSampler(setting);

            sampler.testStarted();
            log.info("sampler testStarted() done.");
        } catch (Exception ex) {
            sampler.setStartError(true);
            log.error("testStarted() error!", ex);
        }
    }

    @Override
    public SampleResult sample(Entry entry) {
        try {
            if (sampler.isStartError()) {
                return AppSamplerUtils.buildSampleResult(setting.getSampleLabel(),
                        String.valueOf(CODE_TEST_STARTED_ERROR),
                        RESPONSE_MESSAGE_STARTED_ERROR,
                        false);
            }

            SampleResult sampleResult = sampler.sample();
            sampleResult.setSampleLabel(setting.getSampleLabel());

            return sampleResult;
        } catch (Exception ex) {
            log.error("sample() error!", ex);
            return AppSamplerUtils.buildSampleResult(setting.getSampleLabel(),
                    String.valueOf(CODE_SAMPLE_ERROR),
                    ex.getMessage(),
                    false);
        }
    }

    @Override
    public void testStarted(String s) {
        // ignore the unsupported invoke
    }

    @Override
    public void testEnded() {
        try {
            sampler.testEnded();
            log.info("sampler testEnded() done.");
        } catch (Exception ex) {
            log.error("testEnded() error!", ex);
        }
    }

    @Override
    public void testEnded(String s) {
        // ignore the unsupported invoke
    }

    /**
     * setSamplerSetting
     *
     * @param sampler
     */
    private static synchronized void setSamplerSetting(AbstractSampler sampler) throws PocException {
        String samplerType = sampler.getPropertyAsString(GUI_SAMPLER_TYPE);
        String sampleLabel = sampler.getPropertyAsString(GUI_SAMPLE_LABEL);
        String assertType = sampler.getPropertyAsString(GUI_ASSERT_TYPE);
        boolean isLogRequest = sampler.getPropertyAsBoolean(GUI_SAMPLE_IS_LOG_REQUEST);
        boolean isLogResponse = sampler.getPropertyAsBoolean(GUI_SAMPLE_IS_LOG_RESPONSE);
        String sampleVar = sampler.getPropertyAsString(GUI_SAMPLE_VAR);
        boolean isHttps = sampler.getPropertyAsBoolean(GUI_IS_HTTPS);
        String method = sampler.getPropertyAsString(GUI_METHOD);
        String url = sampler.getPropertyAsString(GUI_URL);
        String headerData = sampler.getPropertyAsString(GUI_HEADER_DATA);
        String bodyData = sampler.getPropertyAsString(GUI_BODY_DATA);
        String variables = sampler.getPropertyAsString(GUI_VARIABLES);
        String jsonPath = sampler.getPropertyAsString(GUI_JSON_PATHS);
        String defaultValues = sampler.getPropertyAsString(GUI_DEFAULT_VALUES);
        String manualDoc = sampler.getPropertyAsString(GUI_MANUAL_DOC);

        if (StringUtils.isEmpty(sampleLabel)) {
            throw new PocException("sampleLabel is empty!");
        }

        if (StringUtils.isEmpty(samplerType)) {
            throw new PocException("samplerType is empty!");
        }

        setting = SamplerSetting.builder()
                .samplerType(samplerType)
                .sampleLabel(sampleLabel)
                .assertType(assertType)
                .isLogRequest(isLogRequest)
                .isLogResponse(isLogResponse)
                .sampleVar(sampleVar)
                .isHttps(isHttps)
                .method(method)
                .url(url)
                .headerData(headerData)
                .bodyData(bodyData)
                .variables(variables)
                .jsonPath(jsonPath)
                .defaultValues(defaultValues)
                .manualDoc(manualDoc)
                .build();
        log.info("Current SamplerSetting is: {}", setting);
    }

    /**
     * setSampler
     *
     * @param setting
     * @throws PocException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private static synchronized void setSampler(SamplerSetting setting) throws PocException,
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException {
        sampler = AppSamplerBuilder.getSampler(setting);
    }
}


