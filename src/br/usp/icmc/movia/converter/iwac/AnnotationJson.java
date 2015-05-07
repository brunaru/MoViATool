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
package br.usp.icmc.movia.converter.iwac;

import java.util.List;

public class AnnotationJson {
	
	/** session begin timestamp */
	private long begin;
	/** can be the same as 'begin' */
	private long date_archived;
	/** can be the same as 'begin' */
	private long date_created;
	/** can be the same as 'begin' */
	private long date_recorded;
	/** can be the same as 'begin' */
	private long date_updated;
	/** session end timestamp */
	private long end;
	/** should be unique; suggestion: name + timestamp */
	private String id;
	
	/** all the media elements (video and audio) are listed here */
	private List<MediaElement> media_elements;
	
	/** must be movia */
	private String origin;
	/** users */
	private List<String> owners;
	/** NA must be empty */
	private List<String> parents;
	/** must be active */
	private String status;
	
	private List<Timeline> timelines;
	
	/** name of the session */
	private String title;
	/** must be external */
	private String type;
	
	public long getBegin() {
		return begin;
	}
	public void setBegin(long begin) {
		this.begin = begin;
	}
	public long getDate_archived() {
		return date_archived;
	}
	public void setDate_archived(long date_archived) {
		this.date_archived = date_archived;
	}
	public long getDate_created() {
		return date_created;
	}
	public void setDate_created(long date_created) {
		this.date_created = date_created;
	}
	public long getDate_recorded() {
		return date_recorded;
	}
	public void setDate_recorded(long date_recorded) {
		this.date_recorded = date_recorded;
	}
	public long getDate_updated() {
		return date_updated;
	}
	public void setDate_updated(long date_updated) {
		this.date_updated = date_updated;
	}
	public long getEnd() {
		return end;
	}
	public void setEnd(long end) {
		this.end = end;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<MediaElement> getMedia_elements() {
		return media_elements;
	}
	public void setMedia_elements(List<MediaElement> media_elements) {
		this.media_elements = media_elements;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public List<String> getOwners() {
		return owners;
	}
	public void setOwners(List<String> owners) {
		this.owners = owners;
	}
	public List<String> getParents() {
		return parents;
	}
	public void setParents(List<String> parents) {
		this.parents = parents;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<Timeline> getTimelines() {
		return timelines;
	}
	public void setTimelines(List<Timeline> timelines) {
		this.timelines = timelines;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
