package cn.bossfriday.jmeter.entities;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static cn.bossfriday.jmeter.common.Const.CODE_SUCCESS;


/**
 * ApiResult
 *
 * @author chenx
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private int code;

    private String msg;

    @JSONField(name = "data")
    private T data;

    /**
     * isSuccess
     *
     * @return
     */
    public boolean isSuccess() {
        return CODE_SUCCESS == this.code;
    }
}
