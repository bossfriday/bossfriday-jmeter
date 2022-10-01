package cn.bossfriday.jmeter.expression;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Deque;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ExpressionParser
 *
 * @author chenx
 */
public class ExpressionParser {

    public static final String EXP_FUNCTION = "(?<fun>[a-zA-Z0-9_]+)\\((?<args>.*?)\\)";
    public static final String EXP_VARIABLE = "#\\{(?<var>[a-zA-Z0-9_.]+)\\}";

    private static final String COMMA = "\\s*,\\s*";

    private Pattern functionPattern = Pattern.compile(EXP_FUNCTION);
    private Pattern variablePattern = Pattern.compile(EXP_VARIABLE);

    @Getter
    private String expression;

    @Getter
    private Deque<ExpressionNode> expNodeStack = new LinkedList<>();

    public ExpressionParser(String expression) {
        this.expression = expression;
    }

    /**
     * parse
     */
    public void parse() {
        this.parse(this.expression);
    }

    /**
     * parse
     *
     * @param expression
     * @return
     */
    public ExpressionNode parse(String expression) {
        if (StringUtils.isEmpty(expression)) {
            return null;
        }

        ExpressionNode expNode = new ExpressionNode();
        this.expNodeStack.push(expNode);
        if (this.parseFunction(expNode, expression)) {
            return expNode;
        }

        if (this.parseVariable(expNode, expression)) {
            return expNode;
        }

        if (this.parseTemplate(expNode, expression)) {
            return expNode;
        }

        expNode.setNodeType(ExpressionNodeType.CONSTANT.getType());
        expNode.setExpression(expression);

        return expNode;
    }

    /**
     * isConstant
     *
     * @return
     */
    public boolean isConstant() {
        for (ExpressionNode node : this.expNodeStack) {
            if (node.getNodeType() != ExpressionNodeType.CONSTANT.getType()) {
                return false;
            }
        }

        return true;
    }

    /**
     * parseFunction
     *
     * @param expNode
     * @param expression
     * @return
     */
    private boolean parseFunction(ExpressionNode expNode, String expression) {
        Matcher matcher = Pattern.compile(EXP_FUNCTION).matcher(expression);
        if (!matcher.find()) {
            return false;
        }

        if (matcher.end() - matcher.start() != expression.length()) {
            return false;
        }

        expNode.setNodeType(ExpressionNodeType.FUNCTION.getType());
        String fun = matcher.group("fun");
        String args = matcher.group("args");
        expNode.setExpression(fun);
        for (String argExp : args.split(COMMA)) {
            if (!argExp.isEmpty()) {
                expNode.addArgument(this.parse(argExp));
            }
        }

        return true;
    }

    /**
     * parseVariable
     *
     * @param expNode
     * @param expression
     * @return
     */
    private boolean parseVariable(ExpressionNode expNode, String expression) {
        Matcher matcher = Pattern.compile(EXP_VARIABLE).matcher(expression);
        if (!matcher.find()) {
            return false;
        }

        if (matcher.end() - matcher.start() != expression.length()) {
            return false;
        }

        expNode.setNodeType(ExpressionNodeType.VARIABLE.getType());
        String varExp = matcher.group("var");
        expNode.setExpression(varExp);

        return true;
    }

    /**
     * parseTemplate
     *
     * @param expNode
     * @param expression
     * @return
     */
    private boolean parseTemplate(ExpressionNode expNode, String expression) {
        expNode.setNodeType(ExpressionNodeType.TEMPLATE.getType());
        StringBuffer temp = new StringBuffer();
        int argNodeCount = this.processSubNode(expNode, 0, this.functionPattern.matcher(expression), temp);
        if (argNodeCount > 0) {
            expression = temp.toString();
        }

        temp.setLength(0);
        argNodeCount = this.processSubNode(expNode, argNodeCount, this.variablePattern.matcher(expression), temp);
        if (argNodeCount > 0) {
            expNode.setExpression(temp.toString());
        }

        return argNodeCount > 0;
    }

    /**
     * 处理子节点
     *
     * @param expNode
     * @param argNodeCount
     * @param matcher
     * @param temp
     * @return
     */
    private int processSubNode(ExpressionNode expNode, int argNodeCount, Matcher matcher, StringBuffer temp) {
        while (matcher.find()) {
            matcher.group();
            ExpressionNode subNode = this.parse(matcher.group());
            if (subNode == null) {
                continue;
            }

            expNode.addArgument(subNode);
            matcher.appendReplacement(temp, "{{" + argNodeCount + "}}");
            argNodeCount++;
        }

        matcher.appendTail(temp);
        return argNodeCount;
    }
}
