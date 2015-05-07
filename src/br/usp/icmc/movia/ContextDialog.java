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

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.usp.icmc.movia.annotation.AuthorContext;
import br.usp.icmc.movia.util.ConstantsUtil;

import br.usp.icmc.movia.R;

public class ContextDialog {
	
	public static void showContextDialog(Context context, List<AuthorContext> textAuthorsCtx, 
			List<AuthorContext> inkAuthorsCtx, List<AuthorContext> audioAuthorsCtx) {
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		LinearLayout layout = new LinearLayout(context);
		layout.setLayoutParams(layoutParams);
		layout.setOrientation(LinearLayout.VERTICAL);
		Dialog dialog = new Dialog(context);
		dialog.setContentView(layout);
		dialog.setTitle(R.string.contextInfo);
		dialog.setCancelable(Boolean.TRUE);
		
		addContextText(context, textAuthorsCtx, ConstantsUtil.ANNOTATION_TEXT, layoutParams, layout);
		addContextText(context, inkAuthorsCtx, ConstantsUtil.ANNOTATION_INK, layoutParams, layout);
		addContextText(context, audioAuthorsCtx, ConstantsUtil.ANNOTATION_AUDIO, layoutParams, layout);
		
		dialog.show();
	}
	
	private static void addContextText(Context context, List<AuthorContext> authorsCtx, String type, 
			LayoutParams layoutParams, LinearLayout layout) {
		Resources res = context.getResources();
		for (AuthorContext authorCtx : authorsCtx) {
			TextView usernameText = new TextView(context);
			usernameText.setText(res.getString(R.string.username) + ": " + authorCtx.getUserName());
			usernameText.setLayoutParams(layoutParams);
			
			TextView typeText = new TextView(context);
			typeText.setText(res.getString(R.string.annotationType) + ": " + type);
			typeText.setLayoutParams(layoutParams);
			
			TextView dateText = new TextView(context);
			dateText.setText(res.getString(R.string.date) + ": " + authorCtx.getDate());
			dateText.setLayoutParams(layoutParams);
			
			TextView locationText = new TextView(context);
			locationText.setText(res.getString(R.string.location) + ": " + authorCtx.getLocation());
			locationText.setLayoutParams(layoutParams);
			
			TextView eventText = new TextView(context);
			String event = authorCtx.getEvent();
			if (event == null) {
				event = "";
			}
			eventText.setText(res.getString(R.string.event) + ": " + event);
			eventText.setLayoutParams(layoutParams);
			
			TextView deviceText = new TextView(context);
			deviceText.setText(res.getString(R.string.deviceModel) + ": " + authorCtx.getDeviceModel());
			deviceText.setLayoutParams(layoutParams);
			
			TextView apiText = new TextView(context);
			apiText.setText(res.getString(R.string.deviceApi) + ": " + authorCtx.getDeviceApi());
			apiText.setLayoutParams(layoutParams);			
			apiText.setPadding(0, 0, 10, 10);
			
			layout.addView(usernameText);
			layout.addView(typeText);
			layout.addView(dateText);
			layout.addView(locationText);
			layout.addView(eventText);
			layout.addView(deviceText);
			layout.addView(apiText);
		}
	}

}
