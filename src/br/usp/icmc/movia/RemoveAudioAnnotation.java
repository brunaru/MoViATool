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
import android.widget.Toast;
import br.usp.icmc.movia.annotation.AnnotationContainer;
import br.usp.icmc.movia.annotation.audio.AudioAnnotationInfo;
import br.usp.icmc.movia.annotation.operators.AuthoringOperators;
import br.usp.icmc.movia.util.Util;

import br.usp.icmc.movia.R;

public class RemoveAudioAnnotation {

	/**
	 * Confirma a exclusao e remove uma anotacao em audio.
	 * 
	 * @param context
	 * @param audioAnnotation
	 * @param notesPath
	 * @param videoName
	 * @param author
	 * @param parent
	 * @param bNavigation
	 * @param container
	 * @param authoringOperators
	 */
	public static void removeDialog(final Context context, final AudioAnnotationInfo audioAnnotation, 
			final String notesPath, final String videoName, final String author, final Dialog parent, 
			final Button bNavigation, final AnnotationContainer container, final AuthoringOperators authoringOperators) {
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.removeannotation);
		dialog.setTitle("Remove annotation?");
		dialog.setCancelable(Boolean.TRUE);

		Button bOk = (Button) dialog.findViewById(R.id.bRemoveAnOk);
		Button bCancel = (Button) dialog.findViewById(R.id.bRemoveAnCancel);

		bOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				authoringOperators.removeAudioAnnotation(audioAnnotation, notesPath, videoName, author, container);
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
