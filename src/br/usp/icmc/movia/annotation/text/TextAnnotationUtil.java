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
import br.usp.icmc.movia.context.util.DateUtil;
import br.usp.icmc.movia.util.ConstantsUtil;

/**
 * Classe de utilidades de anotacoes textuais.
 * 
 * @author Brunaru
 * 
 */
public class TextAnnotationUtil implements AnnotationUtil {

	private Map<String, TextAnnotation> textAnnotationMap = new HashMap<String, TextAnnotation>();
	
	public String getAnnotationFileName(String notesPath, String videoName, String author) {
		return notesPath + videoName + "_" + author + ".xml";
	}

	public boolean initiateAnnotations(String notesPath, String videoName, String author, AnnotationContainer container) {
		Boolean exists = Boolean.FALSE;
		Serializer serializer = new Persister();
		File source = new File(getAnnotationFileName(notesPath, videoName, author));
		if (source.exists()) {
			exists = Boolean.TRUE;
		}
		try {
			TextAnnotation textAnnotation = serializer.read(TextAnnotation.class, source);
			textAnnotationMap.put(author, textAnnotation);
			List<TextAnnotationInfo> annotationList = textAnnotation.getAnnotationList();
			for (TextAnnotationInfo annotation : annotationList) {
				container.getTextAnnotationMap().get(author).put(annotation.getTime(), annotation);
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
	 * Guarda a anotacao textual em um arquivo xml.
	 * 
	 * @param text
	 * @param screensPath
	 * @param notesPath
	 * @param videoName
	 * @param author
	 * @param time
	 * @param textAnnotationMap
	 * @return Anotacao armazenada.
	 */
	public TextAnnotationInfo storeAnnotation(String text, String notesPath, String videoName, String author, int time, AnnotationContainer container) {
		int n = 0;
		n = text.split("\\s+").length;
		int duration = (n / ConstantsUtil.ANNOTATION_DIVISOR_WPS) + ConstantsUtil.ANNOTATION_EXTRA_TIME;
		TextAnnotationInfo annotation = new TextAnnotationInfo(text, time, duration, author, DateUtil.getCurretDate());
		textAnnotationMap.get(author).getAnnotationList().add(annotation);
		container.getTextAnnotationMap().get(author).put(time, annotation);
		writeFile(notesPath, videoName, author);
		return annotation;
	}

	/**
	 * Remove uma anotacao textual.
	 * 
	 * @param annotation
	 * @param annotationMap
	 * @param notesPath
	 * @param videoName
	 * @param author
	 */
	public void removeAnnotation(TextAnnotationInfo annotation, String notesPath, String videoName, String author, AnnotationContainer container) {
		textAnnotationMap.get(author).getAnnotationList().remove(annotation);
		container.getTextAnnotationMap().get(author).remove(annotation.getTime());
		writeFile(notesPath, videoName, author);
	}
	
	public void deleteAnnotationFile(String notesPath, String videoName, String author, AnnotationContainer container) {
		container.getTextAnnotationMap().get(author).clear();
		deleteFile(notesPath, videoName, author);
	}

	/**
	 * Edita o texto de uma anotação.
	 * 
	 * @param annotation
	 * @param annotationMap
	 * @param notesPath
	 * @param videoName
	 * @param author
	 * @param newText
	 */
	public void editAnnotationText(TextAnnotationInfo annotation, String notesPath, String videoName, String author, String newText) {
		textAnnotationMap.get(author).getAnnotationList().remove(annotation);
		annotation.setText(newText);
		annotation.setAdditionTime(DateUtil.getCurretDate());
		annotation.setAddedBy(author);
		textAnnotationMap.get(author).getAnnotationList().add(annotation);
		writeFile(notesPath, videoName, author);
	}

	public void writeFile(String notesPath, String videoName, String author) {
		String editiondate = ContextOperators.getCurretDate();
		textAnnotationMap.get(author).setLastModified(editiondate);
		Serializer serializer = new Persister();
		File result = new File(getAnnotationFileName(notesPath, videoName, author));
		try {
			serializer.write(textAnnotationMap.get(author), result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean deleteFile(String notesPath, String videoName, String author) {
		File file = new File(getAnnotationFileName(notesPath, videoName, author));
		return file.delete();
	}

	public void setAuthor(String author, LocationManager manager, Geocoder geoCoder, String event) {
		textAnnotationMap.put(author, new TextAnnotation());
		textAnnotationMap.get(author).setOriginalAuthor(author);
		addAuthorContext(author, manager, geoCoder, event);
		String creationdate = ContextOperators.getCurretDate();
		textAnnotationMap.get(author).setCreationdate(creationdate);
	}

	public void addAuthor(String author, LocationManager manager, Geocoder geoCoder, String event) {
		addAuthorContext(author, manager, geoCoder, event);
	}

	public void addAuthorContext(String author, LocationManager manager, Geocoder geoCoder, String event) {
		for (AuthorContext athrCtx : textAnnotationMap.get(author).getAuthorsCtxList()) {
			if (athrCtx.getUserName().equalsIgnoreCase(author)) {
				return;
			}
		}
		AuthorContext authorCtx = AnnotationUtilCommon.completeAuthorContext(author, event, manager, geoCoder);
		textAnnotationMap.get(author).getAuthorsCtxList().add(authorCtx);
	}

	public List<AuthorContext> getAuthorContextInfo(String author) {
		if (textAnnotationMap.isEmpty() || textAnnotationMap.get(author) == null) {
			return new ArrayList<AuthorContext>();
		}
		return textAnnotationMap.get(author).getAuthorsCtxList();
	}
	
	public TextAnnotation getTextAnnotation(String user) {
		return textAnnotationMap.get(user);
	}

}
