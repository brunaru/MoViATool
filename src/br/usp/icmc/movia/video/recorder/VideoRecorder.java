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
package br.usp.icmc.movia.video.recorder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Log;
import br.usp.icmc.movia.util.ConstantsUtil;

/**
 * @author Olibas
 * This class id responsible for recording the video with the information.
 * edited by brunaru
 */
public class VideoRecorder {
	
	/**
	 * Create a file Uri for saving an image or video.
	 * 
	 * @param type
	 *            - audio/ video
	 * @return the Uri object containing all important information the app will
	 *         need to annotate on the video recorded
	 */
	public static Uri getOutputMediaFileUri(int type, String fileName){
	      return Uri.fromFile(getOutputMediaFile(type,fileName));
	}

	/**
	 * Create a File for saving an image or video.
	 * @param type
	 * @return
	 */
	@SuppressLint("SimpleDateFormat") 
	public static File getOutputMediaFile(int type, String fileName){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(ConstantsUtil.VIDEOS_DIR_PATH, "MOVIA");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MOVIA Video recording", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		if (type == ConstantsUtil.MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else if (type == ConstantsUtil.MEDIA_TYPE_VIDEO) {
			if (new File(mediaStorageDir.getPath() + File.separator + fileName
					+ ".mp4").exists()) { // file with this name already exists
											// in the repository -->
											// Concatenates timestamp to avoid
											// duplicating file.
				mediaFile = new File(mediaStorageDir.getPath() + File.separator
						+ fileName + timeStamp + ".mp4");
			} else {
				mediaFile = new File(mediaStorageDir.getPath() + File.separator
						+ fileName + ".mp4");
			}
		} else {
			return null;
		}

	    return mediaFile;
	}
}
