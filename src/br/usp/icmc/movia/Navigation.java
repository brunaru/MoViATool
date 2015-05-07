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

import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import br.usp.icmc.movia.annotation.AnnotationContainer;
import br.usp.icmc.movia.annotation.operators.AuthoringOperators;
import br.usp.icmc.movia.annotation.text.TextAnnotationInfo;
import br.usp.icmc.movia.util.Util;
import br.usp.icmc.movia.util.VideoUtil;

import br.usp.icmc.movia.R;

/**
 * Classe que cria a janela de navegacao.
 * 
 * @author Brunaru
 * 
 */
public class Navigation {
	
	static void showDialog(final Context context, final VideoView myVideoView, final String notesPath, final String videoName, final String author,
			final Button bNavigation, final String videoPath, final String screensPath, final SeekBar seekBar, final AnnotationContainer container,
			final AuthoringOperators authoringOperators, EditText editTextNote, final String user) {
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.navigationdialog);
		dialog.setTitle(R.string.navigation_title);
		dialog.setCancelable(Boolean.TRUE);
		TextView textBy = (TextView) dialog.findViewById(R.id.tNavBy);
		textBy.setText(context.getResources().getString(R.string.annotationsBy) + " " + author);
		textBy.setTextColor(Color.WHITE);
		textBy.setTextAppearance(context, android.R.style.TextAppearance_Medium);
		
		/* Opcao default. */
		addTextThumbnails(dialog, context, myVideoView, notesPath, videoName, author, bNavigation, videoPath, screensPath, seekBar, container,
				authoringOperators, user);
		
		RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroupType);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.radioText) {
					addTextThumbnails(dialog, context, myVideoView, notesPath, videoName, author, bNavigation, videoPath, screensPath, seekBar, container,
							authoringOperators, user);
				} else if (checkedId == R.id.radioInk) {
					addInkThumbnails(dialog, context, myVideoView, notesPath, videoName, author, bNavigation, videoPath, screensPath, seekBar, container,
							authoringOperators, user);
				} else if (checkedId == R.id.radioAudio) {
					addAudioThumbnails(dialog, context, myVideoView, notesPath, videoName, author, bNavigation, videoPath, screensPath, seekBar, container, 
							authoringOperators, user);
				}
				
			}
		});

		Button bCancel = (Button) dialog.findViewById(R.id.bCancel);
		bCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		editTextNote.clearFocus();
		dialog.show();
	}
	
	/**
	 * Adiciona miniaturas de anotacoes textuais a janela de navegacao.
	 * 
	 * @param dialog
	 * @param context
	 * @param myVideoView
	 * @param annotationMap
	 * @param notesPath
	 * @param videoName
	 * @param author
	 */
	public static void addTextThumbnails(final Dialog dialog, final Context context, final VideoView myVideoView, final String notesPath, 
			final String videoName, final String author, final Button bNavigation, String videoPath, String screensPath, final SeekBar seekBar, 
			final AnnotationContainer container, final AuthoringOperators authoringOperators, final String user) {
		LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.LinearLayoutNavDialog);
		layout.removeAllViews();

		for (Map.Entry<Integer, TextAnnotationInfo> entry : container.getTextAnnotationMap().get(author).entrySet()) {
			final TextAnnotationInfo annotation = entry.getValue();

			/* Imagem do Thumbnail. */
			ImageView imageView = new ImageView(context);
			imageView.setId(R.id.navImageViewId);
			//String myJpgPath = annotation.getScreen();
			Bitmap bmp = VideoUtil.getThumbnailBitmap(videoPath, screensPath, videoName, annotation.getTime(), context, VideoUtil.TEXT_THUMBNAIL);			
			imageView.setImageBitmap(bmp);
			RelativeLayout.LayoutParams imgParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			imgParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
			imgParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);			
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int position = annotation.getTime() * 1000;
					myVideoView.seekTo(position);
					seekBar.setProgress(position);
					dialog.dismiss();
				}
			});	

			/* Botao Edit/Remove. */
			Button bEdit = new Button(context);
			bEdit.setId(R.id.navButtonViewId);
			bEdit.setText(R.string.editorremove);
			RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			buttonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
			buttonParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			bEdit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!author.equalsIgnoreCase(user)) {
						Toast toast = Util.createToast(context, context.getResources().getString(R.string.cantedit), Toast.LENGTH_LONG);
						toast.show();
					} else {
						EditOrRemoveTextualAnnotation.editOrRemove(context, annotation, notesPath, videoName, author, dialog, bNavigation, container, authoringOperators);
					}					
				}
			});
			
			/* Texto da Anotacao. */
			TextView textView = new TextView(context);
			String text = VideoUtil.formatSeconds(annotation.getTime()) + " " + annotation.getText();
			textView.setText(text);
			RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			textParams.addRule(RelativeLayout.LEFT_OF, bEdit.getId());
			textParams.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
			textParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			textView.setEllipsize(TextUtils.TruncateAt.END);
			textView.setSingleLine();
			textView.setPadding(10, 0, 10, 0);			
			textView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int position = annotation.getTime() * 1000;
					myVideoView.seekTo(position);
					seekBar.setProgress(position);
					dialog.dismiss();
				}
			});

			/* Adiciona no layout relativo com respectivos parametros. */
			RelativeLayout.LayoutParams relParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			RelativeLayout newLayout = new RelativeLayout(context);
			newLayout.setLayoutParams(relParams);
			newLayout.addView(imageView, imgParams);			
			newLayout.addView(bEdit, buttonParams);
			newLayout.addView(textView, textParams);
			newLayout.setPadding(0, 0, 0, 15);

			layout.addView(newLayout);
		}
	}
	
	public static void addInkThumbnails(final Dialog dialog, final Context context, final VideoView myVideoView, final String notesPath, 
			final String videoName, final String author, final Button bNavigation, String videoPath, String screensPath, final SeekBar seekBar, 
			final AnnotationContainer container, final AuthoringOperators authoringOperators, final String user) {
		LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.LinearLayoutNavDialog);
		layout.removeAllViews();
		LayoutParams viewLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		for (final Integer time : container.getDrawAnnotationMap().get(author).keySet()) {
			ImageView imageView = new ImageView(context);			
			Bitmap bmp = VideoUtil.getThumbnailBitmap(videoPath, screensPath, videoName, time, context, VideoUtil.INK_THUMBNAIL);
			
			imageView.setImageBitmap(bmp);
			imageView.setLayoutParams(viewLayoutParams);

			TextView textView = new TextView(context);
			String text = VideoUtil.formatSeconds(time) + " " + context.getResources().getString(R.string.ink);
			textView.setText(text);
			textView.setLayoutParams(viewLayoutParams);
			textView.setPadding(10, 10, 0, 0);

			RelativeLayout.LayoutParams annParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
					LayoutParams.WRAP_CONTENT);
			annParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			LinearLayout annotationLayout = new LinearLayout(context);
			annotationLayout.setLayoutParams(annParams);
			annotationLayout.setOrientation(LinearLayout.HORIZONTAL);
			annotationLayout.addView(imageView);
			annotationLayout.addView(textView);
			annotationLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int position = time * 1000;
					myVideoView.seekTo(position);
					seekBar.setProgress(position);
					dialog.dismiss();
				}
			});
			
			Button bClear = new Button(context);
			bClear.setText(R.string.clear);
			RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			buttonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			buttonParams.rightMargin = 10;
			bClear.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!author.equalsIgnoreCase(user)) {
						Toast toast = Util.createToast(context, context.getResources().getString(R.string.cantremove), Toast.LENGTH_LONG);
						toast.show();
					} else {
						RemoveInkAnnotation.removeDialog(context, container.getDrawAnnotationMap().get(author).get(time), 
								notesPath, videoName, author, dialog, bNavigation, container, authoringOperators);
					}					
				}
			});
			
			RelativeLayout.LayoutParams relParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
					LayoutParams.WRAP_CONTENT);
			RelativeLayout newLayout = new RelativeLayout(context);
			newLayout.setLayoutParams(relParams);
			newLayout.addView(annotationLayout, annParams);
			newLayout.addView(bClear, buttonParams);
			newLayout.setPadding(0, 0, 0, 15);
			
			layout.addView(newLayout);
		}
	}
	
	public static void addAudioThumbnails(final Dialog dialog, final Context context, final VideoView myVideoView, final String notesPath, 
			final String videoName, final String author, final Button bNavigation, String videoPath, String screensPath, final SeekBar seekBar, 
			final AnnotationContainer container, final AuthoringOperators authoringOperators, final String user) {
		LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.LinearLayoutNavDialog);
		layout.removeAllViews();
		LayoutParams viewLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		for (final Integer time : container.getAudioAnnotationMap().get(author).keySet()) {
			ImageView imageView = new ImageView(context);			
			Bitmap bmp = VideoUtil.getThumbnailBitmap(videoPath, screensPath, videoName, time, context, VideoUtil.AUDIO_THUMBNAIL);
			
			imageView.setImageBitmap(bmp);
			imageView.setLayoutParams(viewLayoutParams);

			TextView textView = new TextView(context);
			String text = VideoUtil.formatSeconds(time) + " " + context.getResources().getString(R.string.audio);
			textView.setText(text);
			textView.setLayoutParams(viewLayoutParams);
			textView.setPadding(10, 10, 0, 0);

			RelativeLayout.LayoutParams annParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
					LayoutParams.WRAP_CONTENT);
			annParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			LinearLayout annotationLayout = new LinearLayout(context);
			annotationLayout.setLayoutParams(annParams);
			annotationLayout.setOrientation(LinearLayout.HORIZONTAL);
			annotationLayout.addView(imageView);
			annotationLayout.addView(textView);
			annotationLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int position = time * 1000;
					myVideoView.seekTo(position);
					seekBar.setProgress(position);
					dialog.dismiss();
				}
			});
			
			Button bClear = new Button(context);
			bClear.setText(R.string.remove);
			RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			buttonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			buttonParams.rightMargin = 10;
			bClear.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!author.equalsIgnoreCase(user)) {
						Toast toast = Util.createToast(context, context.getResources().getString(R.string.cantremove), Toast.LENGTH_LONG);
						toast.show();
					} else {
						RemoveAudioAnnotation.removeDialog(context, container.getAudioAnnotationMap().get(author).get(time), 
								notesPath, videoName, author, dialog, bNavigation, container, authoringOperators);
					}					
				}
			});
			
			RelativeLayout.LayoutParams relParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
					LayoutParams.WRAP_CONTENT);
			RelativeLayout newLayout = new RelativeLayout(context);
			newLayout.setLayoutParams(relParams);
			newLayout.addView(annotationLayout, annParams);
			newLayout.addView(bClear, buttonParams);
			newLayout.setPadding(0, 0, 0, 15);
			
			layout.addView(newLayout);
		}
	}
	
}
