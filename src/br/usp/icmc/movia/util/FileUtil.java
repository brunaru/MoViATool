/*
 * Copyright 2013 Bruna C. Rodrigues da Cunha
 * 
 * This file is part of MoViA Tool.
 * 
 * MoViA Tool is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * MoViA Tool is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MoViA Tool.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.usp.icmc.movia.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class FileUtil {

	public static String getPath(Context context, Uri uri) throws URISyntaxException {
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;
			cursor = context.getContentResolver().query(uri, projection, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow("_data");
			if (cursor.moveToFirst()) {
				return cursor.getString(column_index);
			}
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}
		return null;
	}
	
	public static Boolean copyFile(String ori, String dest) {
		InputStream inStream = null;
		OutputStream outStream = null;
		try {
			File oriFile = new File(ori);
			File destFile = new File(dest);
			inStream = new FileInputStream(oriFile);
			outStream = new FileOutputStream(destFile);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, length);
			}
			inStream.close();
			outStream.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static String getFileNameWithExtension(String path) {
		int fileNameFirstIndex = path.lastIndexOf(File.separator);
		String fullName = path.substring(fileNameFirstIndex+1);
		return fullName;
	}
	
	public static Boolean fileExists(String path) {
		File file = new File(path);
		if (file.exists()) {
			return true;
		}
		return false;
	}

}
