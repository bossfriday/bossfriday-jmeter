package cn.bossfriday.jmeter.fuction;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CsvDataReader
 *
 * @author chenx
 * @date 2022/09/22
 */
public class CsvDataReader {

    private static final Logger log = LogManager.getLogger(CsvDataReader.class);
    private static volatile CsvDataReader instance;

    private static ConcurrentHashMap<String, List<CSVRecord>> cvsDataMap = new ConcurrentHashMap<>();

    private CsvDataReader() {

    }

    /**
     * getInstance
     *
     * @return
     */
    public static CsvDataReader getInstance() {
        if (instance == null) {
            synchronized (CsvDataReader.class) {
                if (instance == null) {
                    instance = new CsvDataReader();
                }
            }
        }

        return instance;
    }

    /**
     * getCsvData
     *
     * @param csvFileName
     * @return
     */
    public synchronized List<CSVRecord> getCsvData(String csvFileName) {
        if (StringUtils.isEmpty(csvFileName)) {
            return Collections.emptyList();
        }

        if (cvsDataMap.containsKey(csvFileName)) {
            return cvsDataMap.get(csvFileName);
        }

        try {
            CSVFormat format = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .build();
            CSVParser csvRecordsWithFirstLineHeader = format.parse(new FileReader(csvFileName + ".csv"));
            List<CSVRecord> csvDataList = csvRecordsWithFirstLineHeader.getRecords();
            cvsDataMap.put(csvFileName, csvDataList);
            log.info("load csvFile done, csvFileName={}", csvFileName);
        } catch (Exception ex) {
            log.error(String.format("CsvDataReader.getCsvData(%s) error!", csvFileName), ex);
        }

        return cvsDataMap.get(csvFileName);
    }
}
