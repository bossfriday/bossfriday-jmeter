package cn.bossfriday.jmeter.fuction.impl;

import cn.bossfriday.jmeter.common.Const;
import cn.bossfriday.jmeter.common.PocException;
import cn.bossfriday.jmeter.fuction.BaseFunction;
import cn.bossfriday.jmeter.fuction.CsvDataReader;
import cn.bossfriday.jmeter.fuction.FunctionExecutor;
import org.apache.commons.csv.CSVRecord;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static cn.bossfriday.jmeter.common.Const.*;

/**
 * ModCsvData
 *
 * @author chenx
 */
@FunctionExecutor.Fun(name = Const.FUNCTION_MOD_CSV_DATA)
public class ModCsvData extends BaseFunction {
    public ModCsvData(String funName) {
        super(funName);
    }

    @Override
    public Object apply(Object... args) throws PocException {
        String csvFileName = this.getArgValue(ARG_NAME_CSV_FILE_NAME, args);
        String varName = this.getArgValue(ARG_NAME_VAR_NAME, args);
        Integer sampleIndex = this.getArgValue(ARG_NAME_SAMPLE_INDEX, args);
        Integer mod = Integer.parseInt(this.getArgValue(ARG_NAME_MOD, args).toString());
        List<CSVRecord> csvRecords = CsvDataReader.getInstance().getCsvData(csvFileName);

        return csvRecords.get(sampleIndex % mod).get(varName);
    }

    @Override
    public Map<String, Integer> getArgsMap() {
        Map<String, Integer> argMap = new ConcurrentHashMap<>(16);
        argMap.put(ARG_NAME_CSV_FILE_NAME, 0);
        argMap.put(ARG_NAME_VAR_NAME, 1);
        argMap.put(ARG_NAME_SAMPLE_INDEX, 2);
        argMap.put(ARG_NAME_MOD, 3);

        return argMap;
    }
}
