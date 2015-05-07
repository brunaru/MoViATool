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
package br.usp.icmc.movia.annotation.text;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class TextAnnotationInfo {
	
	@Element
	private String text;
	
	@Element
	private int time;
	
	@Element
	private int duration;
	
	@Element
	private String addedBy;
	
	@Element
	private String additionTime;

	public TextAnnotationInfo() {
		super();
	}
	
	public TextAnnotationInfo(String text, int time, int duration, String addedBy, String additionTime) {
		this.setText(text);
		this.time = time;
		this.duration = duration;
		this.addedBy = addedBy;
		this.additionTime = additionTime;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public String getAdditionTime() {
		return additionTime;
	}

	public void setAdditionTime(String additionTime) {
		this.additionTime = additionTime;
	}
}
