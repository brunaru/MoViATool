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

public class Event {
	
	/** unique */
	private String id;
	
	/** agents */
	private List<Agent> agents;
	
	/** begin timestamp */
	private long begin;
	/** must be default */
	private String behavior;
	/** text annotation */
	private String comment;
	/** can be the same as 'begin' */
	private long date_created;
	/** end timestamp */
	private long end;
	/** begin - session.begin (relative begin) */
	private long session_begin;
	/** nd - session.end (relative end) */
	private long session_end;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Agent> getAgents() {
		return agents;
	}
	public void setAgents(List<Agent> agents) {
		this.agents = agents;
	}
	public long getBegin() {
		return begin;
	}
	public void setBegin(long begin) {
		this.begin = begin;
	}
	public String getBehavior() {
		return behavior;
	}
	public void setBehavior(String behavior) {
		this.behavior = behavior;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public long getDate_created() {
		return date_created;
	}
	public void setDate_created(long date_created) {
		this.date_created = date_created;
	}
	public long getEnd() {
		return end;
	}
	public void setEnd(long end) {
		this.end = end;
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
}

