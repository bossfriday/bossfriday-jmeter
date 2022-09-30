package cn.bossfriday.jmeter.fuction.impl;

import cn.bossfriday.jmeter.common.Const;
import cn.bossfriday.jmeter.common.PocException;
import cn.bossfriday.jmeter.fuction.BaseFunction;
import cn.bossfriday.jmeter.fuction.FunctionExecutor;
import cn.bossfriday.jmeter.utils.ChineseUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static cn.bossfriday.jmeter.common.Const.ARG_NAME_SAMPLE_INDEX;

/**
 * GetGroupName
 *
 * @author chenx
 */
@FunctionExecutor.Fun(name = Const.FUNCTION_GET_GROUP_NAME)
public class GetGroupName extends BaseFunction {

    public GetGroupName(String funName) {
        super(funName);
    }

    @Override
    public Object apply(Object... args) throws PocException {
        Integer sampleIndex = this.getArgValue(ARG_NAME_SAMPLE_INDEX, args);

        return ChineseUtils.getGroupName(sampleIndex);
    }

    @Override
    public Map<String, Integer> getArgsMap() {
        Map<String, Integer> argMap = new ConcurrentHashMap<>(16);
        argMap.put(ARG_NAME_SAMPLE_INDEX, 0);

        return argMap;
    }
}
