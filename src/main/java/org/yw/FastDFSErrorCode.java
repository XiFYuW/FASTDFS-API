package org.yw;

/**
 *	文件服务器描述文本
 */
public enum FastDFSErrorCode {
	/**FASTDFS文件路径为空*/
    FILE_FASTDFS_PATH_ISNULL(3000, "FASTDFS文件路径为空"),
    /**本地文件路径为空*/
    FILE_LOCAL_PATH_ISNULL(4000, "本地文件路径为空"),
    /**文件为空*/
    FILE_ISNULL(3001, "文件为空"),
    /**文件上传失败*/
    FILE_UPLOAD_FAILED(3002, "文件上传失败"),
    /**文件上传成功*/
    FILE_UPLOAD_SUCCEED(3021, "文件上传成功"),
    /**文件不存在*/
    FILE_NOT_EXIST(3003, "文件不存在"),
    /**文件下载失败*/
    FILE_DOWNLOAD_FAILED(3004, "文件下载失败"),
    /**文件下载成功*/
    FILE_DOWNLOAD_SUCCEED(3041, "文件下载成功"),
    /**删除文件失败*/
    FILE_DELETE_FAILED(3005, "删除文件失败"),
    /**删除文件成功*/
    FILE_DELETE_SUCCEED(3015, "删除文件成功"),
    /**文件类型不一致*/
    FILE_TYPE_FIT(3089, "文件类型不一致"),
    /**文件服务器连接失败*/
    FILE_SERVER_CONNECTION_FAILED(3006, "文件服务器连接失败"),
	/**文件服务器异常，请联系管理员*/
	FILE_FASTDFS_ERR(3013, "文件服务器异常，请联系管理员"),
    /**其他*/
    FILE_FASTDFS_QT(5050, ""),
    /**文件服务器连接池初始化失败*/
    FILE_FASTDFS_POOL_INIT_FAILED(3025, "文件服务器连接池初始化失败");

    public int code;

    public String message;

    FastDFSErrorCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
    	return this.code;
    }

    public String getMessage() {
    	return this.message;
    }
}
