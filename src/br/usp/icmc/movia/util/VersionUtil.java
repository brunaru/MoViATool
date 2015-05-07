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
package br.usp.icmc.movia.util;

import android.os.Build;

/**
 * Classe de utilidades de identificao de versoes do sistema Android.
 * 
 * @author Brunaru
 * 
 */
public class VersionUtil {

	/**
	 * Ice Cream Sawdwich Version 4.0.X.
	 */
	public static final int ICE_CREAM_SANDWICH_API_LVL = 14;
	
	/**
	 * Gingerbread API Level 10 Version 2.3.3 - 2.3.7.
	 */
	public static final int GINGERBREAD_API_LVL_10 = 10;

	/**
	 * Froyo Version 2.2.
	 */
	public static final int FROYO_API_LVL = 8;

	/**
	 * Verfica se o API Level eh no minimo 14 (Ice Cream Sandwich).
	 * 
	 * @return true em caso positivo, falso caso contrário.
	 */
	public static Boolean isIceCreamSandwichForward() {
		int currentapiVersion = Build.VERSION.SDK_INT;
		if (currentapiVersion >= ICE_CREAM_SANDWICH_API_LVL) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	/**
	 * Verfica se o API Level eh no minimo 10 (Gingerbread 2.3.3).
	 * 
	 * @return true em caso positivo, falso caso contrário.
	 */
	public static Boolean isGingerbread10Forward() {
		int currentapiVersion = Build.VERSION.SDK_INT;
		if (currentapiVersion >= GINGERBREAD_API_LVL_10) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Verfica se o API Level eh no minimo 8 (Froyo).
	 * 
	 * @return true em caso positivo, falso caso contrário.
	 */
	public static Boolean isFroyoForward() {
		int currentapiVersion = Build.VERSION.SDK_INT;
		if (currentapiVersion >= FROYO_API_LVL) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

}
