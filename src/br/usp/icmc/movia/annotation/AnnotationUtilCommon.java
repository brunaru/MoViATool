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

import android.location.Geocoder;
import android.location.LocationManager;
import br.usp.icmc.movia.annotation.operators.ContextOperators;


public class AnnotationUtilCommon {

	public static AuthorContext completeAuthorContext(String author, String event, LocationManager manager, Geocoder geoCoder) {
		AuthorContext authorCtx = new AuthorContext();
		authorCtx.setUserName(author);
		authorCtx.setLocation(ContextOperators.getLocation(manager, geoCoder));
		authorCtx.setEvent(event);
		authorCtx.setDate(ContextOperators.getCurretDate());
		authorCtx.setDeviceApi(ContextOperators.getDeviceApiVersion());
		authorCtx.setDeviceModel(ContextOperators.getDeviceModel());
		return authorCtx;
	}

}
