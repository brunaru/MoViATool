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

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Classe de utilidades diversas.
 * @author Brunaru
 *
 */
public class Util {
	
	/**
	 * Cria toast padronizado.
	 * @param context
	 * @param text
	 * @param length
	 * @return toast
	 */
	@SuppressLint("ShowToast")
	public static Toast createToast(Context context, String text, int length) {
		Toast toast = Toast.makeText(context, text, length);
		toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
		return toast;
	}

}
