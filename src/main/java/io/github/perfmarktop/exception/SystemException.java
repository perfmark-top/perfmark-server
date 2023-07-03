package io.github.perfmarktop.exception;

/**
 * @author YongJ
 * @date 2022/6/16 9:38
 */
public class SystemException extends RuntimeException{
    private Integer code;
    private BizCodeEnume codeEnum;


    public BizCodeEnume getCodeEnum() {
        return codeEnum;
    }

    public void setCodeEnum(BizCodeEnume codeEnum) {
        this.codeEnum = codeEnum;
    }

    public SystemException(BizCodeEnume codeEnum) {
        super(codeEnum.getMsg());
        this.code = codeEnum.getCode();
    }
    public SystemException(String msg) {
        super(msg);
    }
    public int getCode() {
        return this.codeEnum.getCode();
    }
}
