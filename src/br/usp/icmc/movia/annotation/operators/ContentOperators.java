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

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import br.usp.icmc.movia.annotation.AnnotationContainer;

public class ContentOperators {

	Set<Integer> listTextAnnotationsMoments(AnnotationContainer container) {
		Set<Integer> textMoments = new TreeSet<Integer>();
		for (String author : container.getTextAnnotationMap().keySet()) {
			textMoments.addAll(container.getTextAnnotationMap().get(author).keySet());
		}
		return textMoments;
	}

	Set<Integer> listInkAnnotationsMoments(AnnotationContainer container) {
		Set<Integer> inkMoments = new TreeSet<Integer>();
		for (String author : container.getTextAnnotationMap().keySet()) {
			inkMoments.addAll(container.getDrawAnnotationMap().get(author).keySet());
		}
		return inkMoments;
	}

	Set<Integer> listAudioAnnotationsMoments(AnnotationContainer container) {
		Set<Integer> audioMoments = new TreeSet<Integer>();
		for (String author : container.getTextAnnotationMap().keySet()) {
			audioMoments.addAll(container.getAudioAnnotationMap().get(author).keySet());
		}
		return audioMoments;
	}

	Set<Integer> listTextAndInkAnnotationsMoments(AnnotationContainer container) {
		SortedSet<Integer> textAndInkMoments = new TreeSet<Integer>();
		textAndInkMoments.addAll(listTextAnnotationsMoments(container));
		textAndInkMoments.addAll(listInkAnnotationsMoments(container));
		return textAndInkMoments;
	}

	Set<Integer> listTextAndAudioAnnotationsMoments(AnnotationContainer container) {
		SortedSet<Integer> textAndAudioMoments = new TreeSet<Integer>();
		textAndAudioMoments.addAll(listTextAnnotationsMoments(container));
		textAndAudioMoments.addAll(listAudioAnnotationsMoments(container));
		return textAndAudioMoments;
	}

	Set<Integer> listAudioAndInkAnnotationsMoments(AnnotationContainer container) {
		SortedSet<Integer> audioAndInkMoments = new TreeSet<Integer>();
		audioAndInkMoments.addAll(listInkAnnotationsMoments(container));
		audioAndInkMoments.addAll(listAudioAnnotationsMoments(container));
		return audioAndInkMoments;
	}

	Set<Integer> listAllAnnotationsMoments(AnnotationContainer container) {
		SortedSet<Integer> allInteractionMoments = new TreeSet<Integer>();
		allInteractionMoments.addAll(listTextAnnotationsMoments(container));
		allInteractionMoments.addAll(listInkAnnotationsMoments(container));
		allInteractionMoments.addAll(listAudioAnnotationsMoments(container));
		return allInteractionMoments;
	}

}
