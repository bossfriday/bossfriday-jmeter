package cn.bossfriday.jmeter.expression;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ExpressionNode
 *
 * @author chenx
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpressionNode {

    private Integer nodeType;

    private String expression;

    private Object value;

    private List<ExpressionNode> arguments;

    /**
     * addArgument
     *
     * @param argument
     */
    public void addArgument(ExpressionNode argument) {
        if (argument == null) {
            return;
        }

        if (this.arguments == null) {
            this.arguments = new ArrayList<>();
        }

        this.arguments.add(argument);
    }

    /**
     * getArgumentValues
     *
     * @return
     */
    public List<Object> getArgumentValues() {
        List<Object> values = new ArrayList<>();
        if (CollectionUtils.isEmpty(this.arguments)) {
            return values;
        }

        this.arguments.forEach(arg -> values.add(arg.getValue()));

        return values;
    }
}
