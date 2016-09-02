package com.us.launcher.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamTools {

	/**
	 * @return
	 */
	public static String readStream(InputStream is){
		String result = "";

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			int len;
			while ((len = is.read(buffer)) != -1) {
					baos.write(buffer,0,len);
			}
			result = baos.toString();
			result = new String(baos.toByteArray());
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
