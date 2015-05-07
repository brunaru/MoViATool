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
package br.usp.icmc.movia;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import br.usp.icmc.movia.util.ConstantsUtil;
import br.usp.icmc.movia.video.recorder.CameraActivity;

/**
 * @author Olibas
 * @author Brunaru
 * Screen used to set the preferences of the video by the user and to record the video
 */
public class RecordVideoActivity extends Activity{

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_recording_preferences);
		
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
		
		EditText fName = (EditText) findViewById(R.id.videoFileNameEditText);
		
		fName.setText("Video_"+timeStamp);
		
		Button butOk = (Button) findViewById(R.id.recordVideoButOk);
		butOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RadioButton highQualityButt = (RadioButton) findViewById(R.id.highQualityRadio);
				EditText videoFileName = (EditText) findViewById(R.id.videoFileNameEditText);								
				if (highQualityButt.isChecked()){
					recordVideo(videoFileName.getText().toString(), ConstantsUtil.VIDEO_HIGH_QUALITY);
				}
				else{
					recordVideo(videoFileName.getText().toString(), ConstantsUtil.VIDEO_LOW_QUALITY);
				}
			}
		});
	}
	

	/**
	 * requests the camera to be turned on and starts to record the video with the preferences edited by the user
	 * @param videoQuality - 0: low quality; 1: high quality
	 * @param videoFileName - the name of the video file
	 */
	public void recordVideo(String videoFileName, int quality){
		Intent cameraIntent = new Intent(this, CameraActivity.class);
		cameraIntent.putExtra(ConstantsUtil.NEW_VIDEO_FILE, videoFileName);
		cameraIntent.putExtra(ConstantsUtil.NEW_VIDEO_QUALITY, quality);
		cameraIntent.putExtra(ConstantsUtil.USER_VAR, getIntent().getStringExtra(ConstantsUtil.USER_VAR));
	    startActivityForResult(cameraIntent, ConstantsUtil.CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
	}
	

	/** This method is called when the startActivityForResult() method is finished (after the intent is finished) **/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ConstantsUtil.CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE){
			
			if (resultCode == RESULT_OK) {
				
				
				Toast.makeText(this, getString(R.string.savedTo) + 
						data.getStringExtra(ConstantsUtil.FULL_NEW_VIDEO_FILE), 
						Toast.LENGTH_LONG).show();
			}
            
            Intent intent = getIntent();
            Intent videoChoiceIntent = new Intent(this, ChooseVideoActivity.class);
            videoChoiceIntent.putExtra(ConstantsUtil.USER_VAR, intent.getStringExtra(ConstantsUtil.USER_VAR));
            startActivityForResult(videoChoiceIntent, ConstantsUtil.VIDEO_CHOOSER_REQUEST_CODE);
		}
	}
}
