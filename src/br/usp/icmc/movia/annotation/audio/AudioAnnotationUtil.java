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
package br.usp.icmc.movia.annotation.audio;

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
 * Classe de utilidades de anotacoes em audio.
 * 
 * @author Brunaru
 * 
 */
public class AudioAnnotationUtil implements AnnotationUtil {

	private Map<String, AudioAnnotation> audioAnnotationMap = new HashMap<String, AudioAnnotation>();
	
	public String getAnnotationFileName(String notesPath, String videoName, String author) {
		return notesPath + videoName + ConstantsUtil.AUDIO_FILE_COMPL + author + ".xml";
	}

	public boolean initiateAnnotations(String notesPath, String videoName, String author, AnnotationContainer container) {
		Boolean exists = Boolean.FALSE;
		Serializer serializer = new Persister();
		File source = new File(getAnnotationFileName(notesPath, videoName, author));
		if (source.exists()) {
			exists = Boolean.TRUE;
		}
		try {			
			AudioAnnotation audioAnnotation = serializer.read(AudioAnnotation.class, source);
			audioAnnotationMap.put(author, audioAnnotation);
			List<AudioAnnotationInfo> annotationList = audioAnnotation.getAnnotationList();
			for (AudioAnnotationInfo annotation : annotationList) {
				container.getAudioAnnotationMap().get(author).put(annotation.getTime(), annotation);
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
	 * Guarda a anotacao em audio em um arquivo xml.
	 * 
	 * @param audioFileName
	 * @param notesPath
	 * @param videoName
	 * @param author
	 * @param time
	 * @param container
	 * @return Anotacao armazanada.
	 */
	public AudioAnnotationInfo storeAnnotation(String audioFileName, String notesPath, String videoName, String author, int time,
			AnnotationContainer container) {
		AudioAnnotationInfo annotation = new AudioAnnotationInfo(audioFileName, time, author, DateUtil.getCurretDate());
		audioAnnotationMap.get(author).getAnnotationList().add(annotation);
		container.getAudioAnnotationMap().get(author).put(time, annotation);
		writeFile(notesPath, videoName, author);
		return annotation;
	}

	/**
	 * Remove uma anotacao em audio.
	 * 
	 * @param annotation
	 * @param annotationMap
	 * @param notesPath
	 * @param videoName
	 * @param author
	 */
	public void removeAnnotation(AudioAnnotationInfo annotation, String notesPath, String videoName, String author, AnnotationContainer container) {
		String audioFileName = annotation.getAudioFileName();
		File audio = new File(audioFileName);
		audio.delete();
		audioAnnotationMap.get(author).getAnnotationList().remove(annotation);
		container.getAudioAnnotationMap().get(author).remove(annotation.getTime());
		writeFile(notesPath, videoName, author);
	}

	public void writeFile(String notesPath, String videoName, String author) {
		String editiondate = ContextOperators.getCurretDate();
		audioAnnotationMap.get(author).setLastModified(editiondate);
		Serializer serializer = new Persister();
		File result = new File(getAnnotationFileName(notesPath, videoName, author));
		try {
			serializer.write(audioAnnotationMap.get(author), result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setAuthor(String author, LocationManager manager, Geocoder geoCoder, String event) {
		audioAnnotationMap.put(author, new AudioAnnotation());
		audioAnnotationMap.get(author).setOriginalAuthor(author);
		addAuthorContext(author, manager, geoCoder, event);
		String creationdate = ContextOperators.getCurretDate();
		audioAnnotationMap.get(author).setCreationdate(creationdate);
	}

	public void addAuthor(String author, LocationManager manager, Geocoder geoCoder, String event) {
		addAuthorContext(author, manager, geoCoder, event);
	}

	public void addAuthorContext(String author, LocationManager manager, Geocoder geoCoder, String event) {
		for (AuthorContext athrCtx : audioAnnotationMap.get(author).getAuthorsCtxList()) {
			if (athrCtx.getUserName().equalsIgnoreCase(author)) {
				return;
			}
		}
		AuthorContext authorCtx = AnnotationUtilCommon.completeAuthorContext(author, event, manager, geoCoder);
		audioAnnotationMap.get(author).getAuthorsCtxList().add(authorCtx);
	}

	public List<AuthorContext> getAuthorContextInfo(String author) {
		if (audioAnnotationMap.isEmpty() || audioAnnotationMap.get(author) == null) {
			return new ArrayList<AuthorContext>();
		}
		return audioAnnotationMap.get(author).getAuthorsCtxList();
	}

}
