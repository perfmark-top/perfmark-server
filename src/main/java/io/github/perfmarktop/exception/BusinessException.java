package io.github.perfmarktop.exception;

/**
 * @author YongJ
 * @date 2022/6/16 9:38
 */
public class BusinessException extends RuntimeException{
    private Integer code;
    private BizCodeEnume codeEnum;
    public Integer getCode() {
        return codeEnum.getCode();
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public BusinessException(BizCodeEnume codeEnum) {
        super(codeEnum.getMsg());
        this.code = codeEnum.getCode();
    }

    public BusinessException(String message, Throwable cause, Integer code) {
        super(message, cause);
        this.code = code;
    }
}
