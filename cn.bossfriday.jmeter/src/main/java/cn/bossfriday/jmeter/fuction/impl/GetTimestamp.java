package cn.bossfriday.jmeter.fuction.impl;

import cn.bossfriday.jmeter.common.Const;
import cn.bossfriday.jmeter.common.PocException;
import cn.bossfriday.jmeter.fuction.BaseFunction;
import cn.bossfriday.jmeter.fuction.FunctionExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static cn.bossfriday.jmeter.common.Const.ARG_NAME_X;

/**
 * GetTimestamp
 *
 * @author chenx
 */
@FunctionExecutor.Fun(name = Const.FUNCTION_GET_TIMESTAMP)
public class GetTimestamp extends BaseFunction {

    public GetTimestamp(String funName) {
        super(funName);
    }

    @Override
    public Object apply(Object... args) throws PocException {
        Long x = Long.parseLong(this.getArgValue(ARG_NAME_X, args).toString());
        return Math.addExact(System.currentTimeMillis(), x);
    }

    @Override
    public Map<String, Integer> getArgsMap() {
        Map<String, Integer> argMap = new ConcurrentHashMap<>(16);
        argMap.put(ARG_NAME_X, 0);

        return argMap;
    }
}
