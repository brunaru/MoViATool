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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.location.Geocoder;
import android.location.LocationManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import br.usp.icmc.movia.R;
import br.usp.icmc.movia.annotation.AnnotationContainer;
import br.usp.icmc.movia.annotation.operators.AuthoringOperators;
import br.usp.icmc.movia.util.ConstantsUtil;

public class CameraActivity extends Activity {
	private static final String TAG = "CameraActivity";
	private Camera mCamera;
	private CameraPreview mPreview;
	private MediaRecorder mMediaRecorder;
	private boolean isRecording = false;
	private long startTime;
	private String videoName;
	private String user;
	private int mark;
	private boolean recapture;
	
	private Button markButton, finishButton, captureButton;
	
	private String notesPath = ConstantsUtil.NOTES_FULL_PATH;	
	private AuthoringOperators authoringOperators;
	private AnnotationContainer container;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.camera_view);
		
		markButton = (Button) findViewById(R.id.button_mark);
		finishButton = (Button) findViewById(R.id.button_finish);
		markButton.setVisibility(View.INVISIBLE);
		finishButton.setVisibility(View.INVISIBLE);		
				
		
		mCamera = getCameraInstance();
		mPreview = new CameraPreview(this, mCamera);
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mPreview);
		
		mark = 0;
		startTime = 0;
		recapture = false;
		user = getIntent().getStringExtra(ConstantsUtil.USER_VAR);
		videoName = getIntent().getStringExtra(ConstantsUtil.NEW_VIDEO_FILE);
		
		authoringOperators = new AuthoringOperators();
		List<String> authors = new ArrayList<String>();
		authors.add(user);
		container = new AnnotationContainer(authors);

		// Add a listener to the Capture button
		captureButton = (Button) findViewById(R.id.button_capture);
		captureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isRecording) {
					// stop recording and release camera
					mMediaRecorder.stop(); // stop the recording
					releaseMediaRecorder(); // release the MediaRecorder object
					mCamera.lock(); // take camera access back from MediaRecorder
					// inform the user that recording has stopped
					captureButton.setText(R.string.recapture);
					recapture = true;
					isRecording = false;
					mark = 0;
					markButton.setVisibility(View.INVISIBLE);
					finishButton.setVisibility(View.VISIBLE);
				} else {
					if (recapture) {
						final AlertDialog alertDialog = new AlertDialog.Builder(CameraActivity.this).create();
						alertDialog.setTitle(R.string.overwriteVideo);
						alertDialog.setMessage(getResources().getString(R.string.confirmOverwriteVideo));
						alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								record();
							}
						});
						alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								alertDialog.dismiss();
							}
						});
						alertDialog.show();
					} else {
						record();
					}
				}
			}
		});
		
		finishButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Tem certeza
				if (isRecording) {
					mMediaRecorder.stop();
					releaseMediaRecorder();
					mCamera.lock();
					mCamera.stopPreview();
					mCamera.release();
				}
				setResult(RESULT_OK, getIntent());
			    finish();
			}
		});
		
		markButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isRecording) {
					long mark = new Date().getTime() - startTime;
					int time = (int) (mark / 1000);
					addMark(time);
				}
			}
		});

	}
	
	/**
	 * Grava video.
	 */
	private void record() {
		// initialize video camera
		if (prepareVideoRecorder()) {
			/* Camera is available and unlocked, MediaRecorder is prepared, now you can start recording. */
			mMediaRecorder.start();
			captureButton.setText(R.string.stop);
			isRecording = true;
			if (recapture) {
				authoringOperators.deleteTextAnnotationFile(notesPath, videoName, user, container);
			}
			markButton.setVisibility(View.VISIBLE);
			finishButton.setVisibility(View.INVISIBLE);
			startTime = new Date().getTime();
		} else {
			// prepare didn't work, release the camera
			releaseMediaRecorder();
			Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Helper method to access the camera returns null if it cannot get the
	 * camera or does not exist
	 * 
	 * @return
	 */
	private Camera getCameraInstance() {
		Camera camera = null;
		try {
			camera = Camera.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return camera;
	}
	
	/**
	 * Adiciona marcacao ao arquivo de anotacao.
	 * 
	 * @param time tempo em segundos.
	 */
	private void addMark(int time) {
		if (!authoringOperators.textAnnotationExist(notesPath, videoName, user)) {
			authoringOperators.setTextAnnotationsAuthor(user,
					(LocationManager) getSystemService(LOCATION_SERVICE),
					new Geocoder(this), "");
		}
		mark++; // utilizado para contar marcacoes
		authoringOperators.storeTextAnnotation(
				getResources().getString(R.string.marking) + mark, notesPath,
				videoName, user, time, container);
		Toast.makeText(this.getApplicationContext(), R.string.markOk, Toast.LENGTH_SHORT).show();
	}

	private boolean prepareVideoRecorder() {
		
		mMediaRecorder = new MediaRecorder();

		// Step 1: Unlock and set camera to MediaRecorder
		mCamera.unlock();
		mMediaRecorder.setCamera(mCamera);

		// Step 2: Set sources
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

		// Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
		int quality = getIntent().getIntExtra(ConstantsUtil.NEW_VIDEO_QUALITY, 
				ConstantsUtil.VIDEO_HIGH_QUALITY);
		if (quality == ConstantsUtil.VIDEO_HIGH_QUALITY) {
			mMediaRecorder.setProfile(CamcorderProfile
					.get(CamcorderProfile.QUALITY_HIGH));
		} else {
			mMediaRecorder.setProfile(CamcorderProfile
					.get(CamcorderProfile.QUALITY_LOW));
		}
		

		// Step 4: Set output file
		String videoPath = OutputMedia.getOutputMediaFile(videoName);
		mMediaRecorder.setOutputFile(videoPath);

		// Step 5: Set the preview output
		mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

		// Step 6: Prepare configured MediaRecorder
		try {
			mMediaRecorder.prepare();
		} catch (IllegalStateException e) {
			Log.d(TAG,
					"IllegalStateException preparing MediaRecorder: "
							+ e.getMessage());
			releaseMediaRecorder();
			return false;
		} catch (IOException e) {
			Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
			releaseMediaRecorder();
			return false;
		}
		getIntent().putExtra(ConstantsUtil.FULL_NEW_VIDEO_FILE, videoPath);
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseMediaRecorder(); // if you are using MediaRecorder, release it
								// first
		releaseCamera(); // release the camera immediately on pause event
	}

	private void releaseMediaRecorder() {
		if (mMediaRecorder != null) {
			mMediaRecorder.reset(); // clear recorder configuration
			mMediaRecorder.release(); // release the recorder object
			mMediaRecorder = null;
			mCamera.lock(); // lock camera for later use
		}
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}

}
