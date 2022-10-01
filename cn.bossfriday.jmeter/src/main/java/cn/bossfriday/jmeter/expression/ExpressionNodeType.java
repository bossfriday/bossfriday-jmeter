package cn.bossfriday.jmeter.expression;

/**
 * ExpressionNodeType
 *
 * @author chenx
 * @date 2022/09/14
 */
public enum ExpressionNodeType {

    // TEMPLATE
    TEMPLATE(0),

    // FUNCTION
    FUNCTION(1),

    // VARIABLE
    VARIABLE(2),

    //CONSTANT
    CONSTANT(3);

    private int type;

    ExpressionNodeType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
