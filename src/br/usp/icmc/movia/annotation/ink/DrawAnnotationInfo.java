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
package br.usp.icmc.movia.annotation.ink;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import android.graphics.Path;

@Root
public class DrawAnnotationInfo {
	
	@ElementList
	private List<PathAction> actions = new ArrayList<PathAction>();
	
	@Element
	private int time;
	
	@Element
	private String addedBy;
	
	@Element
	private String additionTime;
	
	private Path path;
	
	public DrawAnnotationInfo() {
	}
	
	public DrawAnnotationInfo(int time, List<PathAction> actions, String addedBy, String additionTime) {
		this.time = time;
		this.actions = actions;
		this.addedBy = addedBy;
		this.additionTime = additionTime;
	}
	
	public DrawAnnotationInfo(int time, List<PathAction> actions, String addedBy, String additionTime, Path path) {
		this.time = time;
		this.actions = actions;
		this.addedBy = addedBy;
		this.additionTime = additionTime;
		this.path = path;
	}

	public List<PathAction> getActions() {
		return actions;
	}

	public void setActions(List<PathAction> actions) {
		this.actions = actions;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	public Path getPath() {
		if (path != null) {
			return path;
		} else {
			path = new Path();
			for (PathAction p : actions) {
				if (p.getType() == PathAction.MOVE_TO) {
					path.moveTo(p.getX(), p.getY());
				} else if (p.getType() == PathAction.LINE_TO) {
					path.lineTo(p.getX(), p.getY());
				}
			}
			return path;
		}

	}

	public void setPath(Path path) {
		this.path = path;
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
