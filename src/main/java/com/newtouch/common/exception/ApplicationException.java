package com.newtouch.common.exception;

/**
 * 应用异常，如果在代码中明确的知道错误情况，请包装异常为本异常
 *
 * @author dongfeng.zhang
 */
public class ApplicationException extends RuntimeException {
	private static final long serialVersionUID = 4479687565961973770L;
	public static final String PREFIX="err.";
	private String code;
    private String message;
    private Object[] vargs;

    /**
     * 仅指定异常代码
     *
     * @param errorCode
     */
    public ApplicationException(String errorCode) {
        super();
        this.code = errorCode;
        this.message = "";
    }

    /**
     * 仅指定异常代码
     *
     * @param errorCode
     */
    public ApplicationException(String errorCode,Object... vargs) {
        super();
        this.code = errorCode;
        this.vargs=vargs;
    }
    /**
     * 仅指定异常代码
     *
     * @param errorCode
     */
    public ApplicationException(String errorCode, String message) {
        super();
        this.code = errorCode;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object[] getVargs(){return vargs;}
}
