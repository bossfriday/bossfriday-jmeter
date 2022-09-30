package cn.bossfriday.jmeter.expression;

import cn.bossfriday.jmeter.common.PocException;
import cn.bossfriday.jmeter.fuction.FunctionExecutor;
import com.alibaba.fastjson.JSONPath;

import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

/**
 * ExpressionExecutor
 *
 * @author chenx
 */
public class ExpressionCalculator {

    /**
     * apply
     *
     * @param expNodeStack
     * @param params
     * @return
     */
    public synchronized Object apply(Deque<ExpressionNode> expNodeStack, Map<String, Object> params) throws PocException {
        Deque<ExpressionNode> stackCopy = new LinkedList<>();
        stackCopy.addAll(expNodeStack);

        Object result = null;
        while (!stackCopy.isEmpty()) {
            ExpressionNode expNode = stackCopy.pop();
            if (ExpressionNodeType.CONSTANT.getType() == expNode.getNodeType()) {
                expNode.setValue(expNode.getExpression());
            } else if (ExpressionNodeType.VARIABLE.getType() == expNode.getNodeType()) {
                Object value = this.getVariable(expNode, params);
                expNode.setValue(value);
            } else if (ExpressionNodeType.FUNCTION.getType() == expNode.getNodeType()) {
                Object value = FunctionExecutor.getInstance().apply(expNode.getExpression(), expNode.getArgumentValues().toArray());
                expNode.setValue(value);
            } else if (ExpressionNodeType.TEMPLATE.getType() == expNode.getNodeType()) {
                String value = TemplateFormat.format(expNode.getExpression(), expNode.getArgumentValues());
                expNode.setValue(value);
            }

            result = expNode.getValue();
        }

        return result;
    }

    /**
     * getVariable
     *
     * @param varExpNode
     * @param params
     * @return
     */
    private Object getVariable(ExpressionNode varExpNode, Map<String, Object> params) {
        return JSONPath.eval(params, "$." + varExpNode.getExpression());
    }

    private static class TemplateFormat {

        public static String format(String pattern, Collection<?> collection) {
            return format(pattern, collection.toArray());
        }

        /**
         * format
         *
         * @param pattern
         * @param objects
         * @return
         */
        public static String format(String pattern, Object... objects) {
            if (objects == null || objects.length == 0) {
                return pattern;
            }

            StringBuilder temp = new StringBuilder(pattern);
            for (int i = 0; i < objects.length; i++) {
                String token = "{{" + i + "}}";
                int start = temp.indexOf(token);
                if (start == -1) {
                    break;
                }

                temp.replace(start, start + token.length(), String.valueOf(objects[i]));
            }

            return temp.toString();
        }
    }
}
