package cn.bossfriday.jmeter.entities;

import cn.bossfriday.jmeter.expression.ExpressionNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JsonExtractorConfig
 *
 * @author chenx
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SampleExtractorEntry {

    private String varName;

    private String jsonPath;

    private String defaultValue;

    private ExpressionNode expressionNode;
}
