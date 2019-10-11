package org.yw;

/**
 * @author https://github.com/XiFYuW
 * @date 2019/10/11 16:37
 */
public class FastDFSBean {
    private String httpSecretKey;
    private String fileServerAdder;
    private String configPath;
    private int maxStorageConnection;
    private String flagFileName;
    private String flagFileSuffix;
    private Long maxFileSize;
    private int minIdle;

    public FastDFSBean() {
    }

    public FastDFSBean(String httpSecretKey, String fileServerAdder, String configPath, int maxStorageConnection, String flagFileName, String flagFileSuffix, Long maxFileSize, int minIdle) {
        this.httpSecretKey = httpSecretKey;
        this.fileServerAdder = fileServerAdder;
        this.configPath = configPath;
        this.maxStorageConnection = maxStorageConnection;
        this.flagFileName = flagFileName;
        this.flagFileSuffix = flagFileSuffix;
        this.maxFileSize = maxFileSize;
        this.minIdle = minIdle;
    }

    public String getHttpSecretKey() {
        return httpSecretKey;
    }

    public void setHttpSecretKey(String httpSecretKey) {
        this.httpSecretKey = httpSecretKey;
    }

    public String getFileServerAdder() {
        return fileServerAdder;
    }

    public void setFileServerAdder(String fileServerAdder) {
        this.fileServerAdder = fileServerAdder;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public int getMaxStorageConnection() {
        return maxStorageConnection;
    }

    public void setMaxStorageConnection(int maxStorageConnection) {
        this.maxStorageConnection = maxStorageConnection;
    }

    public String getFlagFileName() {
        return flagFileName;
    }

    public void setFlagFileName(String flagFileName) {
        this.flagFileName = flagFileName;
    }

    public String getFlagFileSuffix() {
        return flagFileSuffix;
    }

    public void setFlagFileSuffix(String flagFileSuffix) {
        this.flagFileSuffix = flagFileSuffix;
    }

    public Long getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(Long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"httpSecretKey\":\"")
                .append(httpSecretKey).append('\"');
        sb.append(",\"fileServerAdder\":\"")
                .append(fileServerAdder).append('\"');
        sb.append(",\"configPath\":\"")
                .append(configPath).append('\"');
        sb.append(",\"maxStorageConnection\":")
                .append(maxStorageConnection);
        sb.append(",\"flagFileName\":\"")
                .append(flagFileName).append('\"');
        sb.append(",\"flagFileSuffix\":\"")
                .append(flagFileSuffix).append('\"');
        sb.append(",\"maxFileSize\":")
                .append(maxFileSize);
        sb.append(",\"minIdle\":")
                .append(minIdle);
        sb.append('}');
        return sb.toString();
    }
}
