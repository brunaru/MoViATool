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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	
	private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
	
	/**
	 * Retorna data atual formatada.
	 * @return Data atual.
	 */
	public static String getCurretDate() {
		return new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(new Date());
	}
	
	/**
	 * Retona a timestamp em ms.
	 * @param datestr
	 * @return long
	 */
	public static long dateToTimestampSec(String datestr) {
		try {
			Date date = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).parse(datestr);
			long timestamp = date.getTime();
			return timestamp / 1000;
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}		
	}

}
