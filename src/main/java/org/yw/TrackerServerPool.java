package org.yw;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * TrackerServer 对象池
 */
public class TrackerServerPool {
    /**
     * org.slf4j.Logger
     */
    private static Logger logger = LoggerFactory.getLogger(TrackerServerPool.class);

    /**
     * TrackerServer 配置文件路径
     */
    private static final String FASTDFS_CONFIG_PATH = "application-fastDFS.properties";

    /**
     * 最大连接数 default 8.
     */
    private static int maxStorageConnection;
    public static String httpSecretKey;
    public static String fileServerAdder;
    public static Properties properties = new Properties();

    private static ThreadLocal<TrackerServer> trackerServer = new ThreadLocal<>();

	static {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try {
			properties.load(new InputStreamReader(classLoader.getResourceAsStream(FASTDFS_CONFIG_PATH), "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		maxStorageConnection = Integer.valueOf(getValue("max_storage_connection"));
        httpSecretKey = getValue("fastdfs.http_secret_key");
        fileServerAdder = getValue("file_server_addr");
	}

    /**
     * TrackerServer 对象池.
     * GenericObjectPool 没有无参构造
     */
    private static GenericObjectPool<TrackerServer> trackerServerPool;

    private TrackerServerPool(){}

    private static synchronized GenericObjectPool<TrackerServer> getObjectPool(){
        if(trackerServerPool == null){
            try {
                // 加载配置文件
                ClientGlobal.initByProperties(FASTDFS_CONFIG_PATH);
            } catch (IOException | MyException e) {
                e.printStackTrace();
            }

            if(logger.isDebugEnabled()){
                logger.debug("ClientGlobal configInfo: {}", ClientGlobal.configInfo());
            }

            // Pool配置
            GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
            poolConfig.setMinIdle(2);
            if(maxStorageConnection > 0){
                poolConfig.setMaxTotal(maxStorageConnection);
            }

            trackerServerPool = new GenericObjectPool<>(new TrackerServerFactory(), poolConfig);
        }
        return trackerServerPool;
    }

    /**
     * 获取 TrackerServer
     * @return TrackerServer
     * @throws FastDFSException
     */
    public static void borrowObject() throws FastDFSException {
        try {
            trackerServer.set(getObjectPool().borrowObject());
        } catch (Exception e) {
            e.printStackTrace();
            throw new FastDFSException(FastDFSErrorCode.FILE_SERVER_CONNECTION_FAILED.getCode(),
                    FastDFSErrorCode.FILE_SERVER_CONNECTION_FAILED.getMessage());
        }
    }

    /**
     * 回收 TrackerServer
     */
    public static void returnObject(){
        getObjectPool().returnObject(trackerServer.get());
    }

	public static String getValue(String name) {
		String value = "";
		try {
			value = properties.getProperty(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

    /**
     *  获取StorageClient1
     * @return
     * @throws FastDFSException
     */
    public static StorageClient1 getStorageClient1() throws FastDFSException {
        TrackerServerPool.borrowObject();
        return new StorageClient1(trackerServer.get(), null);
    }
}
