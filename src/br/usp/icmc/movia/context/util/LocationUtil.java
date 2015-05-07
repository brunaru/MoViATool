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

import java.util.List;

import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

/**
 * Classe de utilidades de identificao de localizacao.
 * 
 * @author Brunaru
 * 
 */
public class LocationUtil {

	/**
	 * Recupera localizacao recente.
	 * 
	 * @param manager
	 *            LocationManager.
	 * @param geoCoder
	 *            Geocoder.
	 * @return Endereco.
	 */
	private static Address getLocation(LocationManager manager, Geocoder geoCoder) {
		try {
			Criteria criteria = new Criteria();
			String locProvider = manager.getBestProvider(criteria, true);
			Location location = manager.getLastKnownLocation(locProvider);
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			List<Address> addresses = geoCoder.getFromLocation(lat, lng, 1);
			if (addresses != null && !addresses.isEmpty()) {
				Address address = addresses.get(0);
				return address;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Retorna o endereco na forma: cidade - estado - pais, rua/via.
	 * 
	 * @param manager
	 * @param geoCoder
	 * @return Endereco
	 */
	public static String getFormattedLocation(LocationManager manager, Geocoder geoCoder) {
		Address address = LocationUtil.getLocation(manager, geoCoder);
		if (address == null) {
			return " ";
		}
		String locality = address.getSubAdminArea();
		String admin = address.getAdminArea();
		String countryName = address.getCountryName();
		String thoroughfare = address.getThoroughfare();
		String strAdd = locality + " - " + admin + " - " + countryName + ", " + thoroughfare;
		return strAdd;
	}

}
