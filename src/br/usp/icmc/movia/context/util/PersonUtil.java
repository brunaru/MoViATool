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

import java.util.ArrayList;
import java.util.List;

import br.usp.icmc.movia.util.ConstantsUtil;

import android.accounts.Account;
import android.accounts.AccountManager;

/**
 * Classe de utilidades de identificao de pessoas/autores.
 * 
 * @author Brunaru
 * 
 */
public class PersonUtil {
	
	/**
	 * Identifica usuarios com base em suas contas Google.
	 * 
	 * @param manager
	 * @return lista de usuarios
	 */
	public static List<String> getGoogleUsers(AccountManager manager) {
		List<String> users = new ArrayList<String>();
		Account[] list = manager.getAccounts();
		for (Account acc : list) {
			if (acc.type.equalsIgnoreCase(ConstantsUtil.GOOGLE_ACCOUNT)) {
				String user = acc.name;
				if (user.contains("@")) {
					user = user.substring(0, user.indexOf("@"));
					users.add(user);
				}
			}
		}
		return users;
	}

}
