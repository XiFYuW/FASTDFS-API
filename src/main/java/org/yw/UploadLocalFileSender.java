package org.yw;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import org.csource.fastdfs.UploadCallback;

public class UploadLocalFileSender implements UploadCallback {

	private MappedBiggerFileReader reader;

	public UploadLocalFileSender(InputStream fis, FileChannel fileChannel, long fileSize, int arraySize)
			throws IOException {
		reader = new MappedBiggerFileReader(fis, fileChannel, fileSize, arraySize);
	}

	@Override
	public int send(OutputStream outputstream) throws IOException {
		// TODO Auto-generated method stub
		int readBytes;
		try {
			while (reader.read() != -1) {
				while ((readBytes = reader.getFileIn().read(reader.getArray())) != -1) {
					outputstream.write(reader.getArray(), 0, readBytes);
				}
			}
			outputstream.flush();
		} finally {
			reader.close();
		}
		return 0;
	}

}
