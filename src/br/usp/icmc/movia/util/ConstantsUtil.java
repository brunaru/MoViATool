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

import java.io.File;

import android.os.Environment;

/**
 * Reune constantes compartilhadas por diferentes classes.
 * 
 * @author Brunaru
 * 
 */
public class ConstantsUtil {
	
	public static final int USER_CHOOSER_REQUEST_CODE = 1;
	
	public static final int RECORD_OR_ANNOTATE_REQUEST_CODE = 2;
	
	public static final int RECORD_VIDEO_REQUEST_CODE = 3;
	
	public static final int VIDEO_CHOOSER_REQUEST_CODE = 4;
	
	public static final int AUTHOR_CHOOSER_REQUEST_CODE = 5;
	
	public static final int CONFIRM_OPTIONS_REQUEST_CODE = 6;
	
	public static final int CONFIRM_EVENT_REQUEST_CODE = 7;
	
	public static final int PLAY_VIDEO_REQUEST_CODE = 8;
	
	public static final int ANNOTATE_OLD = 0;
	
	public static final int ANNOTATE_NEW = 1;
	
	public static final String ANNOTATION_TYPE = "anntype";
	
	public static final String USER_VAR = "user";
	
	public static final String ANNOTATION_TEXT = "text";
	
	public static final String ANNOTATION_INK = "ink";
	
	public static final String ANNOTATION_AUDIO = "audio";
	
	public static final String NOTES_PATH_VAR = "notesPath";
	
	public static final String VIDEO_NAME_VAR = "videoName";
	
	public static final String SCREENS_PATH_VAR = "screensPath";
	
	public static final String VIDEO_PATH_VAR = "videoPath";
	
	public static final String AUTHOR_VAR = "author";
	
	public static final String EVENT_VAR = "event";
	
	public static final String AUDIO_VAR = "audioOn";
	
	/** Caminho dos videos. */
	//public static final String VIDEOS_DIR_PATH = Environment.getExternalStorageDirectory().getPath() + "/Videos/";
	public static final String VIDEOS_DIR_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getPath() + File.separator;
	
	/** Tempo extra em segundos que a anotacao deve permanecer na tela. */
	public static final int ANNOTATION_EXTRA_TIME = 3;
	
	/** Divisor utilizado para calcular tempo aproximado de exibicao de texto baseado no numero de palavras lidas por minuto (WPM=200). */
	public static final int ANNOTATION_DIVISOR_WPS = (200/60);
	
	/** Tempo em segundos que a anotacao em tinta deve permanecer na tela. */
	public static final int DRAW_ANNOTATION_TIMER = 3;
	
	/** Conta tipo Google. */
	public static final String GOOGLE_ACCOUNT = "com.google";
	
	/** Email tipo Google. */
	public static final String GMAIL_ADDRESS = "@gmail.com";

	public static final String THUMBNAILS_PATH = "/thumbnails/";

	public static final String NOTES_PATH = "notes/";
	
	public static final String AUDIO_PATH = "audio/";
	
	/** Complemento para nome do arquivo de anotacoes em tinta. */
	public static final String DRAW_FILE_COMPL = "_draws_";
	
	/** Complemento para nome do arquivo de anotacoes em audio. */
	public static final String AUDIO_FILE_COMPL = "_audio_";
	
	/** Formato de gravacao para audio. */
	public static final String AUDIO_FORMAT = "3gp";
	
	/** código para requisitar a gravação de vídeo **/
	public static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	
	/** indica que a câmera será ligada para salvar uma imagem **/
	public static final int MEDIA_TYPE_IMAGE = 1;
	
	/** indica que a câmera será ligada para gravação de vídeo **/
	public static final int MEDIA_TYPE_VIDEO = 2;
	
	/** Nome do arquivo de preferencias. */
	public static final String PREFS_NAME = "myPrefsMoviaFile";

	public static final String NEW_VIDEO_FILE = "newvideoFileName";
	
	public static final String FULL_NEW_VIDEO_FILE = "fullNewvideoFileName";

	public static final int VIDEO_HIGH_QUALITY = 1;
	
	public static final int VIDEO_LOW_QUALITY = 0;

	public static final String NEW_VIDEO_QUALITY = "videoQuality";
	
	public static final String NOTES_FULL_PATH = ConstantsUtil.VIDEOS_DIR_PATH + ConstantsUtil.NOTES_PATH;

}
