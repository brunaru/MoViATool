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

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Customizacao da classe VideoView para adequacao de tamanho em layout do tipo
 * retrato e paisagem.
 * 
 * @author Brunaru
 * 
 */
public class VideoViewCustom extends VideoView {

	private int mForceHeight = 0;
	private int mForceWidth = 0;

	public VideoViewCustom(Context context) {
		super(context);
	}

	public VideoViewCustom(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public VideoViewCustom(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setDimensions(int w, int h) {
		this.mForceHeight = h;
		this.mForceWidth = w;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(mForceWidth, mForceHeight);
	}
}
