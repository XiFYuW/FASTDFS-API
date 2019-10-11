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

/**
 * TrackerServer 对象池
 */
public class TrackerServerPool {

    private static Logger logger = LoggerFactory.getLogger(TrackerServerPool.class);

    private static ThreadLocal<TrackerServer> trackerServer = new ThreadLocal<>();

    /**
     * TrackerServer 对象池.
     * GenericObjectPool 没有无参构造
     */
    private static GenericObjectPool<TrackerServer> trackerServerPool;

    private TrackerServerPool(){}

    public static synchronized void getObjectPool(FastDFSBean fastDFSBean){
        if(trackerServerPool == null){
            try {
                // 加载配置文件
                ClientGlobal.initByProperties(fastDFSBean.getConfigPath());
            } catch (IOException | MyException e) {
                e.printStackTrace();
            }

            if(logger.isDebugEnabled()){
                logger.debug("ClientGlobal configInfo: {}", ClientGlobal.configInfo());
            }

            // Pool配置
            GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
            poolConfig.setMinIdle(fastDFSBean.getMinIdle());
            if(fastDFSBean.getMaxStorageConnection() > 0){
                poolConfig.setMaxTotal(fastDFSBean.getMaxStorageConnection());
            }

            trackerServerPool = new GenericObjectPool<>(new TrackerServerFactory(), poolConfig);
        }
    }

    /**
     * 获取 TrackerServer
     * @return TrackerServer
     * @throws FastDFSException
     */
    public static void borrowObject() throws FastDFSException {
        checkTrackerServerPoolInit();
        try {
            trackerServer.set(trackerServerPool.borrowObject());
        } catch (Exception e) {
            e.printStackTrace();
            throw new FastDFSException(FastDFSErrorCode.FILE_SERVER_CONNECTION_FAILED.getCode(), FastDFSErrorCode.FILE_SERVER_CONNECTION_FAILED.getMessage());
        }
    }

    /**
     * 回收 TrackerServer
     */
    public static void returnObject() throws FastDFSException{
        checkTrackerServerPoolInit();
        trackerServerPool.returnObject(trackerServer.get());
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

    private static void checkTrackerServerPoolInit() throws FastDFSException{
        if(null == trackerServerPool) {
            throw new FastDFSException(FastDFSErrorCode.FILE_FASTDFS_POOL_INIT_FAILED.getCode(), FastDFSErrorCode.FILE_FASTDFS_POOL_INIT_FAILED.getMessage());
        }
    }
}
