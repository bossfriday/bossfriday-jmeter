package cn.bossfriday.jmeter;

import cn.bossfriday.jmeter.utils.AppSamplerUtils;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.JSyntaxTextArea;
import org.apache.jmeter.gui.util.JTextScrollPane;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.BooleanProperty;
import org.apache.jorphan.gui.JLabeledChoice;

import javax.swing.*;
import java.awt.*;

import static cn.bossfriday.jmeter.common.Const.*;

/**
 * AppSamplerGui（自定义App采样器GUI）
 *
 * @author chenx
 */
public class AppSamplerGui extends AbstractSamplerGui {

    private JTextField sampleLabel;
    private JLabeledChoice samplerType;
    private JLabeledChoice asserterType;

    private JCheckBox isLogRequest;
    private JCheckBox isLogResponse;
    private JCheckBox isHttps;

    private JLabeledChoice method;
    private JTextField url;

    private JSyntaxTextArea sampleVar = JSyntaxTextArea.getInstance(5, 80);
    JTextScrollPane sampleVarScrollPane = JTextScrollPane.getInstance(this.sampleVar);

    private JSyntaxTextArea headerData = JSyntaxTextArea.getInstance(5, 80);
    JTextScrollPane headerDataScrollPane = JTextScrollPane.getInstance(this.headerData);

    private JSyntaxTextArea bodyData = JSyntaxTextArea.getInstance(10, 80);
    JTextScrollPane bodyDataScrollPane = JTextScrollPane.getInstance(this.bodyData);

    private JSyntaxTextArea manualDoc = JSyntaxTextArea.getInstance(38, 80);
    JTextScrollPane manualDocScrollPane = JTextScrollPane.getInstance(this.manualDoc);

    private JTextField variables;
    private JTextField jsonPath;
    private JTextField defaultValues;

    public AppSamplerGui() {
        super();

        JPanel settingPanel = new VerticalPanel(10, 10);
        settingPanel.add(this.createBaseSetting());
        settingPanel.add(this.createRequestSetting());
        settingPanel.add(this.createResponseJsonExtractorSetting());
        settingPanel.add(this.createManualDoc());

        JPanel dataPanel = new JPanel(new BorderLayout(10, 10));
        dataPanel.add(settingPanel, BorderLayout.NORTH);
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(this.makeBorder());
        this.add(this.makeTitlePanel(), BorderLayout.NORTH);
        this.add(dataPanel, BorderLayout.CENTER);
    }

    @Override
    public String getLabelResource() {
        throw new IllegalStateException("This shouldn't be called");
    }

    @Override
    public TestElement createTestElement() {
        AppSampler sampler = new AppSampler();
        this.modifyTestElement(sampler);

        return sampler;
    }

    @Override
    public void modifyTestElement(TestElement testElement) {
        testElement.clear();
        this.configureTestElement(testElement);

        testElement.setProperty(GUI_SAMPLE_LABEL, this.sampleLabel.getText());
        testElement.setProperty(new BooleanProperty(GUI_IS_HTTPS, this.isHttps.isSelected()));
        testElement.setProperty(GUI_SAMPLER_TYPE, this.samplerType.getText());
        testElement.setProperty(GUI_ASSERT_TYPE, this.asserterType.getText());
        testElement.setProperty(new BooleanProperty(GUI_SAMPLE_IS_LOG_REQUEST, this.isLogRequest.isSelected()));
        testElement.setProperty(new BooleanProperty(GUI_SAMPLE_IS_LOG_RESPONSE, this.isLogResponse.isSelected()));
        testElement.setProperty(GUI_SAMPLE_VAR, this.sampleVar.getText());
        testElement.setProperty(GUI_METHOD, this.method.getText());
        testElement.setProperty(GUI_URL, this.url.getText());
        testElement.setProperty(GUI_HEADER_DATA, this.headerData.getText());
        testElement.setProperty(GUI_BODY_DATA, this.bodyData.getText());
        testElement.setProperty(GUI_VARIABLES, this.variables.getText());
        testElement.setProperty(GUI_JSON_PATHS, this.jsonPath.getText());
        testElement.setProperty(GUI_DEFAULT_VALUES, this.defaultValues.getText());
    }

    @Override
    public String getStaticLabel() {
        return GUI_SAMPLER_NAME;
    }

    @Override
    public void clearGui() {
        super.clearGui();

        this.manualDoc.setText(AppSamplerManual.getDocument());
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);

        this.sampleLabel.setText(element.getPropertyAsString(GUI_SAMPLE_LABEL));
        this.isHttps.setSelected(element.getPropertyAsBoolean(GUI_IS_HTTPS));
        this.samplerType.setText(element.getPropertyAsString(GUI_SAMPLER_TYPE));
        this.asserterType.setText(element.getPropertyAsString(GUI_ASSERT_TYPE));
        this.isLogRequest.setSelected(element.getPropertyAsBoolean(GUI_SAMPLE_IS_LOG_REQUEST));
        this.isLogResponse.setSelected(element.getPropertyAsBoolean(GUI_SAMPLE_IS_LOG_RESPONSE));
        this.sampleVar.setText(element.getPropertyAsString(GUI_SAMPLE_VAR));
        this.method.setText(element.getPropertyAsString(GUI_METHOD));
        this.url.setText(element.getPropertyAsString(GUI_URL));
        this.headerData.setText(element.getPropertyAsString(GUI_HEADER_DATA));
        this.bodyData.setText(element.getPropertyAsString(GUI_BODY_DATA));
        this.variables.setText(element.getPropertyAsString(GUI_VARIABLES));
        this.jsonPath.setText(element.getPropertyAsString(GUI_JSON_PATHS));
        this.defaultValues.setText(element.getPropertyAsString(GUI_DEFAULT_VALUES));
    }

    protected Component createBaseSetting() {
        this.sampleLabel = new JTextField(20);
        JLabel label1 = new JLabel("SampleLabel: ");
        label1.setLabelFor(this.sampleLabel);

        this.samplerType = new JLabeledChoice("", AppSamplerBuilder.getSamplerTypes(), true, false);
        this.samplerType.setPreferredSize(new Dimension(200, 30));

        this.asserterType = new JLabeledChoice("", AppSamplerAsserter.getAsserters(), false, false);
        this.asserterType.setPreferredSize(new Dimension(200, 30));

        JLabel label2 = new JLabel("    SamplerType: ");
        label2.setLabelFor(this.samplerType);

        JLabel label3 = new JLabel("    AsserterType: ");
        label3.setLabelFor(this.asserterType);

        this.isLogRequest = new JCheckBox("IsLogRequest    ");
        this.isLogRequest.setFont(null);
        this.isLogRequest.setSelected(false);

        this.isLogResponse = new JCheckBox("IsLogResponse");
        this.isLogResponse.setFont(null);
        this.isLogResponse.setSelected(false);

        JPanel panel1 = new HorizontalPanel();
        panel1.setLayout(new FlowLayout(0, 0, 0));
        panel1.add(label1, BorderLayout.WEST);
        panel1.add(this.sampleLabel, BorderLayout.WEST);
        panel1.setLayout(new FlowLayout(0, 0, 0));
        panel1.add(label2, BorderLayout.WEST);
        panel1.add(this.samplerType, BorderLayout.WEST);
        panel1.add(label3, BorderLayout.WEST);
        panel1.add(this.asserterType, BorderLayout.WEST);
        panel1.add(this.isLogRequest, BorderLayout.WEST);
        panel1.add(this.isLogResponse, BorderLayout.WEST);

        JPanel panel3 = new HorizontalPanel();
        JPanel vertPanel3 = new VerticalPanel();
        JPanel httpHeaderDataPanel3 = new JPanel(new BorderLayout());
        httpHeaderDataPanel3.add(this.sampleVarScrollPane, BorderLayout.CENTER);
        vertPanel3.add(httpHeaderDataPanel3);
        vertPanel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "Sample Variable ( Fun Supported )"));
        panel3.add(vertPanel3);

        return this.getTitledPanel("Global Setting", panel1, panel3);
    }

    protected Component createRequestSetting() {
        this.method = new JLabeledChoice("", AppSamplerUtils.getHttpMethodNames(), false, false);

        this.url = new JTextField(83);
        JLabel pathLabel = new JLabel("URL: ");
        pathLabel.setLabelFor(this.url);

        this.isHttps = new JCheckBox("IsHttps");
        this.isHttps.setFont(null);
        this.isHttps.setSelected(false);

        JPanel panel1 = new HorizontalPanel();
        panel1.setLayout(new FlowLayout(0, 0, 0));
        panel1.add(new JLabel("Method:"));
        panel1.add(this.method, BorderLayout.WEST);
        panel1.add(pathLabel, BorderLayout.WEST);
        panel1.add(this.url, BorderLayout.WEST);
        panel1.add(this.isHttps, BorderLayout.WEST);

        JPanel panel2 = new HorizontalPanel();
        JPanel headerDataContentPanel = new VerticalPanel();
        JPanel httpHeaderDataPanel = new JPanel(new BorderLayout());
        httpHeaderDataPanel.add(this.headerDataScrollPane, BorderLayout.CENTER);
        headerDataContentPanel.add(httpHeaderDataPanel);
        headerDataContentPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "Header Data"));
        panel2.add(headerDataContentPanel);

        JPanel panel3 = new HorizontalPanel();
        JPanel bodyDataContentPanel = new VerticalPanel();
        JPanel bodyDataPanel = new JPanel(new BorderLayout());
        bodyDataPanel.add(this.bodyDataScrollPane, BorderLayout.CENTER);
        bodyDataContentPanel.add(bodyDataPanel);
        bodyDataContentPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "Body Data"));
        panel3.add(bodyDataContentPanel);

        return this.getTitledPanel("Request Setting (Fun & Var Supported)", panel1, panel2, panel3);
    }

    protected Component createResponseJsonExtractorSetting() {
        this.variables = new JTextField(90);
        JLabel label1 = new JLabel("Name Of Variables: ");
        label1.setLabelFor(this.sampleLabel);
        label1.setPreferredSize(new Dimension(110, 30));

        this.jsonPath = new JTextField(90);
        JLabel label2 = new JLabel("JSON Path: ");
        label2.setLabelFor(this.jsonPath);
        label2.setPreferredSize(new Dimension(110, 30));

        this.defaultValues = new JTextField(90);
        JLabel label3 = new JLabel("Default Values: ");
        label3.setLabelFor(this.defaultValues);
        label3.setPreferredSize(new Dimension(110, 30));

        JPanel panel1 = new HorizontalPanel();
        panel1.setLayout(new FlowLayout(0, 0, 0));
        panel1.add(label1, BorderLayout.WEST);
        panel1.add(this.variables, BorderLayout.WEST);

        JPanel panel2 = new HorizontalPanel();
        panel2.setLayout(new FlowLayout(0, 0, 0));
        panel2.add(label2, BorderLayout.WEST);
        panel2.add(this.jsonPath, BorderLayout.WEST);
        panel2.add(new JLabel("(Var Spt)"), BorderLayout.WEST);

        JPanel panel3 = new HorizontalPanel();
        panel3.setLayout(new FlowLayout(0, 0, 0));
        panel3.add(label3, BorderLayout.WEST);
        panel3.add(this.defaultValues, BorderLayout.WEST);

        JPanel titledPanel = new VerticalPanel();
        JPanel borderedPanel = new VerticalPanel();
        borderedPanel.add(panel1);
        borderedPanel.add(panel2);
        borderedPanel.add(panel3);
        borderedPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        titledPanel.add(borderedPanel);
        titledPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "Response JSON Extractor Setting"));

        return titledPanel;
    }

    protected Component createManualDoc() {
        this.manualDoc.setEditable(false);
        JPanel panel = new HorizontalPanel();
        JPanel bodyDataContentPanel = new VerticalPanel();
        JPanel bodyDataPanel = new JPanel(new BorderLayout());
        bodyDataPanel.add(this.manualDocScrollPane, BorderLayout.CENTER);
        bodyDataContentPanel.add(bodyDataPanel);
        panel.add(bodyDataContentPanel);

        return this.getTitledPanel("Manual Document", panel);
    }

    /**
     * getTitledPanel
     *
     * @param title
     * @param panels
     * @return
     */
    private Component getTitledPanel(String title, JPanel... panels) {
        JPanel titledPanel = new VerticalPanel();
        JPanel borderedPanel = new VerticalPanel();
        for (JPanel panel : panels) {
            borderedPanel.add(panel);
        }

        borderedPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        titledPanel.add(borderedPanel);
        titledPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), title));

        return titledPanel;
    }
}
