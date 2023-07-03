package io.github.perfmarktop.exception;

/**
 * @author LukeZhang
 * @date 2023/6/30 15:23
 */
public enum BizCodeEnume {
    USER_EXCEPTION(500, "用户名或者密码错误"),
    USER_CHECK_EXCEPTION(500, "用户信息审核中，请耐心等待"),
    KAFKA_Exception(500, "kafka配置异常"),
    IMPORT_LEGAL_EXCEPTION(500, "资产清单导入失败,请检查是否填写资产IP/端口,格式是否合法"),
    CODE_EXCEPTION(500, "加密错误"),
    TOTP_EXCEPTION(500,"验证码生成错误"),
    LOGIN_EXCEPTION(500, "用户未登录"),
    TOKEN_EXCEPTION(5000, "token异常"),
    REDIS_EXCEPTION(500, "redis数据获取异常"),
    RECODE_EXCEPTION(500, "数据加密出现问题"),
    URL_FILTER_EXCEPTION(500, "拦截错误"),
    URL_NOTCONTAINS_EXCEPTION(500, "不权限包含");


    private int code;
    private String msg;

    BizCodeEnume(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
