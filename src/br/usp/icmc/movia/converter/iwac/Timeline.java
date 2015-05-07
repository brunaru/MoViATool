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

public class Timeline {
	
	/** unique */
	private String id;
	
	/** create timestamp */
	private long date_created;
	/** can be the same as date_created */
	private long date_updated;
	
	/** each timeline has a number of "events" (annotations) */
	private List <Event> events;
	
	/** the label of the timeline (Annotations) */
	private String label;
	
	/** must be wac */
	private String taxonomy_class;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getDate_created() {
		return date_created;
	}

	public void setDate_created(long date_created) {
		this.date_created = date_created;
	}

	public long getDate_updated() {
		return date_updated;
	}

	public void setDate_updated(long date_updated) {
		this.date_updated = date_updated;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTaxonomy_class() {
		return taxonomy_class;
	}

	public void setTaxonomy_class(String taxonomy_class) {
		this.taxonomy_class = taxonomy_class;
	}
	
}