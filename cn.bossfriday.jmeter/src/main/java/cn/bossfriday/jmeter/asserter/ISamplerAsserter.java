package cn.bossfriday.jmeter.asserter;

import cn.bossfriday.jmeter.common.Combo3;

/**
 * ISamplerAsserter
 *
 * @author chenx
 */
public interface ISamplerAsserter {

    /**
     * isSuccess
     *
     * @param response
     * @return Boolean: isSuccess, String: code, String: msg
     */
    Combo3<Boolean, String, String> isSuccess(String response);
}
