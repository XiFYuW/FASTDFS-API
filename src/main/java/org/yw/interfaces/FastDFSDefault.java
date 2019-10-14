package org.yw.interfaces;

import org.yw.FastDFSClient;
import org.yw.FastDFSErrorCode;
import org.yw.FastDFSException;
import org.yw.FastDFSResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * @author https://github.com/XiFYuW
 * @date 2019/10/14 11:31
 */
public abstract class FastDFSDefault {

    private FastDFSClient fastDFSClient;
    private volatile FastDFSResponse fastDFSResponse;

    public FastDFSDefault(FastDFSClient fastDFSClient) {
        this.fastDFSClient = fastDFSClient;
    }

    public FastDFSResponse upload(File file, String filePath, Map<String, Object> descriptions){
        try {
            fastDFSResponse = FastDFSResponse.deepClone();
            String path = fastDFSClient.upload(new FileInputStream(file),filePath,descriptions);
            String token = fastDFSClient.getToken(path);
            descriptions = fastDFSClient.getFileDescriptions(path);
            fastDFSResponse.setCode(FastDFSErrorCode.FILE_UPLOAD_SUCCEED.getCode());
            fastDFSResponse.setMessage(FastDFSErrorCode.FILE_UPLOAD_SUCCEED.getMessage());
            fastDFSResponse.setDescriptions(descriptions);
            fastDFSResponse.setToken(token);
            fastDFSResponse.setHttpUrl(fastDFSClient.getFastDFSBean().getFileServerAdder() + "/" + path + "?" + token);
            fastDFSResponse.setFilePath(path);
            fastDFSResponse.setFileName(String.valueOf(descriptions.get(fastDFSClient.getFastDFSBean().getFlagFileName())));
            fastDFSResponse.setFileType(String.valueOf(descriptions.get(fastDFSClient.getFastDFSBean().getFlagFileSuffix())));
        } catch (FastDFSException e) {
            fastDFSResponse.setCode(e.getCode());
            fastDFSResponse.setMessage(e.getMessage());
            return fastDFSResponse;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fastDFSResponse.setCode(FastDFSErrorCode.FILE_FASTDFS_QT.getCode());
            fastDFSResponse.setMessage(e.getMessage());
        }
        return fastDFSResponse;
    }

    public FastDFSResponse download(String filePath, String outPath){
        try {
            fastDFSResponse = FastDFSResponse.deepClone();
            fastDFSClient.download(filePath,outPath);
            fastDFSResponse.setCode(FastDFSErrorCode.FILE_DOWNLOAD_SUCCEED.getCode());
            fastDFSResponse.setMessage(FastDFSErrorCode.FILE_DOWNLOAD_SUCCEED.getMessage());
        } catch (FastDFSException e) {
            fastDFSResponse.setCode(e.getCode());
            fastDFSResponse.setMessage(e.getMessage());
        }
        return fastDFSResponse;
    }

    public FastDFSResponse delete(String filePath) {
        try {
            fastDFSResponse = FastDFSResponse.deepClone();
            fastDFSClient.delete(filePath);
            fastDFSResponse.setCode(FastDFSErrorCode.FILE_DELETE_SUCCEED.getCode());
            fastDFSResponse.setMessage(FastDFSErrorCode.FILE_DELETE_SUCCEED.getMessage());
        } catch (FastDFSException e) {
            fastDFSResponse.setCode(e.getCode());
            fastDFSResponse.setMessage(e.getMessage());
        }
        return fastDFSResponse;
    }

    public FastDFSClient getFastDFSClient() {
        return fastDFSClient;
    }

    public void setFastDFSClient(FastDFSClient fastDFSClient) {
        this.fastDFSClient = fastDFSClient;
    }
}
