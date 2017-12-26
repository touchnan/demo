package cn.touch.demo.hadoop.hdfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class FileCat {
	public static void main(String[] args) throws Exception {
		readDirectoryFile(args);
		// readFile(args);
	}

	public static void readDirectoryFile(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Usage FileCat <target>");
			System.exit(1);
		}
		Configuration conf = new Configuration();
		FileSystem hdfs = FileSystem.get(URI.create(args[0]), conf);
		FileStatus[] status = hdfs.listStatus(new Path("/app/output1"));
		InputStream in = null;
		for (FileStatus file : status) {
			if (file.getPath().getName().startsWith("part-")) {
				try {
					in = hdfs.open(file.getPath());
					IOUtils.copyBytes(in, System.out, 4096, true);
				} finally {
					IOUtils.closeStream(in);
				}
			}
		}
	}

	public static void readFile(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Usage FileCat <target>");
			System.exit(1);
		}
		Configuration conf = new Configuration();
		FileSystem hdfs = FileSystem.get(URI.create(args[0]), conf);
		InputStream in = null;
		try {
			in = hdfs.open(new Path(args[0] + "/part-r-00000"));
			IOUtils.copyBytes(in, System.out, 4096, true);
		} finally {
			IOUtils.closeStream(in);
		}
	}

	public String getYHDSXCategoryIdStr(String filePath) {
		final String DELIMITER = new String(new byte[] { 1 });
		final String INNER_DELIMITER = ",";

		// 遍历目录下的所有文件
		BufferedReader br = null;
		String yhdsxCategoryIdStr = "";
		try {
			FileSystem fs = FileSystem.get(new Configuration());
			FileStatus[] status = fs.listStatus(new Path(filePath));
			for (FileStatus file : status) {
				if (!file.getPath().getName().startsWith("part-")) {
					continue;
				}

				FSDataInputStream inputStream = fs.open(file.getPath());
				br = new BufferedReader(new InputStreamReader(inputStream));

				String line = null;
				while (null != (line = br.readLine())) {
					String[] strs = line.split(DELIMITER);
					String categoryId = strs[0];
					String categorySearchName = strs[9];
					if (-1 != categorySearchName.indexOf("0-956955")) {
						yhdsxCategoryIdStr += (categoryId + INNER_DELIMITER);
					}
				} // end of while
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return yhdsxCategoryIdStr;
	}
}
