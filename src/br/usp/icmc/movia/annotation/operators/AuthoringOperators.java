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
package br.usp.icmc.movia.annotation.operators;

import java.util.List;

import android.content.Context;
import android.location.Geocoder;
import android.location.LocationManager;
import br.usp.icmc.movia.annotation.AnnotationContainer;
import br.usp.icmc.movia.annotation.AuthorContext;
import br.usp.icmc.movia.annotation.audio.AudioAnnotationInfo;
import br.usp.icmc.movia.annotation.audio.AudioAnnotationUtil;
import br.usp.icmc.movia.annotation.ink.DrawAnnotationInfo;
import br.usp.icmc.movia.annotation.ink.DrawAnnotationUtil;
import br.usp.icmc.movia.annotation.text.TextAnnotationInfo;
import br.usp.icmc.movia.annotation.text.TextAnnotationUtil;
import br.usp.icmc.movia.annotation.util.AnnotationControl;
import br.usp.icmc.movia.share.ShareAnnotation;


/**
 * Classe de operadores de autoria.
 * 
 * @author Brunaru
 * 
 */
public class AuthoringOperators {

	/** Permite realizar acoes com anotacoes textuais. */
	TextAnnotationUtil txtUtil = new TextAnnotationUtil();
	
	/** Permite realizar acoes com anotacoes em tinta. */
	DrawAnnotationUtil inkUtil = new DrawAnnotationUtil();
	
	/** Permite realizar acoes com anotacoes em audio. */
	AudioAnnotationUtil audioUtil = new AudioAnnotationUtil();

	/**
	 * Verifica a existencia de um arquivo de anotacoes textuais para
	 * determinados video e autor.
	 * 
	 * @param notesPath
	 * @param videoName
	 * @param author
	 * @return
	 */
	public boolean textAnnotationExist(String notesPath, String videoName, String author) {
		return txtUtil.annotationsExist(notesPath, videoName, author);
	}

	/**
	 * Inicializa arquivo de anotacoes textuais.
	 * 
	 * @param notesPath
	 * @param videoName
	 * @param author
	 * @param container
	 */
	public void initiateTextAnnotations(String notesPath, String videoName, String author, AnnotationContainer container) {
		txtUtil.initiateAnnotations(notesPath, videoName, author, container);
	}

	/**
	 * Configura o autor do arquivo de anotacoes textuais, caso ainda nao
	 * exista.
	 * 
	 * @param user
	 * @param locManager
	 * @param geoCoder
	 * @param event
	 */
	public void setTextAnnotationsAuthor(String user, LocationManager locManager, Geocoder geoCoder, String event) {
		txtUtil.setAuthor(user, locManager, geoCoder, event);
	}

	/**
	 * Adiciona um autor de anotacoes textuais.
	 * 
	 * @param author
	 * @param locManager
	 * @param geoCoder
	 * @param event
	 */
	public void addTextAnnotationsCoAuthor(String author, LocationManager locManager, Geocoder geoCoder, String event) {
		txtUtil.addAuthor(author, locManager, geoCoder, event);
	}

	/**
	 * Guarda a anotacao textual em um arquivo xml.
	 * 
	 * @param text texto da anotacao
	 * @param screensPath caminho para diretorio de miniaturas
	 * @param notesPath caminho para diretorio de anotacoes
	 * @param videoName nome do video (nao completo)
	 * @param author autor da anotacao
	 * @param time tempo em segundos
	 * @param container container de anotacao a ser atualizado
	 */
	public void storeTextAnnotation(String text, String notesPath, String videoName, String author, int time,
			AnnotationContainer container) {
		txtUtil.storeAnnotation(text, notesPath, videoName, author, time, container);
	}

	/**
	 * Edita uma anotacao textual.
	 * 
	 * @param annotation
	 * @param notesPath
	 * @param videoName
	 * @param author
	 * @param newText
	 */
	public void editTextAnnotation(TextAnnotationInfo annotation, String notesPath, String videoName, String author, String newText) {
		txtUtil.editAnnotationText(annotation, notesPath, videoName, author, newText);
	}

	/**
	 * Remove uma anotacao textual.
	 * 
	 * @param annotation
	 * @param notesPath
	 * @param videoName
	 * @param author
	 * @param container
	 */
	public void removeTextAnnotation(TextAnnotationInfo annotation, String notesPath, String videoName, String author, AnnotationContainer container) {
		txtUtil.removeAnnotation(annotation, notesPath, videoName, author, container);
	}
	
	public void deleteTextAnnotationFile(String notesPath, String videoName, String author, AnnotationContainer container) {
		txtUtil.deleteAnnotationFile(notesPath, videoName, author, container);
	}

	/**
	 * Retorna lista que contem contexto dos autores de anotacoes textuais.
	 * 
	 * @return Contexto dos autores de anotacoes textuais.
	 */
	public List<AuthorContext> getTextAnnotationAuthorContextInfo(String author) {
		return txtUtil.getAuthorContextInfo(author);
	}

	/**
	 * Verifica a existencia de um arquivo de anotacoes em tinta para
	 * determinados video e autor.
	 * 
	 * @param notesPath
	 * @param videoName
	 * @param author
	 * @return
	 */
	public boolean inkAnnotationExist(String notesPath, String videoName, String author) {
		return inkUtil.annotationsExist(notesPath, videoName, author);
	}

	/**
	 * Inicializa arquivo de anotacoes em tinta.
	 * 
	 * @param notesPath
	 * @param videoName
	 * @param author
	 * @param container
	 */
	public void initiateInkAnnotations(String notesPath, String videoName, String author, AnnotationContainer container) {
		inkUtil.initiateAnnotations(notesPath, videoName, author, container);
	}

	/**
	 * Configura o autor do arquivo de anotacoes em tinta, caso ainda nao
	 * exista.
	 * 
	 * @param user
	 * @param locManager
	 * @param geoCoder
	 * @param event
	 */
	public void setInkAnnotationsAuthor(String user, LocationManager locManager, Geocoder geoCoder, String event) {
		inkUtil.setAuthor(user, locManager, geoCoder, event);
	}

	/**
	 * Adiciona um autor de anotacoes em tinta.
	 * 
	 * @param author
	 * @param locManager
	 * @param geoCoder
	 * @param event
	 */
	public void addInkAnnotationsCoAuthor(String author, LocationManager locManager, Geocoder geoCoder, String event) {
		inkUtil.addAuthor(author, locManager, geoCoder, event);
	}

	/**
	 * Guarda a anotacao em tinta em um arquivo xml.
	 * 
	 * @param drawPath
	 * @param notesPath
	 * @param videoName
	 * @param author
	 * @param time
	 * @param container
	 */
	public void storeInkAnnotation(DrawAnnotationInfo drawPath, String notesPath, String videoName, String author, int time,
			AnnotationContainer container) {
		inkUtil.storeAnnotation(drawPath, notesPath, videoName, author, time, container);
	}

	/**
	 * Remove uma anotacao em tinta.
	 * 
	 * @param drawAnnotation
	 * @param notesPath
	 * @param videoName
	 * @param author
	 * @param container
	 */
	public void removeInkAnnotation(DrawAnnotationInfo drawAnnotation, String notesPath, String videoName, String author,
			AnnotationContainer container) {
		inkUtil.removeAnnotation(drawAnnotation, notesPath, videoName, author, container);
	}

	/**
	 * Retorna lista que contem contexto dos autores de anotacoes em tinta.
	 * 
	 * @return Contexto dos autores de anotacoes em tinta.
	 */
	public List<AuthorContext> getInkAnnotationAuthorContextInfo(String author) {
		return inkUtil.getAuthorContextInfo(author);
	}

	/**
	 * Permite realizar anotacoes em tinta.
	 */
	public void inkAnnotationOn() {
		AnnotationControl.setAddInk(Boolean.TRUE);
	}

	/**
	 * Desliga anotacoes em tinta.
	 */
	public void inkAnnotationOff() {
		AnnotationControl.setAddInk(Boolean.FALSE);
	}

	/**
	 * Verifica se anotacoes em tinta estao habilitadas.
	 * 
	 * @return True em caso positivo, false caso contrario.
	 */
	public Boolean isInkAnnotationOn() {
		return AnnotationControl.getAddInk();
	}
	
	/**
	 * Verifica a existencia de um arquivo de anotacoes em audio para
	 * determinados video e autor.
	 * 
	 * @param notesPath
	 * @param videoName
	 * @param author
	 * @return
	 */
	public boolean audioAnnotationExist(String notesPath, String videoName, String author) {
		return audioUtil.annotationsExist(notesPath, videoName, author);
	}
	
	/**
	 * Inicializa arquivo de anotacoes em audio.
	 * 
	 * @param notesPath
	 * @param videoName
	 * @param author
	 * @param container
	 */
	public void initiateAudioAnnotations(String notesPath, String videoName, String author, AnnotationContainer container) {
		audioUtil.initiateAnnotations(notesPath, videoName, author, container);
	}

	/**
	 * Configura o autor do arquivo de anotacoes em audio, caso ainda nao
	 * exista.
	 * 
	 * @param user
	 * @param locManager
	 * @param geoCoder
	 * @param event
	 */
	public void setAudioAnnotationsAuthor(String user, LocationManager locManager, Geocoder geoCoder, String event) {
		audioUtil.setAuthor(user, locManager, geoCoder, event);
	}

	/**
	 * Adiciona um autor de anotacoes em audio.
	 * 
	 * @param author
	 * @param locManager
	 * @param geoCoder
	 * @param event
	 */
	public void addAudioAnnotationsCoAuthor(String author, LocationManager locManager, Geocoder geoCoder, String event) {
		audioUtil.addAuthor(author, locManager, geoCoder, event);
	}
	
	/**
	 * Guarda a anotacao em audio em um arquivo xml.
	 * 
	 * @param audioFileName
	 * @param screensPath
	 * @param notesPath
	 * @param videoName
	 * @param author
	 * @param time
	 * @param container
	 */
	public void storeAudioAnnotation(String audioFileName, String notesPath, String videoName, String author, int time,
			AnnotationContainer container) {
		audioUtil.storeAnnotation(audioFileName, notesPath, videoName, author, time, container);
	}

	/**
	 * Remove uma anotacao em audio.
	 * 
	 * @param audioAnnotation
	 * @param notesPath
	 * @param videoName
	 * @param author
	 * @param container
	 */
	public void removeAudioAnnotation(AudioAnnotationInfo audioAnnotation, String notesPath, String videoName, String author,
			AnnotationContainer container) {
		audioUtil.removeAnnotation(audioAnnotation, notesPath, videoName, author, container);
	}

	/**
	 * Retorna lista que contem contexto dos autores de anotacoes em audio.
	 * 
	 * @return Contexto dos autores de anotacoes em audio.
	 */
	public List<AuthorContext> getAudioAnnotationAuthorContextInfo(String author) {
		return audioUtil.getAuthorContextInfo(author);
	}

	/**
	 * Permite realizar anotacoes de audio.
	 */
	public void audioAnnotationOn() {
		AnnotationControl.setAddAudio(Boolean.TRUE);
	}

	/**
	 * Desliga anotacoes em audio.
	 */
	public void audioAnnotationOff() {
		AnnotationControl.setAddAudio(Boolean.FALSE);
	}

	/**
	 * Verifica se anotacoes em audio estao habilitadas.
	 * 
	 * @return True em caso positivo, false caso contrario.
	 */
	public Boolean isAudioAnnotationOn() {
		return AnnotationControl.getAddAudio();
	}
	
	/**
	 * Envia anotacao textual por email.
	 * 
	 * @param notesPath
	 * @param videoName
	 * @param author
	 * @param context
	 * @param user 
	 */
	public void shareAnnotationViaApps(String notesPath, String videoName, String videoPath, String author, Context context, String user, long videoDurationMs) {		
		ShareAnnotation sharer = new ShareAnnotation();
		sharer.shareAnnotationViaApps(notesPath, videoName, videoPath, author, context, txtUtil, inkUtil, user, this, videoDurationMs);
	}

}
