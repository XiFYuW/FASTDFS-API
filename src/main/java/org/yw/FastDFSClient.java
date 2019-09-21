package org.yw;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.ProtoCommon;
import org.csource.fastdfs.StorageClient1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileChannel;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FastDFS Java API. 文件上传下载主类.
 */
public class FastDFSClient {
	/**
	 * ContentType
	 */
	public static final Map<String, String> EXT_MAPS = new HashMap<>();

	/**
	 * org.slf4j.Logger
	 */
	private static Logger logger = LoggerFactory.getLogger(FastDFSClient.class);

	/**
	 * 文件名称Key
	 */
	private static final String FILENAME = "FASTDFS_FILENAME";
	/**
	 * 文件名后缀
	 */
	private static final String FILESUFFIX = "FASTDFS_SUFFIX";
	/**
	 * 文件最大的大小
	 */
	private static int maxFileSize = Integer.valueOf(TrackerServerPool.getValue("max_file_size"));

	private void initFileFormat() {
		// image
		EXT_MAPS.put("png", "image/png");
		EXT_MAPS.put("gif", "image/gif");
		EXT_MAPS.put("bmp", "image/bmp");
		EXT_MAPS.put("ico", "image/x-ico");
		EXT_MAPS.put("jpeg", "image/jpeg");
		EXT_MAPS.put("jpg", "image/jpeg");
		// 压缩文件
		EXT_MAPS.put("zip", "application/zip");
		EXT_MAPS.put("rar", "application/x-rar");
		// doc
		EXT_MAPS.put("pdf", "application/pdf");
		EXT_MAPS.put("ppt", "application/vnd.ms-powerpoint");
		EXT_MAPS.put("xls", "application/vnd.ms-excel");
		EXT_MAPS.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		EXT_MAPS.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
		EXT_MAPS.put("doc", "application/msword");
		EXT_MAPS.put("doc", "application/wps-office.doc");
		EXT_MAPS.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		EXT_MAPS.put("txt", "text/plain");
		// 音频
		EXT_MAPS.put("mp4", "video/mp4");
		EXT_MAPS.put("flv", "video/x-flv");
	}

	public FastDFSClient() {
		initFileFormat();
	}

	public FastDFSResponse uploadTo(InputStream in, String filePath, Map<String, Object> descriptions) {
		FastDFSResponse fastDFSResponse = new FastDFSResponse();
		String path;
		try {
			path = upload(in,filePath,descriptions);
		} catch (FastDFSException e) {
			fastDFSResponse.setSuccess(false);
			fastDFSResponse.setCode(e.getCode());
			fastDFSResponse.setMessage(e.getMessage());
			return fastDFSResponse;
		}
		String token = getToken(path);
		descriptions = getFileDescriptions(path);
		fastDFSResponse.setDescriptions(descriptions);
		fastDFSResponse.setToken(token);
		fastDFSResponse.setHttpUrl(TrackerServerPool.fileServerAdder + "/" + path + "?" + token);
		fastDFSResponse.setFilePath(path);
		fastDFSResponse.setFileName(String.valueOf(descriptions.get("FILENAME")));
		fastDFSResponse.setFileType(String.valueOf(descriptions.get("FILESUFFIX")));
		return  fastDFSResponse;
	}
	/**
	 * 上传通用方法
	 *
	 * @param in
	 *            文件输入流
	 * @param filePath
	 *            文件路径
	 * @param descriptions
	 *            文件描述信息
	 * @return 组名+文件路径，如：group1/M00/00/00/wKgz6lnduTeAMdrcAAEoRmXZPp870.jpeg
	 * @throws FastDFSException
	 * @throws Exception
	 */
	public String upload(InputStream in, String filePath, Map<String, Object> descriptions) throws FastDFSException {
		if (null == in) {
			throw new FastDFSException(FastDFSErrorCode.FILE_ISNULL.getCode(), FastDFSErrorCode.FILE_ISNULL.getMessage());
		}
		if(!new File(filePath).exists()){
			throw new FastDFSException(FastDFSErrorCode.FILE_NOT_EXIST.getCode(), FastDFSErrorCode.FILE_NOT_EXIST.getMessage());
		}
		if(!FastDFSUtil.isNotBlank(filePath)){
			throw new FastDFSException(FastDFSErrorCode.FILE_LOCAL_PATH_ISNULL.getCode(), FastDFSErrorCode.FILE_LOCAL_PATH_ISNULL.getMessage());
		}
		filePath = FastDFSUtil.toLocal(filePath);
		String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
		// 返回路径
		String path = null;
		// 文件描述
		NameValuePair[] nvps = null;
		List<NameValuePair> nvpsList = new ArrayList<>();

		// 文件名
		if (FastDFSUtil.isNotBlank(fileName)) {
			nvpsList.add(new NameValuePair(FILENAME, fileName));
		}
		// 文件名后缀
		String suffix = FastDFSUtil.getFilenameSuffix(fileName);
		nvpsList.add(new NameValuePair(FILESUFFIX, suffix));
		// 描述信息
		if (descriptions != null && descriptions.size() > 0) {
			descriptions.forEach((key, value) -> nvpsList.add(new NameValuePair(key, String.valueOf(value))));
		}
		// List 转 Array
		if (nvpsList.size() > 0) {
			nvps = new NameValuePair[nvpsList.size()];
			nvpsList.toArray(nvps);
		}

		StorageClient1 storageClient = TrackerServerPool.getStorageClient1();
		FileChannel fileChannel = ((FileInputStream) in).getChannel();
		try {
			long fileSize = fileChannel.size();
			if (fileSize > maxFileSize) {
					path = storageClient.upload_appender_file1(null, fileSize,
							new UploadLocalFileSender(in, fileChannel, fileSize, 256 * 1024), suffix, nvps);
			} else {
				byte[] fileBuff = new byte[in.available()];
				in.read(fileBuff, 0, fileBuff.length);
				path = storageClient.upload_file1(fileBuff, suffix, nvps);
			}
			if (!FastDFSUtil.isNotBlank(path)) {
				throw new FastDFSException(FastDFSErrorCode.FILE_UPLOAD_FAILED.getCode(),
						FastDFSErrorCode.FILE_UPLOAD_FAILED.getMessage());
			}
		} catch (IOException | MyException e) {
			e.printStackTrace();
			throw new FastDFSException(FastDFSErrorCode.FILE_FAILED.getCode(),
					e.getMessage());
		}finally {
			// 返还对象
			TrackerServerPool.returnObject();
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("文件上传成功 {}", path);
		}
		return path;
	}

	/**
	 * 下载文件
	 *
	 * @param filePath
	 *            fastdfs文件路径
	 * @param outPath
	 *            本地路径
	 */
	public void download(String filePath, String outPath)
			throws FastDFSException {
		if (!FastDFSUtil.isNotBlank(filePath)) {
			throw new FastDFSException(FastDFSErrorCode.FILE_FASTDFS_PATH_ISNULL.getCode(), FastDFSErrorCode.FILE_FASTDFS_PATH_ISNULL.getMessage());
		}
		if (!FastDFSUtil.isNotBlank(outPath)) {
			throw new FastDFSException(FastDFSErrorCode.FILE_LOCAL_PATH_ISNULL.getCode(), FastDFSErrorCode.FILE_LOCAL_PATH_ISNULL.getMessage());
		}
		Map<String, Object> descriptions = getFileDescriptions(filePath);
		String fileName = String.valueOf(descriptions.get(FILENAME));
		if (!outPath.endsWith(File.separator)) {
			outPath += File.separator + fileName;
		}else{
			outPath += fileName;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("从FASTDFS {} 下载至 {}", filePath, outPath);
		}
		StorageClient1 storageClient = TrackerServerPool.getStorageClient1();
		OutputStream os = null;
		try {
			os = new FileOutputStream(outPath);
			int errno = storageClient.download_file1(filePath, new DownloadFileWriter(os));
	        if (errno != 0) {
	        	throw new FastDFSException(FastDFSErrorCode.FILE_DOWNLOAD_FAILED.getCode(),
						FastDFSErrorCode.FILE_DOWNLOAD_FAILED.getMessage());
	        }
		} catch (MyException | IOException e) {
			e.printStackTrace();
			throw new FastDFSException(FastDFSErrorCode.FILE_FAILED.getCode(),
					e.getMessage());
		} finally {
			TrackerServerPool.returnObject();
			try {
				if (os != null) {
					os.flush();
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("文件下载成功 {}", filePath);
		}
	}

	/**
	 * 删除文件
	 *
	 * @param filePath
	 *            文件路径
	 * @return 删除成功返回 0, 失败返回其它
	 */
	public int delete(String filePath) throws FastDFSException {
		if (!FastDFSUtil.isNotBlank(filePath)) {
			throw new FastDFSException(FastDFSErrorCode.FILE_FASTDFS_PATH_ISNULL.getCode(), FastDFSErrorCode.FILE_FASTDFS_PATH_ISNULL.getMessage());
		}
		StorageClient1 storageClient = TrackerServerPool.getStorageClient1();
		int success = 0;
		try {
			success = storageClient.delete_file1(filePath);
			if (success != 0) {
				throw new FastDFSException(FastDFSErrorCode.FILE_DELETE_FAILED.getCode(),
						FastDFSErrorCode.FILE_DELETE_FAILED.getMessage());
			}
		} catch (IOException | MyException e) {
			e.printStackTrace();
			throw new FastDFSException(FastDFSErrorCode.FILE_FASTDDFS_ERR.getCode(),
					FastDFSErrorCode.FILE_FASTDDFS_ERR.getMessage());
		}finally {
			TrackerServerPool.returnObject();
		}
		return success;
	}

	/**
	 * 获取文件描述信息
	 *
	 * @param filepath
	 *            文件路径
	 * @return 文件描述信息
	 */
	public Map<String, Object> getFileDescriptions(String filepath) {
		NameValuePair[] nvps;
		StorageClient1 storageClient;
		FileInfo fileInfo;
		try {
			storageClient = TrackerServerPool.getStorageClient1();
			nvps = storageClient.get_metadata1(filepath);
			fileInfo = storageClient.get_file_info1(filepath);
		} catch (IOException | MyException | FastDFSException e) {
			e.printStackTrace();
			return null;
		}finally {
			TrackerServerPool.returnObject();
		}
		Map<String, Object> infoMap = new HashMap<>();
		if (nvps != null && nvps.length > 0) {
			for (NameValuePair nvp : nvps) {
				infoMap.put(nvp.getName(), nvp.getValue());
			}
		}
		//源IP
		infoMap.put("SourceIpAddr", fileInfo.getSourceIpAddr());
		//文件大小
		infoMap.put("FileSize", fileInfo.getFileSize());
		//创建时间
		infoMap.put("CreateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fileInfo.getCreateTimestamp()));
		//签名
		infoMap.put("CRC32", fileInfo.getCrc32());
		return infoMap;
	}

	/**
	 * 获取访问服务器的token，拼接到地址后面
	 *
	 * @param filepath
	 *            文件路径 group1/M00/00/00/wKgzgFnkTPyAIAUGAAEoRmXZPp876.jpeg
	 * @return 返回token，如： token=078d370098b03e9020b82c829c205e1f&ts=1508141521
	 */
	public static String getToken(String filepath) {
		int ts = (int) Instant.now().getEpochSecond();
		String token = "null";
		try {
			token = ProtoCommon.getToken(FastDFSUtil.getFilename(filepath), ts, TrackerServerPool.httpSecretKey);
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException | MyException e) {
			e.printStackTrace();
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("token=").append(token);
		sb.append("&ts=").append(ts);
		return sb.toString();
	}
}
