package cn.bossfriday.jmeter.fuction.impl;

import cn.bossfriday.jmeter.common.Const;
import cn.bossfriday.jmeter.common.PocException;
import cn.bossfriday.jmeter.fuction.BaseFunction;
import cn.bossfriday.jmeter.fuction.FunctionExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static cn.bossfriday.jmeter.common.Const.ARG_NAME_X;
import static cn.bossfriday.jmeter.common.Const.ARG_NAME_Y;

/**
 * MathMultiply
 *
 * @author chenx
 */
@FunctionExecutor.Fun(name = Const.FUNCTION_MATH_MULTIPLY)
public class MathMultiply extends BaseFunction {

    public MathMultiply(String funName) {
        super(funName);
    }

    @Override
    public Object apply(Object... args) throws PocException {
        Long x = Long.parseLong(this.getArgValue(ARG_NAME_X, args).toString());
        Long y = Long.parseLong(this.getArgValue(ARG_NAME_Y, args).toString());

        return Math.multiplyExact(x, y);
    }

    @Override
    public Map<String, Integer> getArgsMap() {
        Map<String, Integer> argMap = new ConcurrentHashMap<>(16);
        argMap.put(ARG_NAME_X, 0);
        argMap.put(ARG_NAME_Y, 1);

        return argMap;
    }
}
