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
import java.util.SortedMap;
import java.util.TreeMap;

import br.usp.icmc.movia.annotation.audio.AudioAnnotationInfo;
import br.usp.icmc.movia.annotation.ink.DrawAnnotationInfo;
import br.usp.icmc.movia.annotation.text.TextAnnotationInfo;

/**
 * Contem os mapas de diferentes tipos de anotacoes.
 * 
 * @author Brunaru
 * 
 */
public class AnnotationContainer {

	/** Mapa tempo x anotacao textual. */
	private SortedMap<String, SortedMap<Integer, TextAnnotationInfo>> textAnnotationMap = new TreeMap<String, SortedMap<Integer, TextAnnotationInfo>>();

	/** Mapa tempo x anotacao em tinta. */
	private SortedMap<String, SortedMap<Integer, List<DrawAnnotationInfo>>> drawAnnotationMap = new TreeMap<String, SortedMap<Integer, List<DrawAnnotationInfo>>>();

	/** Mapa tempo x anotacao em audio. */
	private SortedMap<String, SortedMap<Integer, AudioAnnotationInfo>> audioAnnotationMap = new TreeMap<String, SortedMap<Integer, AudioAnnotationInfo>>();
	
	public AnnotationContainer(List<String> authors) {
		for (String author : authors) {
			textAnnotationMap.put(author, new TreeMap<Integer, TextAnnotationInfo>());
			drawAnnotationMap.put(author, new TreeMap<Integer, List<DrawAnnotationInfo>>());
			audioAnnotationMap.put(author, new TreeMap<Integer, AudioAnnotationInfo>());
		}		
	}
	
	public SortedMap<String, SortedMap<Integer, TextAnnotationInfo>> getTextAnnotationMap() {
		return textAnnotationMap;
	}

	public void setTextAnnotationMap(SortedMap<String, SortedMap<Integer, TextAnnotationInfo>> textAnnotationMap) {
		this.textAnnotationMap = textAnnotationMap;
	}

	public SortedMap<String, SortedMap<Integer, List<DrawAnnotationInfo>>> getDrawAnnotationMap() {
		return drawAnnotationMap;
	}

	public void setDrawAnnotationMap(SortedMap<String, SortedMap<Integer, List<DrawAnnotationInfo>>> drawAnnotationMap) {
		this.drawAnnotationMap = drawAnnotationMap;
	}
	
	public SortedMap<String, SortedMap<Integer, AudioAnnotationInfo>> getAudioAnnotationMap() {
		return audioAnnotationMap;
	}

	public void setAudioAnnotationMap(SortedMap<String, SortedMap<Integer, AudioAnnotationInfo>> audioAnnotationMap) {
		this.audioAnnotationMap = audioAnnotationMap;
	}

}
