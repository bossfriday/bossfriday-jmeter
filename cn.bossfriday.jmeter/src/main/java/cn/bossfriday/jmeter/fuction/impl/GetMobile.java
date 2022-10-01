package cn.bossfriday.jmeter.fuction.impl;

import cn.bossfriday.jmeter.common.Const;
import cn.bossfriday.jmeter.common.PocException;
import cn.bossfriday.jmeter.fuction.BaseFunction;
import cn.bossfriday.jmeter.fuction.FunctionExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static cn.bossfriday.jmeter.common.Const.ARG_NAME_MOBILE_SEGMENT;
import static cn.bossfriday.jmeter.common.Const.ARG_NAME_SAMPLE_INDEX;

/**
 * GetMobile
 *
 * @author chenx
 */
@FunctionExecutor.Fun(name = Const.FUNCTION_GET_MOBILE)
public class GetMobile extends BaseFunction {

    public GetMobile(String funName) {
        super(funName);
    }

    @Override
    public Object apply(Object... args) throws PocException {
        String mobileSegment = this.getArgValue(ARG_NAME_MOBILE_SEGMENT, args);
        Integer sampleIndex = this.getArgValue(ARG_NAME_SAMPLE_INDEX, args);

        return mobileSegment + String.format("%8d", sampleIndex).replace(" ", "0");
    }

    @Override
    public Map<String, Integer> getArgsMap() {
        Map<String, Integer> argMap = new ConcurrentHashMap<>(16);
        argMap.put(ARG_NAME_MOBILE_SEGMENT, 0);
        argMap.put(ARG_NAME_SAMPLE_INDEX, 1);

        return argMap;
    }
}
