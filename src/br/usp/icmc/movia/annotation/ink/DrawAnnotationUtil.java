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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.location.Geocoder;
import android.location.LocationManager;
import br.usp.icmc.movia.annotation.AnnotationContainer;
import br.usp.icmc.movia.annotation.AnnotationUtil;
import br.usp.icmc.movia.annotation.AnnotationUtilCommon;
import br.usp.icmc.movia.annotation.AuthorContext;
import br.usp.icmc.movia.annotation.operators.ContextOperators;
import br.usp.icmc.movia.util.ConstantsUtil;


/**
 * Classe de utilidades de anotacoes em tinta.
 * 
 * @author Brunaru
 * 
 */
public class DrawAnnotationUtil implements AnnotationUtil {

	private Map<String, DrawAnnotation> drawAnnotationMap = new HashMap<String, DrawAnnotation>();
	
	public String getAnnotationFileName(String notesPath, String videoName, String author) {
		return notesPath + videoName + ConstantsUtil.DRAW_FILE_COMPL + author + ".xml";
	}

	public boolean initiateAnnotations(String notesPath, String videoName, String author, AnnotationContainer container) {
		Boolean exists = Boolean.FALSE;
		Serializer serializer = new Persister();
		File source = new File(getAnnotationFileName(notesPath, videoName, author));
		if (source.exists()) {
			exists = Boolean.TRUE;
		}
		try {
			container.getDrawAnnotationMap().get(author).clear();
			DrawAnnotation drawAnnotation = serializer.read(DrawAnnotation.class, source);
			drawAnnotationMap.put(author, drawAnnotation);
			List<DrawAnnotationInfo> annotationList = drawAnnotation.getAnnotationList();
			for (DrawAnnotationInfo annotation : annotationList) {
				int time = annotation.getTime();
				if (container.getDrawAnnotationMap().get(author).containsKey(time)) {
					container.getDrawAnnotationMap().get(author).get(time).add(annotation);
				} else {
					List<DrawAnnotationInfo> list = new ArrayList<DrawAnnotationInfo>();
					list.add(annotation);
					container.getDrawAnnotationMap().get(author).put(time, list);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exists;
	}

	public boolean annotationsExist(String notesPath, String videoName, String author) {
		Boolean exists = Boolean.FALSE;
		File source = new File(getAnnotationFileName(notesPath, videoName, author));
		if (source.exists()) {
			exists = Boolean.TRUE;
		}
		return exists;
	}

	/**
	 * Guarda a anotacao em tinta em um arquivo xml.
	 * 
	 * @param actions
	 * @param screensPath
	 * @param notesPath
	 * @param videoName
	 * @param author
	 * @param time
	 * @param drawAnnotationMap
	 * @return Anotacao armazenada.
	 */
	public DrawAnnotationInfo storeAnnotation(DrawAnnotationInfo annotation, String notesPath, String videoName, String author, int time,
			AnnotationContainer container) {
		annotation.setAddedBy(author);
		drawAnnotationMap.get(author).getAnnotationList().add(annotation);
		if (container.getDrawAnnotationMap().get(author).containsKey(time)) {
			container.getDrawAnnotationMap().get(author).get(time).add(annotation);
		} else {
			List<DrawAnnotationInfo> list = new ArrayList<DrawAnnotationInfo>();
			list.add(annotation);
			container.getDrawAnnotationMap().get(author).put(time, list);
		}
		writeFile(notesPath, videoName, author);
		return annotation;
	}

	/**
	 * Remove uma anotacao em tinta.
	 * 
	 * @param annotation
	 * @param drawAnnotationMap
	 * @param notesPath
	 * @param videoName
	 * @param author
	 */
	public void removeAnnotation(DrawAnnotationInfo annotation, String notesPath, String videoName, String author, AnnotationContainer container) {
		drawAnnotationMap.get(author).getAnnotationList().remove(annotation);
		container.getDrawAnnotationMap().get(author).remove(annotation.getTime());
		writeFile(notesPath, videoName, author);
	}

	public void writeFile(String notesPath, String videoName, String author) {
		String editiondate = ContextOperators.getCurretDate();
		drawAnnotationMap.get(author).setLastModified(editiondate);
		Serializer serializer = new Persister();
		File result = new File(getAnnotationFileName(notesPath, videoName, author));
		try {
			serializer.write(drawAnnotationMap.get(author), result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setAuthor(String author, LocationManager manager, Geocoder geoCoder, String event) {
		drawAnnotationMap.put(author, new DrawAnnotation());
		drawAnnotationMap.get(author).setOriginalAuthor(author);
		addAuthorContext(author, manager, geoCoder, event);
		String creationdate = ContextOperators.getCurretDate();
		drawAnnotationMap.get(author).setCreationdate(creationdate);
	}

	public void addAuthor(String author, LocationManager manager, Geocoder geoCoder, String event) {
		addAuthorContext(author, manager, geoCoder, event);
	}

	public void addAuthorContext(String author, LocationManager manager, Geocoder geoCoder, String event) {
		for (AuthorContext athrCtx : drawAnnotationMap.get(author).getAuthorsCtxList()) {
			if (athrCtx.getUserName().equalsIgnoreCase(author)) {
				return;
			}
		}
		AuthorContext authorCtx = AnnotationUtilCommon.completeAuthorContext(author, event, manager, geoCoder);
		drawAnnotationMap.get(author).getAuthorsCtxList().add(authorCtx);
	}

	public List<AuthorContext> getAuthorContextInfo(String author) {
		if (drawAnnotationMap.isEmpty() || drawAnnotationMap.get(author) == null) {
			return new ArrayList<AuthorContext>();
		}
		return drawAnnotationMap.get(author).getAuthorsCtxList();
	}

}
