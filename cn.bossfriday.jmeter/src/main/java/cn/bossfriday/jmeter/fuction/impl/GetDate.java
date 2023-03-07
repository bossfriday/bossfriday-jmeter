package cn.bossfriday.jmeter.fuction.impl;

import cn.bossfriday.jmeter.common.Const;
import cn.bossfriday.jmeter.common.PocException;
import cn.bossfriday.jmeter.fuction.BaseFunction;
import cn.bossfriday.jmeter.fuction.FunctionExecutor;
import cn.bossfriday.jmeter.utils.DateUtil;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static cn.bossfriday.jmeter.common.Const.*;
import static java.util.Calendar.ZONE_OFFSET;

/**
 * GetFormatTime
 *
 * @author chenx
 */
@FunctionExecutor.Fun(name = Const.FUNCTION_GET_DATE)
public class GetDate extends BaseFunction {

    public GetDate(String funName) {
        super(funName);
    }

    @Override
    public Object apply(Object... args) throws PocException {
        String dateString = this.getArgValue(ARG_NAME_DATE, args);
        String pattern = this.getArgValue(ARG_NAME_PATTERN, args);
        Integer field = Integer.parseInt(this.getArgValue(ARG_NAME_FIELD, args));
        Integer amount = Integer.parseInt(this.getArgValue(ARG_NAME_AMOUNT, args));

        if (field < 0 || field >= ZONE_OFFSET) {
            throw new PocException("Invalid field number!");
        }

        Calendar calendar = null;
        try {
            calendar = DateUtil.str2Calendar(dateString, pattern);
            calendar.add(field, amount);
        } catch (ParseException e) {
            throw new PocException("Invalid dateString or  pattern!");
        }

        return DateUtil.date2Str(calendar.getTime(), pattern);
    }

    @Override
    public Map<String, Integer> getArgsMap() {
        Map<String, Integer> argMap = new ConcurrentHashMap<>(16);
        argMap.put(ARG_NAME_DATE, 0);
        argMap.put(ARG_NAME_PATTERN, 1);
        argMap.put(ARG_NAME_FIELD, 2);
        argMap.put(ARG_NAME_AMOUNT, 3);

        return argMap;
    }

    @Override
    public String getDocument() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getDocument());
        sb.append("    // ");
        sb.append("field:Calendar.XXField; 1:Year; 2:Mon; 5:Day; 10:Hour; 12:Min; 13:Sec;");

        return sb.toString();
    }
}
