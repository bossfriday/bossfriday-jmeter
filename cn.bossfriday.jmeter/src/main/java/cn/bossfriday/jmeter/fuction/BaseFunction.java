package cn.bossfriday.jmeter.fuction;

import cn.bossfriday.jmeter.common.PocException;
import cn.bossfriday.jmeter.utils.AppSamplerUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;

import static cn.bossfriday.jmeter.common.Const.ARG_NAME_SAMPLE_INDEX;

/**
 * BaseFunction
 *
 * @author chenx
 */
public abstract class BaseFunction {

    protected String funName;
    protected Map<String, Integer> argsMap;

    protected BaseFunction(String funName) {
        this.funName = funName;

        this.argsMap = this.getArgsMap();
        this.argsMap.putIfAbsent(ARG_NAME_SAMPLE_INDEX, this.argsMap.size());
    }

    /**
     * apply
     *
     * @param args
     * @return
     * @throws PocException
     */
    public abstract Object apply(Object... args) throws PocException;

    /**
     * getArgs
     *
     * @return
     */
    public abstract Map<String, Integer> getArgsMap();

    /**
     * getArgValue
     *
     * @param argName
     * @param args
     * @return
     * @throws PocException
     */
    public <T> T getArgValue(String argName, Object... args) throws PocException {
        if (MapUtils.isEmpty(this.argsMap)) {
            throw new PocException(String.format("Function args is empty! funName:%s", this.funName));
        }

        if (!this.argsMap.containsKey(argName)) {
            throw new PocException(String.format("Function argKey not existed! argName:%s", argName));
        }

        int argIndex = this.argsMap.get(argName);
        if (args.length <= argIndex) {
            throw new PocException(String.format("Function argValue not existed! argName:%s", argName));
        }

        return (T) args[argIndex];
    }

    /**
     * getDocument
     *
     * @return
     */
    public String getDocument() {
        Map<String, Integer> sortedArgsMap = AppSamplerUtils.sortMapByValue(this.getArgsMap());
        StringBuilder sb = new StringBuilder();
        sb.append(this.funName);
        sb.append("(");
        int index = 0;
        for (Map.Entry<String, Integer> entry : sortedArgsMap.entrySet()) {
            sb.append("#");
            sb.append("{");
            sb.append(entry.getKey());
            sb.append("}");

            if (index != sortedArgsMap.size() - 1) {
                sb.append(", ");
            }

            index++;
        }
        sb.append(")");

        return sb.toString();
    }
}
