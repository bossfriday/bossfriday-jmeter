package cn.bossfriday.jmeter.fuction.impl;

import cn.bossfriday.jmeter.common.Const;
import cn.bossfriday.jmeter.common.PocException;
import cn.bossfriday.jmeter.fuction.BaseFunction;
import cn.bossfriday.jmeter.fuction.FunctionExecutor;
import cn.bossfriday.jmeter.utils.ChineseUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static cn.bossfriday.jmeter.common.Const.ARG_NAME_FIRST_NAME_LENGTH;

/**
 * RandomChineseName
 *
 * @author chenx
 */
@FunctionExecutor.Fun(name = Const.FUNCTION_RANDOM_CHINESE_NAME)
public class RandomChineseName extends BaseFunction {

    public RandomChineseName(String funName) {
        super(funName);
    }

    @Override
    public Object apply(Object... args) throws PocException {
        Integer firstNameLength = Integer.parseInt(this.getArgValue(ARG_NAME_FIRST_NAME_LENGTH, args).toString());
        try {
            return ChineseUtils.getRandomChineseName(firstNameLength);
        } catch (Exception ex) {
            throw new PocException(ex);
        }
    }

    @Override
    public Map<String, Integer> getArgsMap() {
        Map<String, Integer> argMap = new ConcurrentHashMap<>(16);
        argMap.put(ARG_NAME_FIRST_NAME_LENGTH, 0);

        return argMap;
    }
}
