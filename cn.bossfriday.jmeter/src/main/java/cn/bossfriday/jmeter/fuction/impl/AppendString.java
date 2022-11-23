package cn.bossfriday.jmeter.fuction.impl;

import cn.bossfriday.jmeter.common.Const;
import cn.bossfriday.jmeter.common.PocException;
import cn.bossfriday.jmeter.fuction.BaseFunction;
import cn.bossfriday.jmeter.fuction.FunctionExecutor;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AppendString
 *
 * @author chenx
 */
@FunctionExecutor.Fun(name = Const.FUNCTION_APPEND_STRING)
public class AppendString extends BaseFunction {

    public AppendString(String funName) {
        super(funName);
    }

    @Override
    public Object apply(Object... args) throws PocException {
        if (ArrayUtils.isEmpty(args)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            sb.append(arg.toString());
        }

        return sb.toString();
    }

    @Override
    public Map<String, Integer> getArgsMap() {
        return new ConcurrentHashMap<>(16);
    }

    @Override
    public String getDocument() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.funName);
        sb.append("(#{str}...)");

        return sb.toString();
    }
}
