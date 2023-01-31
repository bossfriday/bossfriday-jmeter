package cn.bossfriday.jmeter.utils;

import cn.bossfriday.jmeter.common.Combo3;
import cn.bossfriday.jmeter.common.HttpMethod;
import cn.bossfriday.jmeter.common.PocException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.*;
import org.apache.jmeter.samplers.SampleResult;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static cn.bossfriday.jmeter.common.Const.CONST_LF;
import static cn.bossfriday.jmeter.common.Const.CONST_SEMICOLON;


/**
 * AppSamplerUtils
 *
 * @author chenx
 */
public class AppSamplerUtils {

    /**
     * regex
     */
    private static final String REG_GROUP_NAME_KEY = "key";
    private static final String REG_GROUP_NAME_VALUE = "value";
    private static final String REG_KEY_VALUE = "(?<" + REG_GROUP_NAME_KEY + ">[a-zA-Z0-9_-]+)(?<colon>(:))(?<" + REG_GROUP_NAME_VALUE + ">(.*?)\n)";

    private static Pattern keyValuePattern = Pattern.compile(REG_KEY_VALUE);

    private AppSamplerUtils() {

    }

    /**
     * buildSampleResult
     *
     * @param label
     * @param respCode
     * @param respMessage
     * @param isSuccess
     * @return
     */
    public static SampleResult buildSampleResult(String label, String respCode, String respMessage, boolean isSuccess) {
        SampleResult result = new SampleResult();
        if (result.getStartTime() == 0L) {
            result.sampleStart();
        }

        result.setSampleLabel(label);
        result.setSuccessful(isSuccess);
        result.setResponseCode(respCode);
        result.setResponseMessage(respMessage);

        if (result.getEndTime() == 0L) {
            result.sampleEnd();
        }

        return result;
    }

    /**
     * getHttpMethodNames
     *
     * @return
     */
    public static String[] getHttpMethodNames() {
        String[] httpMethods = new String[HttpMethod.values().length];
        for (int i = 0; i < HttpMethod.values().length; i++) {
            httpMethods[i] = HttpMethod.values()[i].getName();
        }

        return httpMethods;
    }

    /**
     * getHttpRequest
     *
     * @param methodName
     * @return
     * @throws PocException
     */
    public static HttpRequestBase getHttpRequest(String methodName) throws PocException {
        if (methodName.equals(HttpMethod.GET.getName())) {
            return new HttpGet();
        }

        if (methodName.equals(HttpMethod.POST.getName())) {
            return new HttpPost();
        }

        if (methodName.equals(HttpMethod.HEAD.getName())) {
            return new HttpHead();
        }

        if (methodName.equals(HttpMethod.PUT.getName())) {
            return new HttpPut();
        }

        if (methodName.equals(HttpMethod.OPTIONS.getName())) {
            return new HttpOptions();
        }

        if (methodName.equals(HttpMethod.TRACE.getName())) {
            return new HttpTrace();
        }

        if (methodName.equals(HttpMethod.DELETE.getName())) {
            return new HttpDelete();
        }

        if (methodName.equals(HttpMethod.PATCH.getName())) {
            return new HttpPatch();
        }

        throw new PocException(String.format("invalid http method name: %s", methodName));
    }

    /**
     * getKvMap
     *
     * @param data
     * @return
     */
    public static LinkedHashMap<String, String> getKvMap(String data) throws PocException {
        LinkedHashMap<String, String> resultMap = new LinkedHashMap<>(16);
        if (StringUtils.isEmpty(data)) {
            return resultMap;
        }

        if (!data.endsWith(CONST_LF)) {
            data += CONST_LF;
        }

        Matcher matcher = keyValuePattern.matcher(data);
        while (matcher.find()) {
            String key = matcher.group(REG_GROUP_NAME_KEY).trim();
            String value = matcher.group(REG_GROUP_NAME_VALUE).trim();

            if (StringUtils.isEmpty(value)) {
                continue;
            }

            if (resultMap.putIfAbsent(key, value) != null) {
                throw new PocException(String.format("duplicated key: %s", key));
            }
        }

        return resultMap;
    }

    /**
     * getJsonExtractorSetting
     *
     * @param variables
     * @param jsonPaths
     * @param defaultValues
     * @return
     */
    public static List<Combo3<String, String, String>> getJsonExtractorSetting(String variables, String jsonPaths, String defaultValues) throws PocException {
        if (StringUtils.isEmpty(variables) || StringUtils.isEmpty(jsonPaths)) {
            return Collections.emptyList();
        }

        variables = AppSamplerUtils.subStringLastSuffix(variables, CONST_SEMICOLON);
        jsonPaths = AppSamplerUtils.subStringLastSuffix(jsonPaths, CONST_SEMICOLON);
        defaultValues = AppSamplerUtils.subStringLastSuffix(defaultValues, CONST_SEMICOLON);

        String[] varArr = variables.split(CONST_SEMICOLON);
        String[] jsonPathArr = jsonPaths.split(CONST_SEMICOLON);
        String[] defaultValueArr = defaultValues.split(CONST_SEMICOLON);

        // 配对检查
        if (varArr.length != jsonPathArr.length) {
            throw new PocException("variables count not equal jsonPaths count!");
        }

        Set<String> duplicatedVarCheckTmpSet = new HashSet<>();
        List<Combo3<String, String, String>> result = new ArrayList<>();
        for (int i = 0; i < varArr.length; i++) {
            String varName = varArr[i];
            String jsonPath = jsonPathArr[i];
            String defaultValue = (defaultValueArr.length <= i) ? "" : defaultValueArr[i];

            // 重复变量检查
            if (!duplicatedVarCheckTmpSet.contains(varName)) {
                duplicatedVarCheckTmpSet.add(varName);
            } else {
                throw new PocException(String.format("duplicated variable!(%s)", varName));
            }

            result.add(new Combo3<>(varName, jsonPath, defaultValue));
        }

        return result;
    }

    /**
     * subStringLastSuffix
     *
     * @param input
     * @param suffix
     * @return
     */
    public static String subStringLastSuffix(String input, String suffix) {
        if (StringUtils.isEmpty(input)) {
            return input;
        }

        if (input.endsWith(suffix)) {
            return input.substring(0, input.lastIndexOf(suffix));
        }

        return input;
    }

    /**
     * sortMapByValue
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V extends Comparable> Map<K, V> sortMapByValue(Map<K, V> map) {
        LinkedHashMap<K, V> finalMap = new LinkedHashMap<>();
        List<Map.Entry<K, V>> list = map.entrySet()
                .stream()
                .sorted((p1, p2) -> p1.getValue().compareTo(p2.getValue()))
                .collect(Collectors.toList());
        list.forEach(ele -> finalMap.put(ele.getKey(), ele.getValue()));

        return finalMap;
    }

    /**
     * sortMapByKey
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K extends Comparable, V> Map<K, V> sortMapByKey(Map<K, V> map) {
        LinkedHashMap<K, V> finalMap = new LinkedHashMap<>();
        List<Map.Entry<K, V>> list = map.entrySet()
                .stream()
                .sorted((p1, p2) -> p1.getKey().compareTo(p2.getKey()))
                .collect(Collectors.toList());
        list.forEach(ele -> finalMap.put(ele.getKey(), ele.getValue()));

        return finalMap;
    }
}
