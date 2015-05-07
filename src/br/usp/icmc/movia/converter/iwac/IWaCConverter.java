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

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import br.usp.icmc.movia.annotation.AuthorContext;
import br.usp.icmc.movia.annotation.text.TextAnnotation;
import br.usp.icmc.movia.annotation.text.TextAnnotationInfo;
import br.usp.icmc.movia.context.util.DateUtil;
import br.usp.icmc.movia.util.ConstantsUtil;

public class IWaCConverter {
	
	public String convertTextAnnotationToJson(TextAnnotation textAnnotation, long videoDurationMs, 
			String videoName, String videoFileName, Context context) {
		String filePath = ConstantsUtil.NOTES_FULL_PATH + videoName + ".json";
		AnnotationJson annotationJson = textAnnotationToAnnotationJson(textAnnotation, videoDurationMs, videoName, videoFileName);
		String json = toJson(annotationJson);
		if (json == null) {
			return null;
		}
		File file = new File(filePath);
		PrintWriter pw;
		try {
			pw = new PrintWriter(file,"UTF-8");
			pw.write(json);
			pw.flush();
			pw.close();
			if (pw.checkError()) {				
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return filePath;
	}
	
	private AnnotationJson textAnnotationToAnnotationJson(TextAnnotation textAnnotation, long videoDurationMs, 
			String videoName, String videoFileName) {
		AnnotationJson annotation = new AnnotationJson();
		long videoDurationSec = videoDurationMs/1000;
		long begin = findSessionCreationTime(textAnnotation);
		long lastModified = DateUtil.dateToTimestampSec(textAnnotation.getLastModified());
		annotation.setBegin(begin);
		annotation.setDate_archived(begin);
		annotation.setDate_created(begin);
		annotation.setDate_recorded(begin);
		annotation.setDate_updated(lastModified);
		long end = begin + videoDurationSec;
		annotation.setEnd(end);
		String id = videoName + "_" + begin;
		annotation.setId(id);
		
		/* MediaElement (por enquanto apenas video) */
		MediaElement me = new MediaElement();
		me.setId(videoName);
		me.setBegin(begin);
		me.setEnd(end);
		me.setFilename(Constants.URL_MOVIA + id + File.separator + videoFileName);
		me.setSession_begin(0);
		me.setSession_end(videoDurationSec);
		me.setType(Constants.TYPE_VIDEO);
		List<MediaElement> melems = new ArrayList<MediaElement>();
		melems.add(me);
		annotation.setMedia_elements(melems);
		
		annotation.setOrigin(Constants.ORIGIN);
		List<String> owners = new ArrayList<String>();
		for (AuthorContext ctx  : textAnnotation.getAuthorsCtxList()) {
			owners.add(ctx.getUserName());
		}
		annotation.setOwners(owners);
		annotation.setParents(new ArrayList<String>());
		annotation.setStatus(Constants.STATUS_ACTIVE);
		
		/* Por enquanto apenas uma timeline */
		List<Timeline> timelines = new ArrayList<Timeline>();
		Timeline timeline = new Timeline();
		timeline.setId(videoName);
		timeline.setDate_created(begin);
		timeline.setDate_updated(lastModified);
		
		int ids = 100;
		List<Event> events = new ArrayList<Event>();
		for (TextAnnotationInfo textInfo : textAnnotation.getAnnotationList()) {
			Event event = new Event();
			event.setId(Constants.TEXT + ids);
			ids++;
			
			List<Agent> agents = new ArrayList<Agent>();
			Agent agent = new Agent();
			agent.setLabel(textInfo.getAddedBy());
			agent.setRole(Constants.ROLE_AUTHOR);
			agents.add(agent);
			event.setAgents(agents);
			
			long annTime = textInfo.getTime();
			long beginTime = annTime + begin;
			event.setBegin(beginTime);
			
			event.setBehavior(Constants.BEHAVIOR_DEFAULT);
			event.setComment(textInfo.getText());
			event.setDate_created(DateUtil.dateToTimestampSec(textInfo.getAdditionTime()));
			long timeEnd = beginTime + textInfo.getDuration();
			event.setEnd(timeEnd);
			event.setSession_begin(textInfo.getTime());
			event.setSession_end(textInfo.getTime()+textInfo.getDuration());
			events.add(event);
		}
		
		timeline.setEvents(events);
		
		timeline.setLabel(Constants.TIMELINE_LABEL);
		timeline.setTaxonomy_class(Constants.TAXONOMY_CLASS);
		
		timelines.add(timeline);
		annotation.setTimelines(timelines);
		
		annotation.setTitle(Constants.SESSION_TITLE + ": " + videoName);
		annotation.setType(Constants.TYPE_EXTERNAL);
		return annotation;
	}
	
	private long findSessionCreationTime(TextAnnotation textAnnotation) {
		long creationTime = DateUtil.dateToTimestampSec(textAnnotation.getCreationdate());
		int index = 0;
		long firstTime = DateUtil.dateToTimestampSec(textAnnotation.getAnnotationList().get(index).getAdditionTime());
		/** Encontra a primeira anotacao realizada. */
		for (TextAnnotationInfo textInfo : textAnnotation.getAnnotationList()) {
			long addTime = DateUtil.dateToTimestampSec(textInfo.getAdditionTime());
			if (addTime < firstTime) {
				firstTime = addTime;
				index = textAnnotation.getAnnotationList().indexOf(textInfo);
			}
		}
		int relativeTime = textAnnotation.getAnnotationList().get(index).getTime();
		long originalCreation = creationTime - relativeTime;
		return originalCreation;
	}
	
	public String toJson(AnnotationJson annotation) {
		try {
			JSONObject jsonAnnotation = new JSONObject();
			jsonAnnotation.put("begin", annotation.getBegin());
			jsonAnnotation.put("date_archived", annotation.getDate_archived());
			jsonAnnotation.put("date_created", annotation.getDate_created());
			jsonAnnotation.put("date_recorded", annotation.getDate_recorded());
			jsonAnnotation.put("date_updated", annotation.getDate_updated());
			jsonAnnotation.put("end", annotation.getEnd());
			jsonAnnotation.put("id", annotation.getId());
			
			JSONObject jsonMediaElements = new JSONObject();
			for (MediaElement me : annotation.getMedia_elements()) {
				JSONObject jsonMediaElement = new JSONObject();
				jsonMediaElement.put("begin", me.getBegin());
				jsonMediaElement.put("end", me.getEnd());
				jsonMediaElement.put("filename", me.getFilename());
				jsonMediaElement.put("session_begin", me.getSession_begin());
				jsonMediaElement.put("session_end", me.getSession_end());
				jsonMediaElement.put("type", me.getType());
				jsonMediaElements.put(me.getId(), jsonMediaElement);
			}
			jsonAnnotation.put("media_elements", jsonMediaElements);
			
			jsonAnnotation.put("origin", annotation.getOrigin());
			JSONArray jsonArrOwner = new JSONArray();
			for (String owner : annotation.getOwners()) {
				jsonArrOwner.put(owner);
			}
			jsonAnnotation.put("owner", jsonArrOwner);
			JSONArray jsonArrParents = new JSONArray();
			for (String parent : annotation.getParents()) {
				jsonArrParents.put(parent);
			}
			jsonAnnotation.put("parents", jsonArrParents);
			jsonAnnotation.put("status", annotation.getStatus());
			
			JSONObject jsonTimelines = new JSONObject();
			for (Timeline tl : annotation.getTimelines()) {
				JSONObject jsonTimeline = new JSONObject();
				jsonTimeline.put("date_created", tl.getDate_created());
				jsonTimeline.put("date_updated", tl.getDate_updated());
				
				JSONObject jsonEvents = new JSONObject();
				for (Event event : tl.getEvents()) {
					JSONObject jsonEvent = new JSONObject();
					JSONArray jsonArrAgents = new JSONArray();
					for (Agent agent : event.getAgents()) {
						JSONObject jsonAgent = new JSONObject();
						jsonAgent.put("label", agent.getLabel());
						jsonAgent.put("role", agent.getRole());
						jsonArrAgents.put(jsonAgent);
					}
					jsonEvent.put("agents", jsonArrAgents);
					
					jsonEvent.put("begin", event.getBegin());
					jsonEvent.put("behavior", event.getBehavior());
					jsonEvent.put("comment", event.getComment());
					jsonEvent.put("date_created", event.getDate_created());
					jsonEvent.put("end", event.getEnd());
					jsonEvent.put("session_begin", event.getSession_begin());
					jsonEvent.put("session_end", event.getSession_end());
					
					jsonEvents.put(event.getId(), jsonEvent);
				}
				jsonTimeline.put("events", jsonEvents);
				
				jsonTimeline.put("label", tl.getLabel());
				jsonTimeline.put("taxonomy_class", tl.getTaxonomy_class());
				
				jsonTimelines.put(tl.getId(), jsonTimeline);
			}
			jsonAnnotation.put("timelines", jsonTimelines);
			
			jsonAnnotation.put("title", annotation.getTitle());
			jsonAnnotation.put("type", annotation.getType());
			
			return jsonAnnotation.toString();
			
		} catch(JSONException  e) {
			e.printStackTrace();
			return null;
		}
	}

}
