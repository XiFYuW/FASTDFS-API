package org.yw;

import org.csource.fastdfs.DownloadCallback;

import java.io.IOException;
import java.io.OutputStream;

public class DownloadFileWriter implements DownloadCallback {
	private OutputStream out = null;
	private long current_bytes = 0;

	public DownloadFileWriter(OutputStream out) {
		this.out = out;
	}

	@Override
	public int recv(long file_size, byte[] data, int bytes) {
		try {
			this.out.write(data, 0, bytes);
			this.current_bytes += bytes;

			if (this.current_bytes == file_size) {
				this.out.close();
				this.out = null;
				this.current_bytes = 0;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			return -1;
		}

		return 0;
	}

	@Override
	protected void finalize() throws Throwable {
		if (this.out != null) {
			this.out.close();
			this.out = null;
		}
	}
}
