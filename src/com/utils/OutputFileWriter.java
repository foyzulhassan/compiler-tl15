/*
 * Foyzul Hassan
 * Class to write data into output files
 */

package com.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import com.entities.TokenInfo;

public class OutputFileWriter {
	public static String WriteTokenFile(String filepath, String filename, List<TokenInfo> tokenlist)
			throws IOException {
		String filewithpath = filepath + File.separator + filename + ".tok";

		File file = new File(filewithpath);

		if (!file.exists())
			file.createNewFile();

		FileOutputStream fos = new FileOutputStream(filewithpath);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

		for (int i = 0; i < tokenlist.size(); i++) {
			TokenInfo tokeninfo = tokenlist.get(i);
			String line = tokeninfo.getWord().toString();

			if (tokeninfo.getWordValue().length() > 0)
				line += "(" + tokeninfo.getWordValue() + ")";

			bw.write(line);
			bw.write("\n");
		}

		bw.close();

		return filewithpath;
	}

	public static String WriteTokenError(String filepath, String filename, List<String> tokenlist) throws IOException {
		String filewithpath = filepath + File.separator + filename + ".tok";

		File file = new File(filewithpath);

		if (!file.exists())
			file.createNewFile();

		FileOutputStream fos = new FileOutputStream(filewithpath);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

		for (int i = 0; i < tokenlist.size(); i++) {
			String error = tokenlist.get(i);

			bw.write(error);
			bw.write("\n");
		}

		bw.close();

		return filewithpath;
	}

	public static String writToFile(String pContent, String filepath, String filename, String extension)
			throws IOException {
		FileOutputStream fop = null;
		File file;

		String filewithpath = filepath + File.separator + filename + extension;

		file = new File(filewithpath);

		if (!file.exists())
			file.createNewFile();

		try {

			fop = new FileOutputStream(file);

			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fout = new FileOutputStream(file.getAbsolutePath());

			byte[] contentInBytes = pContent.getBytes();

			fop.write(contentInBytes);

			fop.flush();
			fop.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return filewithpath;
	}
}
