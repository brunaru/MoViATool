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

public class MediaElement {
	
	/** unique */
	private String id;
	
	/** begin timestamp */
	private long begin;
	/** end timestamp */
	private long end;
	/** url filename */
	private String filename;
	/** media_element.begin - session.begin (relative begin) */
	private long session_begin;
	/** media_element.end - session.begin (relative end) */
	private long session_end;
	/** type: video or audio */
	private String type;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getBegin() {
		return begin;
	}
	public void setBegin(long begin) {
		this.begin = begin;
	}
	public long getEnd() {
		return end;
	}
	public void setEnd(long end) {
		this.end = end;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public long getSession_begin() {
		return session_begin;
	}
	public void setSession_begin(long session_begin) {
		this.session_begin = session_begin;
	}
	public long getSession_end() {
		return session_end;
	}
	public void setSession_end(long session_end) {
		this.session_end = session_end;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
