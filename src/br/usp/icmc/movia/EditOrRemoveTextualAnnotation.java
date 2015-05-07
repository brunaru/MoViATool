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

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.usp.icmc.movia.annotation.AnnotationContainer;
import br.usp.icmc.movia.annotation.operators.AuthoringOperators;
import br.usp.icmc.movia.annotation.text.TextAnnotationInfo;
import br.usp.icmc.movia.util.Util;

import br.usp.icmc.movia.R;

public class EditOrRemoveTextualAnnotation {
	
	public static void editOrRemove(final Context context, final TextAnnotationInfo annotation, final String notesPath, 
			final String videoName, final String author, final Dialog parent, final Button bNavigation,
			final AnnotationContainer container, final AuthoringOperators authoringOperators) {
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.annotationchange);
		dialog.setTitle("Choose option");
		dialog.setCancelable(Boolean.TRUE);
		Button bEdit = (Button) dialog.findViewById(R.id.bWEditAn);
		Button bRemove = (Button) dialog.findViewById(R.id.bWRemoveAn);
		Button bCancel = (Button) dialog.findViewById(R.id.bWCancel);

		bEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				editDialog(context, annotation, notesPath, videoName, author, parent, bNavigation, container, authoringOperators);
				dialog.dismiss();
			}
		});

		bRemove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				removeDialog(context, annotation, notesPath, videoName, author, parent, bNavigation, container, authoringOperators);
				dialog.dismiss();
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
	
	private static void editDialog(final Context context, final TextAnnotationInfo annotation, final String notesPath, 
			final String videoName, final String author, final Dialog parent, final Button bNavigation,
			final AnnotationContainer container, final AuthoringOperators authoringOperators) {
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.editannotation);
		dialog.setTitle(R.string.editAnnotation);
		dialog.setCancelable(Boolean.TRUE);
		
		Button bOk = (Button) dialog.findViewById(R.id.bEditAnOk);
		Button bCancel = (Button) dialog.findViewById(R.id.bEditAnCancel);
		final EditText eText = (EditText) dialog.findViewById(R.id.editAn);
		eText.setText(annotation.getText());
		
		bOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String newText = eText.getText().toString();
				authoringOperators.editTextAnnotation(annotation, notesPath, videoName, author, newText);
				dialog.dismiss();
				parent.dismiss();
				bNavigation.performClick();
				Toast toast = Util.createToast(context, context.getResources().getString(R.string.annEdited), Toast.LENGTH_LONG);
				toast.show();
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

	private static void removeDialog(final Context context, final TextAnnotationInfo annotation, final String notesPath, 
			final String videoName, final String author, final Dialog parent, final Button bNavigation, 
			final AnnotationContainer container, final AuthoringOperators authoringOperators) {
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.removeannotation);
		dialog.setTitle("Remove annotation?");
		dialog.setCancelable(Boolean.TRUE);

		Button bOk = (Button) dialog.findViewById(R.id.bRemoveAnOk);
		Button bCancel = (Button) dialog.findViewById(R.id.bRemoveAnCancel);

		bOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				authoringOperators.removeTextAnnotation(annotation, notesPath, videoName, author, container);
				dialog.dismiss();
				parent.dismiss();
				bNavigation.performClick();
				Toast toast = Util.createToast(context, context.getResources().getString(R.string.annRemoved), Toast.LENGTH_LONG);
				toast.show();
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

}
