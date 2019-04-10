package org.yw;

/**
 * FastDFS 上传下载时可能出现的一些异常信息
 */
public class FastDFSException extends Exception {

    
	private static final long serialVersionUID = 1L;

	/**
     * 错误码
     */
    private int code;

    /**
     * 错误消息
     */
    private String message;

    public FastDFSException(){}

    public FastDFSException(int code, String message) {
        this.code = code;
        this.message = message;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
