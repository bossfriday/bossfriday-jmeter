package cn.bossfriday.jmeter;

import cn.bossfriday.jmeter.fuction.FunctionExecutor;

import static cn.bossfriday.jmeter.common.Const.CONST_LF;

/**
 * SamplerManual
 *
 * @author chenx
 */
public class AppSamplerManual {

    private AppSamplerManual() {

    }

    /**
     * getDocument
     *
     * @return
     */
    public static String getDocument() {
        StringBuilder sb = new StringBuilder();

        sb.append("1. Sample Variable Example:" + CONST_LF);
        sb.append("lang: en" + CONST_LF);
        sb.append("mobile: getMobile(138)" + CONST_LF);
        sb.append(CONST_LF);

        sb.append("2. URL Example:" + CONST_LF);
        sb.append("localhost:9033/baseCheck" + CONST_LF);
        sb.append("localhost:9033/baseCheck?id=#{id}" + CONST_LF);
        sb.append("localhost:9033/baseCheck?mobile=getMobile(138)" + CONST_LF);
        sb.append(CONST_LF);

        sb.append("3. Header Example:" + CONST_LF);
        sb.append("content-type: application/json" + CONST_LF);
        sb.append("uid: #{uid}" + CONST_LF);
        sb.append("mobile: getMobile(138)" + CONST_LF);
        sb.append(CONST_LF);

        sb.append("4. Body Example:" + CONST_LF);
        sb.append("mobile=13810494632&region=86&vCode=966966" + CONST_LF);
        sb.append("mobile=#{mobile}&region=86&deviceCode=1" + CONST_LF);
        sb.append("mobile=getMobile(138)&region=86&deviceCode=1" + CONST_LF);
        sb.append(CONST_LF);

        sb.append("5. Name Of Variable / Default Values Example:" + CONST_LF);
        sb.append("uid;appToken;imToken" + CONST_LF);
        sb.append(CONST_LF);

        sb.append("6. JSON Path Example:" + CONST_LF);
        sb.append("$.data.uid;$.data.appToken;$.data.imToken" + CONST_LF);
        sb.append("#{mobile};$.data.uid;$.data.appToken;$.data.imToken" + CONST_LF);
        sb.append("getMobile(138);$.data.uid;$.data.appToken;$.data.imToken" + CONST_LF);
        sb.append(CONST_LF);

        sb.append("7. Implemented Functions:" + CONST_LF);
        sb.append(FunctionExecutor.getInstance().getDocument());
        sb.append(CONST_LF);

        return sb.toString();
    }
}
