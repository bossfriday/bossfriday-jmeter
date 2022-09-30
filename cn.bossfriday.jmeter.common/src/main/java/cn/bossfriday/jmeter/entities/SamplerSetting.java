package cn.bossfriday.jmeter.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SamplerSetting
 *
 * @author chenx
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SamplerSetting {

    private String sampleLabel;

    private String samplerType;

    private String assertType;

    private boolean isLogRequest;

    private boolean isLogResponse;

    private String sampleVar;

    private boolean isHttps;

    private String method;

    private String url;

    private String headerData;

    private String bodyData;

    private String variables;

    private String jsonPath;

    private String defaultValues;

    private String manualDoc;
}
