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
package br.usp.icmc.movia.share;

import java.io.File;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Video;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import br.usp.icmc.movia.R;
import br.usp.icmc.movia.annotation.ink.DrawAnnotationUtil;
import br.usp.icmc.movia.annotation.operators.AuthoringOperators;
import br.usp.icmc.movia.annotation.text.TextAnnotation;
import br.usp.icmc.movia.annotation.text.TextAnnotationUtil;
import br.usp.icmc.movia.converter.iwac.IWaCConverter;
import br.usp.icmc.movia.util.Util;
import br.usp.icmc.movia.util.VideoUtil;

/**
 * Compartilhador de anotacoes.
 * 
 * @author Brunaru
 * 
 */
public class ShareAnnotation {
	
	/**
	 * Compartilha anotacoes textuais atraves de um app escolhida.
	 * 
	 * @param notesPath
	 * @param videoName
	 * @param author
	 * @param context
	 * @param txtUtil
	 */
	public void shareAnnotationViaApps(final String notesPath, final String videoName, final String videoPath, final String author, 
			final Context context, final TextAnnotationUtil txtUtil, final DrawAnnotationUtil inkUtil, final String user, final AuthoringOperators authoringOperators, 
			final long videoDurationMs) {
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.share_annotation_apps);
		dialog.setTitle(R.string.shareAnnotation);
		dialog.setCancelable(Boolean.TRUE);
		TextView tShare = (TextView) dialog.findViewById(R.id.tShare);
		String text = tShare.getText().toString();
		text = text.replace("xxx", author);
		tShare.setText(text);
		Button bOk = (Button) dialog.findViewById(R.id.bShareAnEmOk);
		Button bCancel = (Button) dialog.findViewById(R.id.bShareAnEmCancel);

		final RadioButton radioVideo = (RadioButton) dialog.findViewById(R.id.radioVideo);
		final RadioButton radioText = (RadioButton) dialog.findViewById(R.id.radioText);
		final RadioButton radioInk = (RadioButton) dialog.findViewById(R.id.radioInk);
		
		bOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (radioText.isChecked()) {
					if (authoringOperators.textAnnotationExist(notesPath, videoName, author)) {
						sendXmlToApp(txtUtil.getAnnotationFileName(notesPath, videoName, author), context, author);
					} else {
						Toast toast = Util.createToast(context, author + " "
								+ context.getResources().getString(R.string.noAnnotations), 
								Toast.LENGTH_LONG);
						toast.show();
					}
				} else if (radioInk.isChecked()) {
					if (authoringOperators.textAnnotationExist(notesPath, videoName, author)) {
						sendXmlToApp(inkUtil.getAnnotationFileName(notesPath, videoName, author), context, author);
					} else {
						Toast toast = Util.createToast(context, author + " "
								+ context.getResources().getString(R.string.noAnnotations), 
								Toast.LENGTH_LONG);
						toast.show();
					}
					
				} else if (radioVideo.isChecked()) {
					sendVideoFile(videoName, videoPath, user, context);
				} else { // I+WaC
					sendJsonFile(txtUtil.getTextAnnotation(user), videoName, videoPath, videoDurationMs, context, author);					
				}
			}
		});
		
		bCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	

	private void sendXmlToApp(String path, Context context, String author) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/xml");
		intent.putExtra(Intent.EXTRA_SUBJECT,
				context.getResources().getString(R.string.msgSubject) + " "
						+ author);
		intent.putExtra(Intent.EXTRA_TEXT, context.getResources().getString(R.string.msgBodyXml));
		File source = new File(path);
		if (!source.exists()) {
			return;
		}
		Uri uri = Uri.fromFile(source);
		intent.putExtra(Intent.EXTRA_STREAM, uri);
		context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.chooseApp)));
	}
	
	/**
	 * Envia multiplos arquivo atraves de uma app escolhida.
	 * 
	 * @param notesPath
	 * @param videoName
	 * @param videoPath
	 * @param author
	 * @param context
	 */
	private void sendVideoFile(String videoName, String videoPath, String user, Context context) {
		ContentValues content = new ContentValues(4);
		content.put(Video.VideoColumns.DATE_ADDED, System.currentTimeMillis() / 1000);
		content.put(Video.Media.MIME_TYPE, "video/mp4");
		content.put(MediaStore.Video.Media.DATA, videoPath);
		ContentResolver resolver = context.getContentResolver();
		Uri uri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, content);

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.msgSubjectVideo) + " " + user);
		intent.putExtra(Intent.EXTRA_TEXT, context.getResources().getString(R.string.msgBodyVideo));
		intent.setType("video/mp4");
		intent.putExtra(android.content.Intent.EXTRA_STREAM,uri);
		context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.chooseApp)));
	}
	
	private void sendJsonFile(TextAnnotation textAnnotation, String videoName, String videoPath, long videoDurationMs, Context context, String author) {
		IWaCConverter converter = new IWaCConverter();
		String jsonFilePath = converter.convertTextAnnotationToJson(textAnnotation, videoDurationMs, 
				videoName, videoName + "." + VideoUtil.MP4_VIDEO_FORMAT, context);
		if (jsonFilePath != null) {
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("application/json");
			intent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.msgSubjectIwac) + " " + author);
			intent.putExtra(Intent.EXTRA_TEXT, context.getResources().getString(R.string.msgBodyIwac));
			File source = new File(jsonFilePath);
			Uri uri = Uri.fromFile(source);
			intent.putExtra(Intent.EXTRA_STREAM, uri);
			context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.chooseApp)));
		}
	}

}
