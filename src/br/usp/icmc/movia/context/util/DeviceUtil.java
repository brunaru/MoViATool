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
package br.usp.icmc.movia.context.util;

import android.os.Build;

public class DeviceUtil {

	/**
	 * Retorna versao do corrente da API do dispositivo.
	 * 
	 * @return versao da api
	 */
	public static String getCurrentApiVersion() {
		return String.valueOf(Build.VERSION.SDK_INT);
	}
	
	/**
	 * Retorna modelo concatenado com o fabricante do dispositivo.
	 * @return fabricante + modelo do dispositivo.
	 */
	public static String getDeviceModel() {
		String model = Build.MODEL;
		String manufacturer = Build.MANUFACTURER;
		return manufacturer + " - " + model;
	}

}
