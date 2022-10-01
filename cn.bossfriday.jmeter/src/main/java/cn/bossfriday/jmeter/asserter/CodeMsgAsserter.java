package cn.bossfriday.jmeter.asserter;

import cn.bossfriday.jmeter.AppSamplerAsserter;
import cn.bossfriday.jmeter.common.Combo3;
import cn.bossfriday.jmeter.common.Const;
import cn.bossfriday.jmeter.entities.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang3.StringUtils;

/**
 * CodeMsgAsserter
 *
 * @author chenx
 */
@AppSamplerAsserter.Asserter(name = Const.ASSERTER_CODE_MSG_ASSERT)
public class CodeMsgAsserter implements ISamplerAsserter {

    @Override
    public Combo3<Boolean, String, String> isSuccess(String response) {
        if (StringUtils.isEmpty(response)) {
            return new Combo3<>(false, "501", "response is empty");
        }

        ApiResponse<Void> apiResponse = JSON.parseObject(response, new TypeReference<ApiResponse<Void>>() {
        });

        return new Combo3<>(apiResponse.isSuccess(), String.valueOf(apiResponse.getCode()), apiResponse.getMsg());
    }
}
