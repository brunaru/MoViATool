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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import br.usp.icmc.movia.util.ConstantsUtil;
import br.usp.icmc.movia.util.DirectoryPicker;
import br.usp.icmc.movia.util.VideoUtil;

import br.usp.icmc.movia.R;

/**
 * Atividade de selecao de video.
 * 
 * @author Brunaru
 * 
 */
public class ChooseVideoActivity extends Activity implements OnItemClickListener {
	
	private ListView listView;

	private VideoArrayAdapter adapterListView;
	
	private Map<String, String> videoFilesMap;
	
	private String videoDir;
	
	private static String VIDEO_DIRECTORY = "videodir";

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_page_video);
		setTitle(R.string.chooseVideo);
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		
		SharedPreferences settings = getSharedPreferences(ConstantsUtil.PREFS_NAME, 0);
		videoDir = settings.getString(VIDEO_DIRECTORY, ConstantsUtil.VIDEOS_DIR_PATH);
		
		TextView tDir = (TextView) findViewById(R.id.tVideoDir);
		tDir.setText(videoDir);
		
		createChooseVideoList();
		
		Button bChooseDir = (Button) findViewById(R.id.bChooseVideoDir);
		bChooseDir.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				showDirChooser();
			}
		});
		
	}
	
	private void showDirChooser() {
		Intent intent = new Intent(this, DirectoryPicker.class);
		startActivityForResult(intent, DirectoryPicker.PICK_DIRECTORY);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == DirectoryPicker.PICK_DIRECTORY && resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			videoDir = (String) extras.get(DirectoryPicker.CHOSEN_DIRECTORY);
			TextView tDir = (TextView) findViewById(R.id.tVideoDir);
			tDir.setText(videoDir);
			createChooseVideoList();
		}
	}
	
	/**
	 * Cria tela de selecao de video.
	 */
	private void createChooseVideoList() {
		try {
			if (videoFilesMap != null) {
				videoFilesMap.clear();
			}
			videoFilesMap = VideoUtil.listVideos(videoDir);
			Map<String, Bitmap> bmpMap = new HashMap<String, Bitmap>();
			List<String> pathNames = new ArrayList<String>();
			if (!videoFilesMap.isEmpty()) {
				for (String videoPath : videoFilesMap.keySet()) {
					String videoName = videoFilesMap.get(videoPath);
					String screensPath = VideoUtil.getDirPath(videoPath) + ConstantsUtil.THUMBNAILS_PATH;
					Bitmap bmp = VideoUtil.getBitmap(videoPath, screensPath, videoName, 50);
					if (bmp != null) {
						pathNames.add(videoPath);
						bmpMap.put(videoPath, bmp);
					}					
				}
				String[] pathArray = pathNames.toArray(new String[pathNames.size()]);			
				adapterListView = new VideoArrayAdapter(this, pathArray, bmpMap);
				adapterListView.notifyDataSetChanged();
				listView.invalidate();
				listView.setAdapter(adapterListView);
				listView.setAdapter(adapterListView);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		String videoPath = adapterListView.getItem(position);
		String videoName = videoFilesMap.get(videoPath);		
		String cleanPath = VideoUtil.getDirPath(videoPath);
		String screensPath = cleanPath + ConstantsUtil.THUMBNAILS_PATH;
		final Intent chooseAuthorIntent = new Intent(this, ChooseAuthorActivity.class);
		chooseAuthorIntent.putExtra(ConstantsUtil.NOTES_PATH_VAR, ConstantsUtil.NOTES_FULL_PATH);
		chooseAuthorIntent.putExtra(ConstantsUtil.VIDEO_NAME_VAR, videoName);
		chooseAuthorIntent.putExtra(ConstantsUtil.SCREENS_PATH_VAR, screensPath);
		chooseAuthorIntent.putExtra(ConstantsUtil.VIDEO_PATH_VAR, videoPath);
		chooseAuthorIntent.putExtra(ConstantsUtil.USER_VAR, getIntent().getStringExtra(ConstantsUtil.USER_VAR));		
        startActivityForResult(chooseAuthorIntent, ConstantsUtil.AUTHOR_CHOOSER_REQUEST_CODE);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		SharedPreferences settings = getSharedPreferences(ConstantsUtil.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(VIDEO_DIRECTORY, videoDir);
		editor.commit();
	}
	
}
