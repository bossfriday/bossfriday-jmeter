package cn.bossfriday.jmeter.component;

import cn.bossfriday.jmeter.common.Combo3;
import cn.bossfriday.jmeter.common.PocException;
import cn.bossfriday.jmeter.entities.SampleExtractorEntry;
import cn.bossfriday.jmeter.entities.SamplerSetting;
import cn.bossfriday.jmeter.expression.ExpressionNode;
import cn.bossfriday.jmeter.expression.ExpressionNodeType;
import cn.bossfriday.jmeter.expression.ExpressionParser;
import cn.bossfriday.jmeter.utils.AppSamplerUtils;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import static cn.bossfriday.jmeter.common.Const.CONST_CSV;
import static cn.bossfriday.jmeter.common.Const.CONST_DOT;


/**
 * 采样结果提取元件
 *
 * @author chenx
 */
public class SampleResultExtractor extends BaseSamplerComponent {

    private static final Logger log = LogManager.getLogger(SampleResultExtractor.class);

    private CSVPrinter csvWriter;
    private List<String[]> synchronizedWriterDataList;

    @Getter
    private List<SampleExtractorEntry> extractorEntryList;

    public SampleResultExtractor(SamplerSetting setting) {
        super(setting);

        this.extractorEntryList = new ArrayList<>();
        this.synchronizedWriterDataList = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public void testStarted() throws PocException {
        try {
            List<Combo3<String, String, String>> extractorComboList = AppSamplerUtils.getJsonExtractorSetting(this.setting.getVariables(),
                    this.setting.getJsonPath(),
                    this.setting.getDefaultValues());
            this.setExtractorEntryList(extractorComboList);

            this.initCsvWriter(extractorComboList);
        } catch (Exception ex) {
            throw new PocException(ex);
        }
    }

    @Override
    public void testEnded() {
        // 为避免多线程写文件，落盘放在采样完成阶段（如果采样数较大时，打压机内存需要够用）
        this.writeAndFlushData();
    }

    /**
     * addRecord
     *
     * @param csvRecord
     */
    public void addRecord(String[] csvRecord) {
        if (ArrayUtils.isEmpty(csvRecord)) {
            return;
        }

        this.synchronizedWriterDataList.add(csvRecord);
    }

    /**
     * setExtractorEntryList
     *
     * @param extractorComboList
     */
    private void setExtractorEntryList(List<Combo3<String, String, String>> extractorComboList) throws PocException {
        if (CollectionUtils.isEmpty(extractorComboList)) {
            return;
        }

        for (Combo3<String, String, String> combo : extractorComboList) {
            String varName = combo.getV1();
            String jsonPath = combo.getV2();
            String defaultValue = combo.getV3();

            // parser entry
            ExpressionParser parser = new ExpressionParser(jsonPath);
            parser.parse();
            Deque<ExpressionNode> expNodeStack = parser.getExpNodeStack();
            if (expNodeStack.size() != 1) {
                throw new PocException("JSON Path setting only support : constant or variable!");
            }

            ExpressionNode expressionNode = parser.getExpNodeStack().getFirst();
            if (expressionNode.getNodeType() != ExpressionNodeType.VARIABLE.getType() && expressionNode.getNodeType() != ExpressionNodeType.CONSTANT.getType()) {
                throw new PocException("JSON Path setting only support : constant or variable!");
            }

            SampleExtractorEntry entry = SampleExtractorEntry.builder()
                    .varName(varName)
                    .jsonPath(jsonPath)
                    .defaultValue(defaultValue)
                    .expressionNode(expressionNode)
                    .build();
            this.extractorEntryList.add(entry);
        }
    }

    /**
     * writeAndFlushData
     */
    private void writeAndFlushData() {
        try {
            if (this.csvWriter == null) {
                return;
            }

            for (String[] csvRecord : this.synchronizedWriterDataList) {
                this.csvWriter.printRecord(csvRecord);
            }

            this.csvWriter.flush();
            this.csvWriter.close();
            log.info("writeAndFlushData() done, writerData.size={}", this.synchronizedWriterDataList.size());
        } catch (Exception ex) {
            log.error("writeAndFlushData() error!", ex);
        }
    }

    /**
     * initCsvWriter
     *
     * @throws IOException
     * @throws PocException
     */
    private void initCsvWriter(List<Combo3<String, String, String>> extractorComboList) throws IOException, PocException {
        String[] csvHeader = this.getCsvHeaders(extractorComboList);
        if (ArrayUtils.isEmpty(csvHeader)) {
            return;
        }

        String csvFileName = this.setting.getSampleLabel();
        File file = new File(getCsvFileName(csvFileName));
        // 重复执行压测用例自动删除之前产生的数据
        if (file.exists()) {
            Files.delete(file.toPath());
        }

        this.csvWriter = new CSVPrinter(new FileWriter(file), CSVFormat.DEFAULT);
        this.csvWriter.printRecord(csvHeader);
    }

    /**
     * getCsvFileName
     *
     * @param csvFileName
     * @return
     */
    private static String getCsvFileName(String csvFileName) throws PocException {
        if (StringUtils.isEmpty(csvFileName)) {
            throw new PocException("csv file name is empty!");
        }

        return csvFileName + CONST_DOT + CONST_CSV;
    }

    /**
     * getCsvHeaders
     *
     * @param extractorComboList
     * @return
     */
    private String[] getCsvHeaders(List<Combo3<String, String, String>> extractorComboList) {
        if (CollectionUtils.isEmpty(extractorComboList)) {
            return new String[0];
        }

        String[] headers = new String[extractorComboList.size()];
        for (int i = 0; i < extractorComboList.size(); i++) {
            headers[i] = extractorComboList.get(i).getV1();
        }

        return headers;
    }
}
