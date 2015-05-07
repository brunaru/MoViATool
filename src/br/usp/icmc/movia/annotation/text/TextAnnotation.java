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

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import br.usp.icmc.movia.annotation.AuthorContext;

@Root
public class TextAnnotation {
	
	@Attribute
	private String originalAuthor;
	
	@Attribute 
	private String creationdate;
	
	@Attribute
	private String lastModified;
	
	@ElementList
	private List<TextAnnotationInfo> annotationList = new ArrayList<TextAnnotationInfo>();
	
	@ElementList
	private List<AuthorContext> authorsCtxList = new ArrayList<AuthorContext>();

	public List<TextAnnotationInfo> getAnnotationList() {
		return annotationList;
	}

	public void setAnnotationList(List<TextAnnotationInfo> annotationList) {
		this.annotationList = annotationList;
	}

	public List<AuthorContext> getAuthorsCtxList() {
		return authorsCtxList;
	}

	public void setAuthorsCtxList(List<AuthorContext> authorsCtxList) {
		this.authorsCtxList = authorsCtxList;
	}

	public String getOriginalAuthor() {
		return originalAuthor;
	}

	public void setOriginalAuthor(String originalAuthor) {
		this.originalAuthor = originalAuthor;
	}
	
	public void clear() {
		originalAuthor = null;
		annotationList.clear();
		authorsCtxList.clear();
	}

	public String getCreationdate() {
		return creationdate;
	}

	public void setCreationdate(String creationdate) {
		this.creationdate = creationdate;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}
	
}
