package org.yw;

import java.util.Map;

/**
 * 上传文件后的数据返回对象，便于前台获取数据.
 */

public class FastDFSResponse {

    /**
     * 返回状态编码
     */
    private int code;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 成功标识
     */
    private boolean success = true;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * Http URL
     */
    private String httpUrl;

    /**
     * Http Token
     */
    private String token;

    /**
     * fastdfs文件详情
     */
    private Map<String, Object> descriptions;

    public FastDFSResponse(){}

    public FastDFSResponse(int code, String message, boolean success, String filePath, String fileName, String fileType, String httpUrl, String token, Map<String, Object> descriptions) {
        this.code = code;
        this.message = message;
        this.success = success;
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileType = fileType;
        this.httpUrl = httpUrl;
        this.token = token;
        this.descriptions = descriptions;
    }

    public FastDFSResponse(boolean success) {
        this.success = success;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Map<String, Object> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(Map<String, Object> descriptions) {
        this.descriptions = descriptions;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"code\":")
                .append(code);
        sb.append(",\"message\":\"")
                .append(message).append('\"');
        sb.append(",\"success\":")
                .append(success);
        sb.append(",\"filePath\":\"")
                .append(filePath).append('\"');
        sb.append(",\"fileName\":\"")
                .append(fileName).append('\"');
        sb.append(",\"fileType\":\"")
                .append(fileType).append('\"');
        sb.append(",\"httpUrl\":\"")
                .append(httpUrl).append('\"');
        sb.append(",\"token\":\"")
                .append(token).append('\"');
        sb.append(",\"descriptions\":")
                .append(descriptions);
        sb.append('}');
        return sb.toString();
    }

}
