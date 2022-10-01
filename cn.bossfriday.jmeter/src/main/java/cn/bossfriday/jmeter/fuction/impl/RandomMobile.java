package cn.bossfriday.jmeter.fuction.impl;

import cn.bossfriday.jmeter.common.Const;
import cn.bossfriday.jmeter.common.PocException;
import cn.bossfriday.jmeter.fuction.BaseFunction;
import cn.bossfriday.jmeter.fuction.FunctionExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import static cn.bossfriday.jmeter.common.Const.*;

/**
 * RandomMobile
 *
 * @author chenx
 */
@FunctionExecutor.Fun(name = Const.FUNCTION_RANDOM_MOBILE)
public class RandomMobile extends BaseFunction {

    public RandomMobile(String funName) {
        super(funName);
    }

    @Override
    public Object apply(Object... args) throws PocException {
        String mobileSegment = this.getArgValue(ARG_NAME_MOBILE_SEGMENT, args);
        Integer start = Integer.parseInt(this.getArgValue(ARG_NAME_START, args).toString());
        Integer end = Integer.parseInt(this.getArgValue(ARG_NAME_END, args).toString());

        return mobileSegment + String.format("%8d", ThreadLocalRandom.current().nextInt(start, end)).replace(" ", "0");
    }

    @Override
    public Map<String, Integer> getArgsMap() {
        Map<String, Integer> argMap = new ConcurrentHashMap<>(16);
        argMap.put(ARG_NAME_MOBILE_SEGMENT, 0);
        argMap.put(ARG_NAME_START, 1);
        argMap.put(ARG_NAME_END, 2);

        return argMap;
    }
}
