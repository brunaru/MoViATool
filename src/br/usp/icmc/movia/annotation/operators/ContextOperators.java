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

import br.usp.icmc.movia.context.util.CalendarUtil;
import br.usp.icmc.movia.context.util.DateUtil;
import br.usp.icmc.movia.context.util.DeviceUtil;
import br.usp.icmc.movia.context.util.LocationUtil;
import br.usp.icmc.movia.context.util.PersonUtil;

import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.location.Geocoder;
import android.location.LocationManager;

public class ContextOperators {

	/**
	 * Recupera o evento mais proximo do horario atual na agenda Google.
	 * 
	 * @param cr
	 * @param gmailUserName
	 * @return evento
	 */
	public static String getEvent(ContentResolver cr, String gmailUserName) {
		return CalendarUtil.getGoogleCurrentEvent(cr, gmailUserName);
	}
	
	/**
	 * Recupera os ultimos n eventos mais recentes da agenda Google.
	 * 
	 * @param cr
	 * @param gmailUserName
	 * @return Ultimos eventos.
	 */
	public static String getEvents(ContentResolver cr, String gmailUserName, int n) {
		List<String> events = CalendarUtil.getGoogleEvents(cr, gmailUserName, n);
		if (events.isEmpty()) {
			return "";
		}
		String event = events.get(0);
		return event;
	}

	/**
	 * Recupera a localizacao atual do usuario.
	 * 
	 * @param manager
	 * @param geoCoder
	 * @return Localizacao.
	 */
	public static String getLocation(LocationManager manager, Geocoder geoCoder) {
		return LocationUtil.getFormattedLocation(manager, geoCoder);
	}

	/**
	 * Recupera os usuarios Google cadastrados no dispositivo.
	 * 
	 * @param manager
	 * @return Usuarios.
	 */
	public static List<String> getGoogleUsers(AccountManager manager) {
		return PersonUtil.getGoogleUsers(manager);
	}

	/**
	 * Retorna API do dispositivo.
	 * 
	 * @return Versao da API.
	 */
	public static String getDeviceApiVersion() {
		return DeviceUtil.getCurrentApiVersion();
	}

	/**
	 * Retorna modelo e respectivo fabricante do dispositivo.
	 * 
	 * @return Modelo e fabricante do dispositivo.
	 */
	public static String getDeviceModel() {
		return DeviceUtil.getDeviceModel();
	}

	/**
	 * Retorna string da data/hora atual.
	 * 
	 * @return Data atual.
	 */
	public static String getCurretDate() {
		return DateUtil.getCurretDate();
	}

}
