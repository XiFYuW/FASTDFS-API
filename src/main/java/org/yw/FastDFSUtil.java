package org.yw;

import org.csource.fastdfs.StorageClient1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FastDFSUtil {

    private static Logger logger = LoggerFactory.getLogger(FastDFSUtil.class);
    /**
     * 路径分隔符
     */
    private static final String SEPARATOR = "/";
    /**
     * Point
     */
    private static final String POINT = ".";

    public static boolean isNotBlank(String str){
        int strLen;
        if(str == null || (strLen = str.length()) == 0) {
            return false;
        }
        for(int i = 0; i < strLen; i++){
            if(!Character.isWhitespace(str.charAt(i))){
                return true;
            }
        }
        return false;
    }


    /**
     * 获取文件名称的后缀
     *
     * @param filename
     *            文件名 或 文件路径
     * @return 文件后缀
     */
    public static String getFilenameSuffix(String filename) {
        String suffix = null;
        String originalFilename = filename;
        if (FastDFSUtil.isNotBlank(filename)) {
            if (filename.contains(SEPARATOR)) {
                filename = filename.substring(filename.lastIndexOf(SEPARATOR) + 1);
            }
            if (filename.contains(POINT)) {
                suffix = filename.substring(filename.lastIndexOf(POINT) + 1);
            } else {
                if (logger.isErrorEnabled()) {
                    logger.error("filename error without suffix : {}", originalFilename);
                }
            }
        }
        return suffix;
    }

    /**
     * 转换路径中的 '\' 为 '/' <br>
     * 并把文件后缀转为小写
     *
     * @param path
     *            路径
     * @return
     */
    public static String toLocal(String path) {
        if (FastDFSUtil.isNotBlank(path)) {
            path = path.replaceAll("\\\\", SEPARATOR);

            if (path.contains(POINT)) {
                String pre = path.substring(0, path.lastIndexOf(POINT) + 1);
                String suffix = path.substring(path.lastIndexOf(POINT) + 1).toLowerCase();
                path = pre + suffix;
            }
        }
        return path;
    }

    /**
     * 获取FastDFS文件的名称
     *
     * @param fileId
     *            包含组名和文件名，如：group1/M00/00/00/wKgzgFnkTPyAIAUGAAEoRmXZPp876.jpeg
     * @return FastDFS 返回的文件名：M00/00/00/wKgzgFnkTPyAIAUGAAEoRmXZPp876.jpeg
     */
    public static String getFastDFSFileName(String fileId) {
        String[] results = new String[2];
        StorageClient1.split_file_id(fileId, results);
        return results[1];
    }

    public static String getFileName(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    public static InputStream checkFilePathIlleng(String filePath) throws FastDFSException{
        if(!FastDFSUtil.isNotBlank(filePath)){
            throw new FastDFSException(FastDFSErrorCode.FILE_LOCAL_PATH_ISNULL.getCode(), FastDFSErrorCode.FILE_LOCAL_PATH_ISNULL.getMessage());
        }
        File file = new File(filePath);
        if(!file.exists()){
            throw new FastDFSException(FastDFSErrorCode.FILE_NOT_EXIST.getCode(), FastDFSErrorCode.FILE_NOT_EXIST.getMessage());
        }
        InputStream in;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new FastDFSException(FastDFSErrorCode.FILE_FASTDFS_QT.getCode(), FastDFSErrorCode.FILE_FASTDFS_QT.getMessage());
        }
        return in;
    }
}
