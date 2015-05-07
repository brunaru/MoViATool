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

import br.usp.icmc.movia.util.VideoUtil;

import br.usp.icmc.movia.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class VideoArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;
	private final Map<String, Bitmap> bmpMap;

	public VideoArrayAdapter(Context context, String[] values, Map<String, Bitmap> bmpMap) {
		super(context, R.layout.list_video, values);
		this.context = context;
		this.values = values;
		this.bmpMap = bmpMap;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.list_video, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.videoLabel);
		String path = values[position];
		textView.setText(VideoUtil.getVideoName(path));
		Bitmap bmp = bmpMap.get(path);
		BitmapDrawable bmpDraw = new BitmapDrawable(this.getContext().getResources(), bmp);
		textView.setCompoundDrawablesWithIntrinsicBounds(bmpDraw, null, null, null);
		textView.setCompoundDrawablePadding(20);
		return rowView;
	}
}
