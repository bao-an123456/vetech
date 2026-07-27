package cn.vetech.charge.cloud.demo.common.constant;

import cn.vetech.charge.cloud.api.Code;

/**
 * 系统错误码枚举类
 */
public enum DemoExceptionEnum implements Code {

    DEMO_0001("DEMO_WORRY", "%s"),
    DEMO_0002("DEMO_NO_EXIST", "%s"),
    DEMO_0003("DEMO_PASSWORD_ERROR", "密码不正确"),
    DEMO_0004("PARAM_WORRY", "参数【%s】不合法");

    private String code;
    private String message;

    private DemoExceptionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
    @Override
    public String getMessage() {
        return this.message;
    }
    @Override
    public String getCode() {
        return this.code;
    }
}