package org.yw;

import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedBiggerFileReader {
	private MappedByteBuffer[] mappedBufArray;
	private int count = 0;
	private int number;//内存数量
	private InputStream fileIn;
	private long fileLength;
	private int arraySize;
	private byte[] array;

	public MappedBiggerFileReader(InputStream fi, FileChannel fileChannel, long fileSize, int arraySize)
			throws IOException {
		this.fileIn = fi;
		this.fileLength = fileSize;
		this.number = (int) Math.ceil((double) fileLength / (double) Integer.MAX_VALUE);
		this.mappedBufArray = new MappedByteBuffer[number];// 内存文件映射数组
		long preLength = 0;
		long regionSize = (long) Integer.MAX_VALUE;// 映射区域的大小 2g
		for (int i = 0; i < number; i++) {// 将文件的连续区域映射到内存文件映射数组中
			if (fileLength - preLength < (long) Integer.MAX_VALUE) {
				regionSize = fileLength - preLength;// 最后一片区域的大小
			}
			mappedBufArray[i] = fileChannel.map(FileChannel.MapMode.READ_ONLY, preLength, regionSize);
			preLength += regionSize;// 下一片区域的开始
		}
		this.arraySize = arraySize;
	}

	public int read() {
		if (count >= number) {
			return -1;
		}
		int limit = mappedBufArray[count].limit();//max
		int position = mappedBufArray[count].position();
		if (limit - position > arraySize) {
			array = new byte[arraySize];
			mappedBufArray[count].get(array);
			return arraySize;
		} else {// 本内存文件映射最后一次读取数据
			array = new byte[limit - position];
			mappedBufArray[count].get(array);
			if (count < number) {
				count++;// 转换到下一个内存文件映射
			}
			return limit - position;
		}
	}

	public void close() throws IOException {
		if (fileIn != null) {
			fileIn.close();
		}
		array = null;
	}

	public byte[] getArray() {
		return array;
	}

	public int getArraySize() {
		return arraySize;
	}

	public long getFileLength() {
		return fileLength;
	}

	public InputStream getFileIn() {
		return fileIn;
	}
}
