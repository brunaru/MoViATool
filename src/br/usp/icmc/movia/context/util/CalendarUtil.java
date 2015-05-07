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
import java.util.Date;
import java.util.List;

import br.usp.icmc.movia.util.ConstantsUtil;
import br.usp.icmc.movia.util.VersionUtil;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

/**
 * Classe de utilidades de eventos de calendario.
 * 
 * @author Brunaru
 * 
 */
public class CalendarUtil {

	private static final String CALENDARS_URI_LVL_FROYO_FORWARD = "content://com.android.calendar/calendars";

	private static final String CALENDARS_URI_LVL_FROYO_BACKWARD = "content://calendar/calendars";

	private static final String EVENTS_URI_LVL_FROYO_FORWARD = "content://com.android.calendar/events";

	private static final String EVENTS_URI_LVL_FROYO_BACKWARD = "content://calendar/events";

	private static final String CALENDAR_ACCOUNT_NAME = "account_name";
	private static final String CALENDAR_ACCOUNT_TYPE = "account_type";
	private static final String CALENDAR_OWNER_ACCOUNT = "ownerAccount";

	private static final String[] CALENDAR_PROJECTION = new String[] { "_id", // 0
	};

	private static final int CALENDAR_PROJECTION_ID_INDEX = 0;

	private static final String[] EVENTS_PROJECTION = new String[] { "title", // 0
			"dtstart", // 1
			"dtend" // 2
	};

	private static final int EVENTS_PROJECTION_TITLE_INDEX = 0;
	private static final int EVENTS_PROJECTION_DTSTART_INDEX = 1;
	private static final int EVENTS_PROJECTION_DTEND_INDEX = 2;

	/**
	 * Recupera os ultimos eventos marcados no calendario Google do usuario.
	 * 
	 * @param cr
	 *            ContentResolver.
	 * @param gmailUserName
	 *            Usuario google.
	 * @param numberOfEvents
	 *            Numero de eventos que deseja retornar.
	 * @return Lista com os eventos.
	 */
	public static List<String> getGoogleEvents(ContentResolver cr, String gmailUserName, int numberOfEvents) {
		long calID = getGoogleCalendar(cr, gmailUserName);
		return getGoogleCalendarLastEvents(cr, calID, numberOfEvents);
	}
	
	/**
	 * Retorna o (provavel) evento corrente do calendario Google.
	 * @param cr
	 * @param gmailUserName
	 * @return Evento corrente.
	 */
	public static String getGoogleCurrentEvent(ContentResolver cr, String gmailUserName) {
		long calID = getGoogleCalendar(cr, gmailUserName);
		return getGoogleCalendarCurrentEvent(cr, calID);
	}

	private static long getGoogleCalendar(ContentResolver cr, String gmailUserName) {
		try {
			Uri calendarsUri;
			if (VersionUtil.isFroyoForward()) {
				calendarsUri = Uri.parse(CALENDARS_URI_LVL_FROYO_FORWARD);
			} else {
				calendarsUri = Uri.parse(CALENDARS_URI_LVL_FROYO_BACKWARD);
			}
			String selection = "((" + CALENDAR_ACCOUNT_NAME + " = ?) AND (" + CALENDAR_ACCOUNT_TYPE + " = ?) AND (" + CALENDAR_OWNER_ACCOUNT + " = ?))";
			String userName = gmailUserName + ConstantsUtil.GMAIL_ADDRESS;
			String[] selectionArgs = new String[] { userName, ConstantsUtil.GOOGLE_ACCOUNT, userName };
			Cursor managedCursor = cr.query(calendarsUri, CALENDAR_PROJECTION, selection, selectionArgs, null);
			if (managedCursor.moveToFirst()) {
				long calID = managedCursor.getLong(CALENDAR_PROJECTION_ID_INDEX);
				return calID;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	private static List<String> getGoogleCalendarLastEvents(ContentResolver cr, long calID, int numberOfEvents) {
		List<String> events = new ArrayList<String>();
		try {
			Uri eventUri;
			if (VersionUtil.isFroyoForward()) {
				eventUri = Uri.parse(EVENTS_URI_LVL_FROYO_FORWARD);
			} else {
				eventUri = Uri.parse(EVENTS_URI_LVL_FROYO_BACKWARD);
			}
			Cursor eventCursor = cr.query(eventUri, EVENTS_PROJECTION, "calendar_id=" + calID, null, "dtstart DESC, dtend DESC");
			int n = 0;
			while (eventCursor.moveToNext() && n < numberOfEvents) {
				String title = eventCursor.getString(EVENTS_PROJECTION_TITLE_INDEX);
				Date dtStart = new Date(Long.parseLong(eventCursor.getString(EVENTS_PROJECTION_DTSTART_INDEX)));
				Date dtEnd = new Date(Long.parseLong(eventCursor.getString(EVENTS_PROJECTION_DTEND_INDEX)));
				events.add(title + " dtStart: " + dtStart + " dtEnd: " + dtEnd);
				n++;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}		
		return events;
	}

	private static String getGoogleCalendarCurrentEvent(ContentResolver cr, long calID) {
		try {
			Uri eventUri;
			if (VersionUtil.isFroyoForward()) {
				eventUri = Uri.parse(EVENTS_URI_LVL_FROYO_FORWARD);
			} else {
				eventUri = Uri.parse(EVENTS_URI_LVL_FROYO_BACKWARD);
			}
			Cursor eventCursor = cr.query(eventUri, EVENTS_PROJECTION, "calendar_id=" + calID, null, null);
			Boolean found = Boolean.FALSE;
			while (eventCursor.moveToNext() && found != Boolean.TRUE) {
				String title = eventCursor.getString(EVENTS_PROJECTION_TITLE_INDEX);
				Date dtStart = new Date(Long.parseLong(eventCursor.getString(EVENTS_PROJECTION_DTSTART_INDEX)));
				Date dtEnd = new Date(Long.parseLong(eventCursor.getString(EVENTS_PROJECTION_DTEND_INDEX)));
				if (dtEnd.after(new Date()) && dtStart.before(new Date())) {
					return title + ", " + dtStart;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}		
		return "";
	}

}
