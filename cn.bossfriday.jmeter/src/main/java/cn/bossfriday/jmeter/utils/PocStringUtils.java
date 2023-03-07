package cn.bossfriday.jmeter.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * PocStringUtils
 *
 * @author chenx
 */
public class PocStringUtils {

    private PocStringUtils() {

    }

    /**
     * leftSubString
     *
     * @param content
     * @param length
     * @return
     */
    public static String leftSubString(String content, Integer length) {
        if (StringUtils.isEmpty(content)) {
            return "";
        }

        if (length <= 0) {
            return "";
        }

        if (length >= content.length()) {
            return content;
        }

        return content.substring(0, length);
    }

    /**
     * rightSubString
     *
     * @param content
     * @param length
     * @return
     */
    public static String rightSubString(String content, Integer length) {
        if (StringUtils.isEmpty(content)) {
            return "";
        }

        if (length <= 0) {
            return "";
        }

        if (length >= content.length()) {
            return content;
        }

        Integer contentLength = content.length();
        int begin = contentLength - length;
        int end = contentLength;

        return content.substring(begin, end);
    }
}
