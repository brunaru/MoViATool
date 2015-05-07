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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import br.usp.icmc.movia.annotation.ink.DrawAnnotationInfo;
import br.usp.icmc.movia.annotation.ink.PathAction;
import br.usp.icmc.movia.annotation.operators.AuthoringOperators;
import br.usp.icmc.movia.context.util.DateUtil;
import br.usp.icmc.movia.util.ConstantsUtil;
import br.usp.icmc.movia.util.MediaControl;


/**
 * View da area de anotacao com tinta.
 * 
 * @author Brunaru
 * 
 */
@SuppressLint("ViewConstructor")
class DrawView extends View {

	@SuppressLint("UseSparseArrays")
	private static Map<Integer, List<Path>> graphMap = new HashMap<Integer, List<Path>>();
	
	private ArrayList<Path> graphics = new ArrayList<Path>();
	
	private Paint mPaint;
	
	private Path path;
	
	private AuthoringOperators authoringOperators;
	
	private int originalWidth, originalHeight, newWidth, newHeight;
	
	private int time;
	
	ArrayList<PathAction> actions;
	DrawAnnotationInfo customPath;

	public DrawView(Context context, int width, int height, AuthoringOperators authoringOperators) {
		super(context);
		mPaint = new Paint();
		mPaint.setDither(true);
		mPaint.setColor(0xFFFFFF00);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(3);
		this.originalWidth = width;
		this.originalHeight = height;
		this.newWidth = width;
		this.newHeight = height;
		this.authoringOperators = authoringOperators;
		time = 0;
	}

	@SuppressLint("ClickableViewAccessibility") 
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
				&& authoringOperators.isInkAnnotationOn()) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				path = new Path();
				actions = new ArrayList<PathAction>();
				
				float x = event.getX();
				float y = event.getY();
				
				path.moveTo(x, y);
				path.lineTo(x, y);
				
				PathAction pathActionMove = new PathAction(x, y, PathAction.MOVE_TO);
				PathAction pathActionLine = new PathAction(x, y, PathAction.LINE_TO);
				actions.add(pathActionMove);
				actions.add(pathActionLine);
				
				graphics.add(path);
				graphMap.put(time, graphics);			
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				float x = event.getX();
				float y = event.getY();
				path.lineTo(x, y);
				PathAction pathActionLine = new PathAction(x, y, PathAction.LINE_TO);
				actions.add(pathActionLine);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				float x = event.getX();
				float y = event.getY();
				path.lineTo(x, y);
				PathAction pathActionLine = new PathAction(x, y, PathAction.LINE_TO);
				actions.add(pathActionLine);
				customPath = new DrawAnnotationInfo(time, actions, "", DateUtil.getCurretDate(), path);
			}
		} else {
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				MediaControl.getMediaControl().show();
			}
		}
		
		invalidate();
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		setMeasuredDimension(newWidth, newHeight);		
		float scaleWidth = (float) (newWidth) / (float) (originalWidth);
        float scaleHeight = (float) (newHeight) / (float) (originalHeight);
		canvas.scale(scaleWidth, scaleHeight);
		for (Path path : graphics) {
			canvas.drawPath(path, mPaint);
		}
	}
	
	/**
	 * Associa os novos valores de altura e largura do layout.
	 * 
	 * @param width
	 * @param height
	 */
	public void resize(int width, int height) {
		this.newWidth = width;
		this.newHeight = height;
	}
	
	/**
	 * Tempo de execucao atual do video.
	 * 
	 * @param newTime
	 */
	public void setTime(int newTime) {
		time = newTime;
	}
	
	/**
	 * Adiciona path (desenho) associado ao tempo de insercao.
	 * 
	 * @param pathList
	 * @param time
	 */
	public void addTimedPath(List<Path> pathList, int time) {
		graphics.addAll(pathList);
		graphMap.put(time, pathList);
	}
	
	/**
	 * Recupera o ultimo path (desenho) realizado).
	 * 
	 * @return
	 */
	public DrawAnnotationInfo getCustomPath() {
		DrawAnnotationInfo copyPath = customPath;
		customPath = null;
		return copyPath;
	}
	
	/**
	 * Remove anotacoes em tinta com tempo expirado.
	 * @param time
	 */
	public void removeTimedPath(int time) {
		if (!graphMap.isEmpty()) {
			List<Integer> remove = new ArrayList<Integer>();
			for (Iterator<Integer> it = graphMap.keySet().iterator(); it.hasNext();) {
				Integer gTime = it.next();
				if ((gTime < (time - ConstantsUtil.DRAW_ANNOTATION_TIMER)) || (gTime > (time))) {
					remove.add(gTime);
				}
			}
			for (Integer rtime : remove) {
				List<Path> pathList = graphMap.get(rtime);
				graphics.removeAll(pathList);
				graphMap.remove(rtime);
			}
			invalidate();
		} else {
			graphics.clear();
			invalidate();
		}
	}
}
