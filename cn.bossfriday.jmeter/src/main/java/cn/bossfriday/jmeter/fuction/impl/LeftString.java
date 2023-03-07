package cn.bossfriday.jmeter.fuction.impl;


import cn.bossfriday.jmeter.common.Const;
import cn.bossfriday.jmeter.common.PocException;
import cn.bossfriday.jmeter.fuction.BaseFunction;
import cn.bossfriday.jmeter.fuction.FunctionExecutor;
import cn.bossfriday.jmeter.utils.PocStringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static cn.bossfriday.jmeter.common.Const.ARG_NAME_CONTENT;
import static cn.bossfriday.jmeter.common.Const.ARG_NAME_LENGTH;

/**
 * LeftString
 *
 * @author chenx
 */
@FunctionExecutor.Fun(name = Const.FUNCTION_LEFT_STRING)
public class LeftString extends BaseFunction {

    public LeftString(String funName) {
        super(funName);
    }

    @Override
    public Object apply(Object... args) throws PocException {
        String content = this.getArgValue(ARG_NAME_CONTENT, args);
        Integer length = Integer.parseInt(this.getArgValue(ARG_NAME_LENGTH, args));

        return PocStringUtils.leftSubString(content, length);
    }

    @Override
    public Map<String, Integer> getArgsMap() {
        Map<String, Integer> argMap = new ConcurrentHashMap<>(16);
        argMap.put(ARG_NAME_CONTENT, 0);
        argMap.put(ARG_NAME_LENGTH, 1);

        return argMap;
    }
}
