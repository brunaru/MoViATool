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
package br.usp.icmc.movia.annotation;

import java.util.List;

import android.location.Geocoder;
import android.location.LocationManager;

public interface AnnotationUtil {

	/**
	 * Inicializa arquivo de anotacoes.
	 * 
	 * @param notesPath
	 * @param videoName
	 * @param author
	 * @param container
	 * @return
	 */
	public boolean initiateAnnotations(String notesPath, String videoName, String author, AnnotationContainer container);

	/**
	 * Verifica a existencia do arquivo de anotacao.
	 * 
	 * @param notesPath
	 * @param videoName
	 * @param author
	 * @return true se existe, false caso contrario.
	 */
	public boolean annotationsExist(String notesPath, String videoName, String author);

	/**
	 * Grava no arquivo xml.
	 * 
	 * @param notesPath
	 * @param videoName
	 * @param author
	 */
	public void writeFile(String notesPath, String videoName, String author);

	/**
	 * Configura o autor original das anotacoes e data de criacao no arquivo.
	 * 
	 * @param author
	 * @param manager
	 * @param geoCoder
	 * @param event
	 */
	public void setAuthor(String author, LocationManager manager, Geocoder geoCoder, String event);

	/**
	 * Adiciona um autor as anotacoes no arquivo.
	 * 
	 * @param author
	 * @param manager
	 * @param geoCoder
	 * @param event
	 */
	public void addAuthor(String author, LocationManager manager, Geocoder geoCoder, String event);

	/**
	 * Adiciona o contexto de um autor.
	 * 
	 * @param author
	 * @param manager
	 * @param geoCoder
	 * @param event
	 */
	public void addAuthorContext(String author, LocationManager manager, Geocoder geoCoder, String event);

	/**
	 * Retorna lista de contexto de autores.
	 * 
	 * @return Contexto de autores.
	 */
	public List<AuthorContext> getAuthorContextInfo(String author);
	
	/**
	 * Retorna o nome do arquivo de anotacoes.
	 * 
	 * @param notesPath caminho de armazenamento de anotacoes.
	 * @param videoName nome do video.
	 * @param author nome do autor.
	 * @return nome completo do arquivo de anotacoes.
	 */
	public String getAnnotationFileName(String notesPath, String videoName, String author);
}
