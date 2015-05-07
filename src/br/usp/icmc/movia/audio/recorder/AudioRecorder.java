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
package br.usp.icmc.movia.audio.recorder;

import java.io.File;
import java.io.IOException;

import br.usp.icmc.movia.util.ConstantsUtil;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;

public class AudioRecorder {

	private static final String LOG_TAG = "AudioRecordTest";
	
	private String filePath;

	private MediaRecorder mRecorder = null;

	private MediaPlayer mPlayer = null;

	public AudioRecorder(String filePath) {
		this.filePath = filePath;
		File test = new File(filePath);
		if (!test.exists()) {
			test.mkdirs();
		}
	}

	public void onRecord(boolean start, String fileName) {
		if (start) {
			startRecording(fileName);
		} else {
			stopRecording();
		}
	}

	public void onPlay(boolean start, String fileName) {
		if (start) {
			startPlaying(fileName);
		} else {
			stopPlaying();
		}
	}

	public int startPlaying(String fileName) {
		if (isPlaying()) {
			stopPlaying();
		}
		mPlayer = new MediaPlayer();
		try {
			String fullName = filePath + fileName + "." + ConstantsUtil.AUDIO_FORMAT;
			File file = new File(fullName);
			if (file.exists()) {
				mPlayer.setDataSource(fullName);
				mPlayer.prepare();
				mPlayer.start();
				return mPlayer.getDuration();
			}			
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}
		return 0;
	}
	
	public boolean isPlaying() {
		if (mPlayer == null) {
			return Boolean.FALSE;
		}
		return mPlayer.isPlaying();
	}

	public void stopPlaying() {
		if (mPlayer == null) {
			return;
		}
		mPlayer.release();
		mPlayer = null;
	}

	private void startRecording(String fileName) {
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(filePath + fileName + "." + ConstantsUtil.AUDIO_FORMAT);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		try {
			mRecorder.prepare();
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}

		mRecorder.start();
	}

	public void stopRecording() {
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
	}

	public void onPause() {
		if (mRecorder != null) {
			mRecorder.release();
			mRecorder = null;
		}

		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}
	
	public String getFullFileName(String fileName) {
		String fullFileName = filePath + fileName + "." + ConstantsUtil.AUDIO_FORMAT;
		return fullFileName;
	}
	
}
