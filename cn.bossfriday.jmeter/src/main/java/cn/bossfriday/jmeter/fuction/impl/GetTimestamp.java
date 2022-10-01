package cn.bossfriday.jmeter.fuction.impl;

import cn.bossfriday.jmeter.common.Const;
import cn.bossfriday.jmeter.common.PocException;
import cn.bossfriday.jmeter.fuction.BaseFunction;
import cn.bossfriday.jmeter.fuction.FunctionExecutor;

import java.util.Collections;
import java.util.Map;

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
        return System.currentTimeMillis();
    }

    @Override
    public Map<String, Integer> getArgsMap() {
        return Collections.emptyMap();
    }
}
