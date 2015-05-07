/*
 * Copyright 2013, 2014 Bruna C. Rodrigues da Cunha
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
package br.usp.icmc.movia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import br.usp.icmc.movia.annotation.AnnotationContainer;
import br.usp.icmc.movia.annotation.ink.DrawAnnotationInfo;
import br.usp.icmc.movia.annotation.operators.AuthoringOperators;
import br.usp.icmc.movia.annotation.text.TextAnnotationInfo;
import br.usp.icmc.movia.audio.recorder.AudioRecorder;
import br.usp.icmc.movia.multi.MultiAnnotations;
import br.usp.icmc.movia.util.ConstantsUtil;
import br.usp.icmc.movia.util.MediaControl;
import br.usp.icmc.movia.util.Util;
import br.usp.icmc.movia.util.VideoUtil;

/**
 * Classe responsavel pela reproducao e adicao de anotacoes.
 * 
 * @author Brunaru
 * 
 */
public class VideoActivity extends Activity {
	
	/** Tempo de ocorrencia do timed handler de anotacoes. */
	private static final int DELAY_TIME = 300;
	
	private String user, videoName, screensPath, notesPath, videoPath, event, mainAuthor;
	private List<String> authors = new ArrayList<String>();
	private String annotationText = null;
	private Button bAdd, bNavigation, bInk, bPlay, bRecordButton;
	private EditText editTextNote;
	private VideoViewCustom myVideoView;
	private TextView tAnnotation, tCurrent, tDuration;
	private DrawView myDrawView;
	private SeekBar seekBar;
	private Handler handlerTimer = new Handler();
	private Boolean paused = Boolean.FALSE;
	private Boolean textAnnotationsInitiated = Boolean.FALSE;
	private Boolean inkAnnotationsInitiated = Boolean.FALSE;
	private Boolean audioAnnotationsInitiated = Boolean.FALSE;
	private Boolean playAudioAnnotation = Boolean.TRUE;
	private Boolean addedAudio = Boolean.FALSE;
	private int annotationTime = 0;
	private int duration = 0;
	private int audioStarted = -1;
	private int already = -1;
	
	private AudioRecorder audioRecorder;
	
	private MultiAnnotations multi = new MultiAnnotations();
	
	private AuthoringOperators authoringOperators = new AuthoringOperators();
	private AnnotationContainer container;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initiateData();
        
        initiateMainAuthorChooser();
        
        initiateContainer();
        
        initiateAnnotations();
        
        configureEditTextArea();
        
        configureInkButton();
        
        configureAudioButton();
        
        configureAddAnnotationAction();
        
        initiateNavigationDialog();
        
        configureContextButton();
        
        configureShareButton();
        
        initiateVideoView();
        
    }
	
	@SuppressWarnings("deprecation")	
	@SuppressLint({ "InlinedApi", "NewApi" })	
	public void hideNavgationBar() {
		int currentapiVersion = Build.VERSION.SDK_INT;
		if (currentapiVersion > 10) { // Acima de Gingerbread
			View decorView = getWindow().getDecorView();
			int uiOptions;
			if(currentapiVersion < 14) { // ICS e adiante
				// Esconde status bar.
				uiOptions = View.STATUS_BAR_HIDDEN;
			} else {
				// Esconde navigation bar e status bar.
				uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE;
			}
			decorView.setSystemUiVisibility(uiOptions);			
		}
	}

	/**
	 * Inicializa os dados da atividade.
	 */
	private void initiateData() {
		Intent data = getIntent();
		authors.addAll(Arrays.asList(data.getStringArrayExtra(ConstantsUtil.AUTHOR_VAR)));
		authors.removeAll(Collections.singleton(null));
		mainAuthor = user = data.getStringExtra(ConstantsUtil.USER_VAR);
		videoName = data.getStringExtra(ConstantsUtil.VIDEO_NAME_VAR);
		screensPath = data.getStringExtra(ConstantsUtil.SCREENS_PATH_VAR);
		notesPath = data.getStringExtra(ConstantsUtil.NOTES_PATH_VAR);
		videoPath = data.getStringExtra(ConstantsUtil.VIDEO_PATH_VAR);
		event = data.getStringExtra(ConstantsUtil.EVENT_VAR);
		audioRecorder = new AudioRecorder(notesPath + ConstantsUtil.AUDIO_PATH + videoName + "/");
		playAudioAnnotation = data.getBooleanExtra(ConstantsUtil.AUDIO_VAR, Boolean.TRUE);
	}
	
	/**
	 * Inicializa o combo para escolher o autor principal exibido.
	 */
	private void initiateMainAuthorChooser() {
		Spinner spinnerAuthors = (Spinner) findViewById(R.id.spinnerAuthors);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, authors);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerAuthors.setAdapter(dataAdapter);
		int index = authors.indexOf(mainAuthor);
		spinnerAuthors.setSelection(index);
		spinnerAuthors.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				mainAuthor = parent.getItemAtPosition(pos).toString();
				Toast toast = Util.createToast(getApplicationContext(), getResources().getString(R.string.nowMainAuthor) + " " + mainAuthor, Toast.LENGTH_LONG);
				toast.show();
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// Nao faz nada			
			}
		});
	}
	
	/**
	 * Inicializa container de anotacoes.
	 */
	private void initiateContainer() {
		container = new AnnotationContainer(authors);
	}
	
	/**
	 * Carrega as anotacoes.
	 */
	private void initiateAnnotations() {
		initiateTextualAnnotations();
		initiateDrawableAnnotations();
		initiateAudioAnnotations();
	}
	
	/**
	 * Carrega as anotacoes textuais.
	 */
	private void initiateTextualAnnotations() {
		for (String author : authors) {
			if (authoringOperators.textAnnotationExist(notesPath, videoName, author)) {
				authoringOperators.initiateTextAnnotations(notesPath, videoName, author, container);
			}
		}
	}
	
	/**
	 * Cria arquivo de anotacoes textuais caso nao exista.
	 */
	private void createTextualAnnotations() {
		if (!authoringOperators.textAnnotationExist(notesPath, videoName, user)) {
			authoringOperators.setTextAnnotationsAuthor(user, (LocationManager) getSystemService(LOCATION_SERVICE), 
					new Geocoder(this), event);
		} else {
			authoringOperators.addTextAnnotationsCoAuthor(user, (LocationManager) getSystemService(LOCATION_SERVICE), 
					new Geocoder(this), event);
		}
	}
	
	/**
	 * Carrega as anotacoes de tinta.
	 */
	private void initiateDrawableAnnotations() {
		for (String author : authors) {
			if (authoringOperators.inkAnnotationExist(notesPath, videoName, author)) {
				authoringOperators.initiateInkAnnotations(notesPath, videoName, author, container);
			}
		}
	}
	
	/**
	 * Cria arquivo de anotacoes de tinta caso nao exista.
	 */
	private void createDrawableAnnotations() {
		if (!authoringOperators.inkAnnotationExist(notesPath, videoName, user)) {
			authoringOperators.setInkAnnotationsAuthor(user, (LocationManager) getSystemService(LOCATION_SERVICE), 
					new Geocoder(this), event);			
		} else {
			authoringOperators.addInkAnnotationsCoAuthor(user, (LocationManager) getSystemService(LOCATION_SERVICE), 
					new Geocoder(this), event);
		}
	}
	
	/**
	 * Carrega as anotacoes em audio.
	 */
	private void initiateAudioAnnotations() {
		for (String author : authors) {
			if (authoringOperators.audioAnnotationExist(notesPath, videoName, author)) {
				authoringOperators.initiateAudioAnnotations(notesPath, videoName, author, container);
			}
		}
	}
	
	/**
	 * Cria arquivo de anotacoes em audio caso nao exista.
	 */
	private void createAudioAnnotations() {
		if (!authoringOperators.audioAnnotationExist(notesPath, videoName, user)) {
			authoringOperators.setAudioAnnotationsAuthor(user, (LocationManager) getSystemService(LOCATION_SERVICE), 
					new Geocoder(this, Locale.getDefault()), event);
		} else {
			authoringOperators.addAudioAnnotationsCoAuthor(user, (LocationManager) getSystemService(LOCATION_SERVICE), 
					new Geocoder(this, Locale.getDefault()), event);
		}
	}

	/**
	 * Inicializa video e textos.
	 */
	private void initiateVideoView() {
		tAnnotation = (TextView) findViewById(R.id.tAnnotation);
		tAnnotation.setText("");
		myVideoView = (VideoViewCustom) findViewById(R.id.videoView1);
		MediaControl.setMediaControl(new MediaController(this));
		myVideoView.setMediaController(MediaControl.getMediaControl());
		setVideoSize();
		myVideoView.setVideoPath(videoPath);
		myVideoView.requestFocus();
		configureSeekBar();
		configurePlayButton();
		myVideoView.setOnCompletionListener(new OnCompletionListener() {			
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.start();
			}
		});
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			MediaControl.getMediaControl().setVisibility(View.INVISIBLE);
		}
		myVideoView.start();
	}	

	/**
	 * Redimensiona o tamanho do video quando o layout for retrato.
	 */
	private void setPortraitVideoSize() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int displayWidth = displaymetrics.widthPixels;
		int displayHeight = displaymetrics.heightPixels;
		int rotation = VideoUtil.getVideoRotation(videoPath);
		int videoHeight;
		int videoWidth ;
		if (rotation == 90) {
			/* Rotaciona. */
			videoHeight = VideoUtil.getVideoWidth(videoPath);
			videoWidth = VideoUtil.getVideoHeight(videoPath);
		} else {
			/* Normal. */
			videoHeight = VideoUtil.getVideoHeight(videoPath);
			videoWidth = VideoUtil.getVideoWidth(videoPath);
		}
		int newVideoWidth;
		int newVideoHeight;
		/* Se a largura foi maior que a altura faz o redimensionamento padrao. */
		if (videoWidth >= videoHeight) {
			newVideoWidth = displayWidth;
			newVideoHeight = (videoHeight * newVideoWidth) / videoWidth;
		} else {
			/* O tamanho maximo da altura vai ser metade do display. */
			newVideoHeight = displayHeight/2;
			newVideoWidth = (videoWidth * newVideoHeight) / videoHeight;
		}
		LayoutParams params = myVideoView.getLayoutParams();
		params.height = newVideoHeight;
		params.width = newVideoWidth;
		myVideoView.setDimensions(newVideoWidth, newVideoHeight);
		myVideoView.getHolder().setFixedSize(newVideoWidth, newVideoHeight);
		myVideoView.setLayoutParams(params);
		resizeDrawView(newVideoWidth, newVideoHeight);
		
	}
	
	/**
	 * Redimensiona o tamanho do video quando o layout for paisagem.
	 */
	private void setLandscapeVideoSize() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		
		int navpx = Math.round((48+5) * (displaymetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		
		int displayWidth = displaymetrics.widthPixels;
		int displayHeight = displaymetrics.heightPixels - navpx;
		int rotation = VideoUtil.getVideoRotation(videoPath);
		int videoHeight;
		int videoWidth;
		if (rotation == 90) {
			/* Rotaciona. */
			videoHeight = VideoUtil.getVideoWidth(videoPath);
			videoWidth = VideoUtil.getVideoHeight(videoPath);
		} else {
			/* Normal. */
			videoHeight = VideoUtil.getVideoHeight(videoPath);
			videoWidth = VideoUtil.getVideoWidth(videoPath);
		}
		int newVideoWidth;
		int newVideoHeight;
		/* Se a largura foi maior que a altura faz o redimensionamento padrao. */
		if (videoWidth >= videoHeight) {
			/* Calcula a proporcao de acordo com o aspect ratio. */
			float width = videoWidth;
			float height = videoHeight;
			int ar = Math.round((width/height));
			if (ar < 2) { // video meio quadrado
				newVideoHeight = displayHeight;
				newVideoWidth = (videoWidth * newVideoHeight) / videoHeight;
			} else { // video largo
				newVideoWidth = displayWidth;
				newVideoHeight = (videoHeight * newVideoWidth) / videoWidth;
			}
			if (newVideoHeight > displayHeight) {
				newVideoHeight = newVideoHeight - navpx;
				newVideoWidth = (videoWidth * newVideoHeight) / videoHeight;
			}			
		} else {
			newVideoHeight = displayHeight;
			newVideoWidth = (videoWidth * newVideoHeight) / videoHeight;
		}
		LayoutParams params = myVideoView.getLayoutParams();
		params.height = displayHeight;
		params.width = newVideoWidth;
		myVideoView.setDimensions(newVideoWidth, newVideoHeight);
		myVideoView.getHolder().setFixedSize(newVideoWidth, newVideoHeight);
		myVideoView.setLayoutParams(params);
		resizeDrawView(newVideoWidth, newVideoHeight);
	}
	
	/**
	 * Configura tamanho do video em ralacao a tela dos dispositivo.
	 */
	private void setVideoSize() {
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.mainLinear);
			layout.setVisibility(View.INVISIBLE);
			setLandscapeVideoSize();
			MediaControl.getMediaControl().setAnchorView(myVideoView);
		} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.mainLinear);
			layout.setVisibility(View.VISIBLE);
			setPortraitVideoSize();
			MediaControl.getMediaControl().setAnchorView(bNavigation);
		}
	}
	
	/**
     * Configura o comportamento da area de texto.
     */
	private void configureEditTextArea() {
		editTextNote = (EditText) findViewById(R.id.editTextNote);
		editTextNote.clearFocus();
		editTextNote.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility") @Override
			public boolean onTouch(View v, MotionEvent event) {
				if (myVideoView.isPlaying()) {
					myVideoView.pause();
					paused = Boolean.TRUE;
				}
				return false;
			}
		});
		editTextNote.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (hasFocus) {
					imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
				} else {
					imm.hideSoftInputFromWindow(editTextNote.getWindowToken(), 0); 
				}
			}
		});
		editTextNote.setOnEditorActionListener(new OnEditorActionListener() {			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))) {
					bAdd.requestFocus();
					bAdd.performClick();
				}
				return false;
			}
		});
	}
	
	/**
	 * Controle de anotacoes.
	 */
	private void configureAddAnnotationAction() {
		bAdd = (Button) findViewById(R.id.bAdd);
		bAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String text = editTextNote.getText().toString();
				if (!text.isEmpty()) {
					annotationText = text;
					editTextNote.setText("");
					editTextNote.clearFocus();
				} else {
					Toast toast = Util.createToast(getApplicationContext(), getResources().getString(R.string.addFirst), Toast.LENGTH_LONG);
					toast.show();
				}
			}
		});
	}
	
	/**
	 * Configura botao de adicao de anotacao em tinta.
	 */
	private void configureInkButton() {
		bInk = (Button) findViewById(R.id.bInk);
		bInk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (authoringOperators.isInkAnnotationOn()) {
					bInk.setText(R.string.inkOff);
					Drawable icon = getResources().getDrawable(R.drawable.pen_off);
					bInk.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
					authoringOperators.inkAnnotationOff();
					Toast toast = Util.createToast(getApplicationContext(), 
							getResources().getString(R.string.inkDrawOff), Toast.LENGTH_SHORT);
					if (paused) {
						myVideoView.start();
						paused = Boolean.FALSE;
					}
					toast.show();
				} else {
					bInk.setText(R.string.inkOn);
					Drawable icon = getResources().getDrawable(R.drawable.pen_on);
					bInk.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
					authoringOperators.inkAnnotationOn();
					Toast toast = Util.createToast(getApplicationContext(), 
							getResources().getString(R.string.inkDrawOn), Toast.LENGTH_SHORT);
					if (myVideoView.isPlaying()) {
						myVideoView.pause();
						paused = Boolean.TRUE;
					}
					toast.show();
				}
			}
		});
	}
	
	/**
	 * Configura botao de adicao de anotacao em audio.
	 */
	private void configureAudioButton() {
		bRecordButton = (Button) findViewById(R.id.bAudio);
		bRecordButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (authoringOperators.isAudioAnnotationOn()) {
					bRecordButton.setText(R.string.audioOff);
					Drawable icon = getResources().getDrawable(R.drawable.mic_off);
					bRecordButton.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
					authoringOperators.audioAnnotationOff();
					audioRecorder.onRecord(authoringOperators.isAudioAnnotationOn(), "0");
					if (audioStarted != -1) {
						addAudioNote(audioRecorder.getFullFileName(String.valueOf(audioStarted)), audioStarted);
					}
					Toast toast = Util.createToast(getApplicationContext(), 
							getResources().getString(R.string.audioOff), Toast.LENGTH_SHORT);
					toast.show();
					if (paused) {
						myVideoView.start();
						paused = Boolean.FALSE;
					}
					addedAudio = Boolean.TRUE;
				} else {
					bRecordButton.setText(R.string.audioOn);
					Drawable icon = getResources().getDrawable(R.drawable.mic_on);
					bRecordButton.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
					if (myVideoView.isPlaying()) {
						myVideoView.pause();
						paused = Boolean.TRUE;
					}
					authoringOperators.audioAnnotationOn();
					audioStarted = (myVideoView.getCurrentPosition() / 1000);
					audioRecorder.onRecord(authoringOperators.isAudioAnnotationOn(), String.valueOf(audioStarted));
					Toast toast = Util.createToast(getApplicationContext(), 
							getResources().getString(R.string.audioOn), Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		});
	}
	
	/**
	 * Guarda a anotacao em audio.
	 * 
	 * @param audioFileName
	 * @param time
	 */
	private void addAudioNote(String audioFileName, int time) {
		if (!audioAnnotationsInitiated) {
			createAudioAnnotations();
			audioAnnotationsInitiated = Boolean.TRUE;
		}
		authoringOperators.storeAudioAnnotation(audioFileName, notesPath, videoName, user, time, container);
	}
	
	/**
	 * Adiciona o texto anotado a cena de referencia, dada pelo tempo.
	 * 
	 * @param text
	 * @param time
	 */
	private void addTextNote(String text, int time) {
		if (!textAnnotationsInitiated) {
			createTextualAnnotations();
			textAnnotationsInitiated = Boolean.TRUE;
		}
		authoringOperators.storeTextAnnotation(text, notesPath, videoName, user, time, container);
		TextAnnotationInfo annotation = container.getTextAnnotationMap().get(user).get(time);
		/* Se eh o anotador principal adicional abaixo do video. */
		if (user.equalsIgnoreCase(mainAuthor)) {
			tAnnotation.setText(text);
			duration = annotation.getDuration();
			annotationTime = annotation.getTime();
		} else {
			
		}		
		if (paused) {
			myVideoView.start();
			paused = Boolean.FALSE;
		}
	}
	
	/**
	 * Verifica se ja existe uma anotacao textual neste intervalo de tempo.
	 * @param text
	 * @param time
	 */
	public void verifyAddText(final String text, final int time) {
		if (container.getTextAnnotationMap().get(user).containsKey(time)) {
			final AlertDialog alertDialog = new AlertDialog.Builder(VideoActivity.this).create();
			alertDialog.setTitle(R.string.overwrite);
			alertDialog.setMessage(getResources().getString(R.string.confirmOverwrite));
			alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					addTextNote(text, time);
					alertDialog.dismiss();
				}
			});
			alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					alertDialog.dismiss();
				}
			});
			alertDialog.show();
		} else {
			addTextNote(text, time);
		}
	}
	
	private void addDrawNote(DrawAnnotationInfo drawPath, int time) {
		if (!inkAnnotationsInitiated) {
			createDrawableAnnotations();
			inkAnnotationsInitiated = Boolean.TRUE;
		}
		authoringOperators.storeInkAnnotation(drawPath, notesPath, videoName, user, time, container);
	}
	
	/**
     * Inicia janela de navegacao por miniaturas.
     */
	private void initiateNavigationDialog() {
		bNavigation = (Button) findViewById(R.id.bNavigation);
		bNavigation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Navigation.showDialog(VideoActivity.this, myVideoView, notesPath, videoName, 
						mainAuthor, bNavigation, videoPath, screensPath, seekBar, container, 
						authoringOperators, editTextNote, user);
			}
		});
	}
	
	/**
	 * Configura o botao que exibe informacoes de contexto.
	 */
	private void configureContextButton() {
		Button bViewContextInfo = (Button) findViewById(R.id.bContextInfo);
		bViewContextInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ContextDialog.showContextDialog(VideoActivity.this, 
						authoringOperators.getTextAnnotationAuthorContextInfo(mainAuthor), 
						authoringOperators.getInkAnnotationAuthorContextInfo(mainAuthor),
						authoringOperators.getAudioAnnotationAuthorContextInfo(mainAuthor));
			}
		});
	}
	
	/**
	 * Configura o botao que compartilha anotacoes.
	 */
	private void configureShareButton() {
		Button bShare = (Button) findViewById(R.id.bShare);
		bShare.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				
				authoringOperators.shareAnnotationViaApps(notesPath, videoName, videoPath, mainAuthor, VideoActivity.this, user, myVideoView.getDuration());
			}
		});
	}
	
	/**
	 * Adiciona a area de anotacoes em tinta sobre o video.
	 * 
	 * @param width
	 * @param height
	 */
	private void addDrawView(int width, int height) {				
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.mainLay);
		myDrawView = new DrawView(VideoActivity.this, width, height, authoringOperators);
		RelativeLayout.LayoutParams relParams = new RelativeLayout.LayoutParams(width, height);
		relParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		myDrawView.setLayoutParams(relParams);
		layout.addView(myDrawView);
	}
	
	/**
	 * Redimensiona a area de anotacoes em tinta para ficar igual ao tamanho do video apresentado. 
	 * Esta situacao é dependente do layout (retrato ou paisagem).
	 * 
	 * @param width
	 * @param height
	 */
	private void resizeDrawView(int width, int height) {
		if (myDrawView == null) {
			addDrawView(width, height);
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myDrawView.getLayoutParams();
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
			myDrawView.setLayoutParams(params);
		} else {
			LayoutParams params = (LayoutParams) myDrawView.getLayoutParams();
			params.width = width;
			params.height = height;
			myDrawView.resize(width, height);
			myDrawView.setLayoutParams(params);
		}		
	}
	
	/**
	 * Configuracoes da seekbar.
	 */
	private void configureSeekBar() {
		/* Controle da seekbar. */
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (fromUser) {
					if (authoringOperators.isAudioAnnotationOn()) {
						bRecordButton.performClick();
					}
					myVideoView.seekTo(progress);
				}				
			}
		});
		/* Inicializa seekbar. */
		myVideoView.setOnPreparedListener(new OnPreparedListener() {
			public void onPrepared(MediaPlayer mp) {
				int duration = myVideoView.getDuration();
				tCurrent = (TextView) findViewById(R.id.tCurrent);
				tDuration = (TextView) findViewById(R.id.tDuration);
				tDuration.setText(VideoUtil.formatSeconds(duration / 1000));
				seekBar.setMax(duration);
				handlerTimer.postDelayed(update, DELAY_TIME);
			}
		});
	}
	
	/**
	 * Controle do botao de Play e Pause.
	 */
	private void configurePlayButton() {
		bPlay = (Button) findViewById(R.id.bPlay);
		bPlay.setText("Play / Pause");
		bPlay.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!myVideoView.isPlaying()) {
					if (authoringOperators.isAudioAnnotationOn()) {
						Toast toast = Util.createToast(getApplicationContext(), 
								getResources().getString(R.string.turnAudioOff), Toast.LENGTH_SHORT);
						toast.show();
					} else {
						myVideoView.start();
					}					
				} else {
					myVideoView.pause();
				}
			}
		});
	}
	
	/**
	 * Atualiza anotacoes.
	 */
	private Runnable update = new Runnable() {
		public void run() {
			int position = myVideoView.getCurrentPosition();
			seekBar.setProgress(position);
			int time = (position / 1000);
			tCurrent.setText(VideoUtil.formatSeconds(time));
			
			/* Anotacao textual. */
			if (annotationText != null) {
				verifyAddText(annotationText, time);
				annotationText = null;				
			}
			if (container.getTextAnnotationMap().get(mainAuthor).containsKey(time) 
					&& !container.getTextAnnotationMap().get(mainAuthor).get(time).getText().equals(tAnnotation.getText().toString())) {
				TextAnnotationInfo annotation = container.getTextAnnotationMap().get(mainAuthor).get(time);
				tAnnotation.setText(annotation.getText());
				duration = annotation.getDuration();
				annotationTime = annotation.getTime();
			} else {
				if (time >= (annotationTime+duration) || time < annotationTime) {
					tAnnotation.setText("");
				}
			}
			
			/* Anotacao textual de multiplos autores. */
			LinearLayout area = (LinearLayout) findViewById(R.id.linearMulti);
			multi.checkTextAnnotation(time, authors, container, mainAuthor, area, getApplicationContext());
			
			/* Anotacao em tinta. */
			if (myDrawView != null) {
				if (container.getDrawAnnotationMap().get(mainAuthor).containsKey(time)) {
					List<DrawAnnotationInfo> drawList = container.getDrawAnnotationMap().get(mainAuthor).get(time);
					List<Path> pathList = new ArrayList<Path>();
					for (DrawAnnotationInfo drawInfo : drawList) {
						pathList.add(drawInfo.getPath());
					}
					myDrawView.addTimedPath(pathList, time);
				}
				DrawAnnotationInfo drawPath = myDrawView.getCustomPath();
				myDrawView.setTime(time);
				myDrawView.removeTimedPath(time);
				myDrawView.postInvalidate();
				if (drawPath != null) {
					addDrawNote(drawPath, drawPath.getTime());
				}
			}
			
			/* Anotacao em audio. */
			if (already < time) {
				already = -1;
			}
			if (container.getAudioAnnotationMap().get(mainAuthor).containsKey(time)) {
				if (already != time && (playAudioAnnotation == Boolean.TRUE || addedAudio == Boolean.TRUE)) {
					myVideoView.pause();					
					audioRecorder.startPlaying(String.valueOf(time));
					already = time;	
					addedAudio = Boolean.FALSE;
					while (audioRecorder.isPlaying()) {
						/* Nao faz nada, espera audio terminar. */
					}
					myVideoView.start();
				}
			}
			
			if (myVideoView.isPlaying()) {
				bPlay.setText(R.string.pause);
			} else {
				bPlay.setText(R.string.play);
			}
			
			handlerTimer.postDelayed(update, DELAY_TIME);
		}
	};
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			MediaControl.getMediaControl().setVisibility(View.INVISIBLE);
		} else {
			MediaControl.getMediaControl().setVisibility(View.VISIBLE);
		}
		setVideoSize();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		audioRecorder.onPause();
	}

}
