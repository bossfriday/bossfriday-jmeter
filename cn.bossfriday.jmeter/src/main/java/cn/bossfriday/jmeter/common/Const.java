package cn.bossfriday.jmeter.common;

/**
 * Const
 *
 * @author chenx
 */
public class Const {

    private Const() {

    }

    /**
     * const
     */
    public static final String CONST_CSV = "csv";
    public static final String CONST_LF = "\n";
    public static final String CONST_DOT = ".";
    public static final String CONST_SEMICOLON = ";";

    /**
     * GUI
     */
    public static final String GUI_SAMPLER_NAME = "BossFriday CommonHttp Sampler";
    public static final String GUI_SAMPLER_TYPE = "samplerType";
    public static final String GUI_SAMPLE_LABEL = "sampleLabel";
    public static final String GUI_ASSERT_TYPE = "assertType";
    public static final String GUI_SAMPLE_IS_LOG_REQUEST = "isLogRequest";
    public static final String GUI_SAMPLE_IS_LOG_RESPONSE = "isLogResponse";
    public static final String GUI_SAMPLE_VAR = "sampleVar";
    public static final String GUI_IS_HTTPS = "isHttps";
    public static final String GUI_METHOD = "method";
    public static final String GUI_URL = "url";
    public static final String GUI_HEADER_DATA = "headerData";
    public static final String GUI_BODY_DATA = "bodyData";
    public static final String GUI_VARIABLES = "variables";
    public static final String GUI_JSON_PATHS = "jsonPaths";
    public static final String GUI_DEFAULT_VALUES = "defaultValues";
    public static final String GUI_MANUAL_DOC = "manualDoc";

    /**
     * Code
     */
    public static final int CODE_SUCCESS = 0;
    public static final int CODE_SAMPLE_ERROR = 500;
    public static final int CODE_TEST_STARTED_ERROR = 400;

    /**
     * Response message
     */
    public static final String RESPONSE_MESSAGE_STARTED_ERROR = "testStarted error!";

    /**
     * Sampler
     */
    public static final String SAMPLER_COMMON = "common";

    /**
     * Asserter
     */
    public static final String ASSERTER_CODE_MSG_ASSERT = "codeMsgAssert";

    /**
     * Function
     */
    public static final String FUNCTION_APPEND_STRING = "appendString";
    public static final String FUNCTION_RANDOM_MOBILE = "randomMobile";
    public static final String FUNCTION_GET_MOBILE = "getMobile";
    public static final String FUNCTION_RANDOM_CSV_DATA = "randomCsvData";
    public static final String FUNCTION_GET_CSV_DATA = "getCsvData";
    public static final String FUNCTION_CIRCLE_CSV_DATA = "circleCsvData";
    public static final String FUNCTION_MOD_CSV_DATA = "modCsvData";
    public static final String FUNCTION_GET_GROUP_NAME = "getGroupName";
    public static final String FUNCTION_GET_DEPT_NAME = "getDeptName";
    public static final String FUNCTION_RANDOM_CHINESE_NAME = "randomChineseName";
    public static final String FUNCTION_GET_TIMESTAMP = "getTs";
    public static final String FUNCTION_MATH_ADD = "mathAdd";
    public static final String FUNCTION_MATH_SUBTRACT = "mathSubtract";
    public static final String FUNCTION_MATH_MULTIPLY = "mathMultiply";
    public static final String FUNCTION_MATH_DIVIDE = "mathDivide";

    /**
     * ArgName
     */
    public static final String ARG_NAME_SAMPLE_INDEX = "sampleIndex";
    public static final String ARG_NAME_MOBILE_SEGMENT = "mobileSegment";
    public static final String ARG_NAME_START = "start";
    public static final String ARG_NAME_END = "end";
    public static final String ARG_NAME_CSV_FILE_NAME = "csvFileName";
    public static final String ARG_NAME_VAR_NAME = "varName";
    public static final String ARG_NAME_MOD_NAME = "mod";
    public static final String ARG_NAME_FIRST_NAME_LENGTH = "firstNameLength";
    public static final String ARG_NAME_X = "x";
    public static final String ARG_NAME_Y = "y";

    /**
     * Others
     */
    public static final int SAMPLE_LOG_INTERVAL_COUNT = 1000;
    public static final Long SAMPLE_VAR_MAP_DURATION = 3600 * 10 * 1000L;
}
