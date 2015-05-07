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

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import br.usp.icmc.movia.util.ConstantsUtil;
import br.usp.icmc.movia.util.FileUtil;
import br.usp.icmc.movia.util.Util;

import br.usp.icmc.movia.R;

/**
 * Tela inicial da aplicacao.
 * @author Brunaru
 *
 */
public class MainActivity extends Activity {
	
	private static final int FILE_SELECT_CODE = 0;
	
	private static final String TAG = "FILE_CHOOSER";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		
		Button bAnnotate = (Button) findViewById(R.id.bAnnotate);
		bAnnotate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				start(ConstantsUtil.ANNOTATE_OLD);
			}
		});
		
		Button bRecord = (Button) findViewById(R.id.bRecordAndAnnotate);
		bRecord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				start(ConstantsUtil.ANNOTATE_NEW);
			}
		});
		
		Button bImport = (Button) findViewById(R.id.bImport);
		bImport.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				showFileChooser();
			}
		});
	}
	
	private void start(int type) {
		final Intent chooseUserIntent = new Intent(this, ChooseUserActivity.class);
		chooseUserIntent.putExtra(ConstantsUtil.ANNOTATION_TYPE, type);
		startActivityForResult(chooseUserIntent, ConstantsUtil.USER_CHOOSER_REQUEST_CODE);
	}
	
	private void showFileChooser() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("text/xml");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.selectFileToImport)), FILE_SELECT_CODE);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast toast = Util.createToast(getApplicationContext(), getResources().getString(R.string.installFM), Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == FILE_SELECT_CODE) {
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				Log.d(TAG, "File Uri: " + uri.toString());
				String path = "";
				try {
					path = FileUtil.getPath(this, uri);
					if (path.endsWith(".xml")) {
						String dest = ConstantsUtil.VIDEOS_DIR_PATH + ConstantsUtil.NOTES_PATH + File.separator + FileUtil.getFileNameWithExtension(path);
						if (FileUtil.fileExists(dest)) {
							confirmDialog(path, dest);
						} else {
							copyFile(path, dest);
						}
					} else {
						Toast toast = Util.createToast(getApplicationContext(), getResources().getString(R.string.xmlOnly), Toast.LENGTH_LONG);
						toast.show();
					}
					Log.i(TAG, path);
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}
				Log.d(TAG, "File Path: " + path);
			}			
		}
	}

	private void confirmDialog(final String path, final String dest) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("File already exists");
		alertDialogBuilder.setMessage("Do you want to overwrite the file?").setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						copyFile(path, dest);
						dialog.cancel();
					}
				}).setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
	
	private void copyFile (String path, String dest) {
		Boolean success = FileUtil.copyFile(path, dest);
		if (success) {
			Toast toast = Util.createToast(getApplicationContext(), getResources().getString(R.string.copySuccess), Toast.LENGTH_LONG);
			toast.show();
		} else {
			Toast toast = Util.createToast(getApplicationContext(), getResources().getString(R.string.error), Toast.LENGTH_LONG);
			toast.show();
		}
	}
}
