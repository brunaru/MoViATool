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
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import br.usp.icmc.movia.R;
import br.usp.icmc.movia.annotation.ink.DrawAnnotation;
import br.usp.icmc.movia.annotation.text.TextAnnotation;

/**
 * Classe de utilidades de manipulacao de video.
 * 
 * @author Brunaru
 * 
 */
public class VideoUtil {
	
	public static final int METADATA_KEY_VIDEO_WIDTH_10 = 20;
	
	public static final String MP4_VIDEO_FORMAT = "mp4";
	
	public static final int TEXT_THUMBNAIL = 1;
	
	public static final int INK_THUMBNAIL = 2;
	
	public static final int AUDIO_THUMBNAIL = 3;

	public static final String DEFAULT_WIDTH = "1280";

	public static final String DEFAULT_HEIGHT = "720";
	
	public static final String DEFAULT_ROTATION = "0";
	
	public static final int THUMBNAIL_WIDTH = 120;

	public static final int THUMBNAIL_HEIGHT = 67;
	
	/**
	 * Captura tela do video.
	 * 
	 * @param time
	 *            Tempo em segundos do frame.
	 * @param videoPath
	 *            Caminho do arquivo de video.
	 * @param screensPath
	 *            Caminho de armazenamento das telas.
	 * @param videoName
	 *            Nome do video.
	 * @return Caminho da tela capturada.
	 */
	private static String captureScreen(int time, String videoPath, String screensPath, String videoName) {
		String screenName = "";
		/* Posicao eh em microsegundos. */
		long position = time*1000000;
		try {
			Bitmap bmp = getVideoFrameAndroid(videoPath, position);
			if(bmp == null) {
				bmp = getVideoFrameAndroid(videoPath, 0);
				if (bmp == null) {
					return null;
				}				
			}
			File folder = new File(screensPath);
			if (!folder.exists()) {
				folder.mkdir();
			}
			screenName = screensPath + videoName + time + ".jpg";
			FileOutputStream out = new FileOutputStream(screenName);
			bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return screenName;
	}
	
	/**
	 * Captura um bitmap do frame dado um video e a posicao do frame no tempo.
	 * 
	 * @param videoPath
	 *            Caminho do video.
	 * @param frameTime
	 *            Posicao temporal do frame.
	 * @return bitmap do frame capturado.
	 */
	private static Bitmap getVideoFrameAndroid(String videoPath, long frameTime) {
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try {
			retriever.setDataSource(videoPath);
			Bitmap frame = retriever.getFrameAtTime(frameTime);
			return frame;
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException();
		} catch (RuntimeException ex) {
			throw new RuntimeException();
		} finally {
			retriever.release();
		}
	}
	
	/**
	 * Recupera a altura original do video.
	 * 
	 * @param videoPath
	 *            Caminho do video.
	 * @return Altura do video.
	 */
	@SuppressLint("InlinedApi") 
	public static int getVideoHeight(String videoPath) {
		MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
		metaRetriever.setDataSource(videoPath);
		String height = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
		if (height == null) {
			height = DEFAULT_HEIGHT;			
		}
		int h = Integer.valueOf(height);
		return h;
	}
	
	/**
	 * Recupera a largura original do video.
	 * 
	 * @param videoPath
	 *            Caminho do video.
	 * @return Largura do video.
	 */
	@SuppressLint("InlinedApi") 
	public static int getVideoWidth(String videoPath) {
		MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
		metaRetriever.setDataSource(videoPath);
		String width = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
		if (VersionUtil.isIceCreamSandwichForward()) {
			width = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);			
		} else {
			width = metaRetriever.extractMetadata(METADATA_KEY_VIDEO_WIDTH_10);
		}
		if (width == null) {
			width = DEFAULT_WIDTH;
		}
		int w = Integer.valueOf(width);
		return w;
	}
	
	/**
	 * Recupera a rotacao original do video.
	 * 
	 * @param videoPath
	 *            Caminho do video.
	 * @return Rotacao do video.
	 */
	@SuppressLint("InlinedApi") 
	public static int getVideoRotation(String videoPath) {
		MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
		metaRetriever.setDataSource(videoPath);
		String rotation = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
		if (rotation == null) {
			rotation = DEFAULT_ROTATION;			
		}
		int r = Integer.valueOf(rotation);
		return r;
	}
	
	/**
	 * Formata uma duracao em segundo no formato H:MM:SS.
	 * 
	 * @param seconds
	 *            Duracao em segundos.
	 * @return Duracao formatada.
	 */
	@SuppressLint("DefaultLocale") 
	public static String formatSeconds(int seconds) {
		String result = String.format("%d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, (seconds % 60));
		return result;
	}
	
	/**
	 * Buscas os nomes de usuarios autores baseado nos arquivos de anotacao.
	 * 
	 * @param notesPath
	 *            Caminho dos arquivos de anotacoes.
	 * @param videoName
	 *            Nome do video.
	 * @return Lista com nome de autores.
	 */
	public static List<String> authorList(String notesPath, String videoName) {
		List<String> authorL = new ArrayList<String>();
		String fileName, authorName;
		File folder = new File(notesPath);
    	if (!folder.exists()) {
    		folder.mkdir();
    	}
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
			if (file.isFile()) {
				fileName = file.getName();
				fileName = fileName.toLowerCase(Locale.getDefault());
				if (fileName.endsWith(".xml") && fileName.contains(videoName.toLowerCase(Locale.getDefault()))) {
					TextAnnotation textAnnotation = null;
					DrawAnnotation drawAnnotation = null;
					Serializer serializer = new Persister();
					try {
						if (serializer.validate(TextAnnotation.class, file)) {
							textAnnotation = serializer.read(TextAnnotation.class, file);
							authorName = textAnnotation.getOriginalAuthor();
							authorL.add(authorName);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if (serializer.validate(DrawAnnotation.class, file)) {
							drawAnnotation = serializer.read(DrawAnnotation.class, file);
							authorName = drawAnnotation.getOriginalAuthor();
							authorL.add(authorName);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return authorL;
	}
	
	/**
	 * Lista todos os videos do diretorio.
	 * 
	 * @param videoDirectoryName
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> listVideos(String videoDirectoryName) throws Exception {
		File dir = new File(videoDirectoryName);
		Map<String, String> videoFileMap = new HashMap<String, String>();
		File[] files = dir.listFiles();
		listFiles(files, videoFileMap, MP4_VIDEO_FORMAT);
		return videoFileMap;
	}
	
	/**
	 * Lista todos os arquivos de um determinado formato.
	 * 
	 * @param files
	 * @param videoFileMap
	 * @param format
	 */
	private static void listFiles(File[] files, Map<String, String> videoFileMap, String format) {
		for (File file : files) {
			if (file.isDirectory()) {
				listFiles(file.listFiles(), videoFileMap, format);
			} else {
				String fileName = file.getName();
				int mid = fileName.lastIndexOf(".");
				if (fileName.substring(mid + 1).equalsIgnoreCase(format)) {
					String videoName = fileName.substring(0, mid);
					videoFileMap.put(file.getPath(), videoName);
				}
			}
		}
	}
	
	/**
	 * Recupera nome do arquivo (sem extensao).
	 * 
	 * @param videoPath
	 * @return
	 */
	public static String getVideoName(String videoPath) {
		int fileNameFirstIndex = videoPath.lastIndexOf(File.separator);
		int fileNameLastIndex = videoPath.lastIndexOf(".");
		String videoName = videoPath.substring(fileNameFirstIndex+1, fileNameLastIndex);
		return videoName;
	}
	
	/**
	 * Recupera nome do arquivo (com extensao).
	 * 
	 * @param videoPath
	 * @return
	 */
	public static String getVideoNameWithExtension(String videoPath) {
		int fileNameFirstIndex = videoPath.lastIndexOf(File.separator);
		String fullVideoName = videoPath.substring(fileNameFirstIndex+1);
		return fullVideoName;
	}
	
	/**
	 * Recupera caminho do diretorio em que o arquivo se localiza.
	 * 
	 * @param videoPath
	 * @return
	 */
	public static String getDirPath(String filePath) {
		int lastIndex = filePath.lastIndexOf(File.separator);
		String cleanPath = filePath.substring(0, lastIndex);
		return cleanPath;
	}
	
	/**
	 * Retorna quadro representativo.
	 * 
	 * @param videoPath caminho do diretorio de videos
	 * @param screensPath caminho do diretorio de capturas
	 * @param videoName nome do video
	 * @param time tempo em segundos
	 * @return quadro em bitmap.
	 */
	public static Bitmap getBitmap(String videoPath, String screensPath, String videoName, int time) {
		Bitmap bm = null;
		/* Se captura jah existe recupera. */
		String myJpgPath = screensPath + videoName + time + ".jpg";
		File jpgFile = new File(myJpgPath);
    	if (jpgFile.exists()) {
    		bm = BitmapFactory.decodeFile(myJpgPath);
    	}
    	/* Se nao existe, captura novo quadro. */
		if (bm == null) {
			myJpgPath = captureScreen(time, videoPath, screensPath, videoName);
			bm = BitmapFactory.decodeFile(myJpgPath);
		}
		/* Em caso de erro retorna null. */
		if (bm == null) {
			return null;
		}
		/* Ajusta o tamanho da miniatura. */
		Bitmap scaledBm = Bitmap.createScaledBitmap(bm, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, true);
		return scaledBm;
	}
	
	public static Bitmap getThumbnailBitmap(String videoPath, String screensPath, String videoName, int time, Context context, int type) {
		// String myJpgPath = screensPath + videoName + time + ".jpg";
		// File jpgFile = new File(myJpgPath);		
		Bitmap bm = getBitmap(videoPath, screensPath, videoName, time);
		
		/* Erro, bitmap nao pode ser gerado. */
		if (bm == null) {
			if (type == TEXT_THUMBNAIL) {
				bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.text);
			} else if (type == INK_THUMBNAIL) {
				bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.ink);
			} else {
				bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.audio);
			}
		}
		
		return bm;
	}

}
