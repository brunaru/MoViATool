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
package br.usp.icmc.movia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import br.usp.icmc.movia.annotation.operators.ContextOperators;
import br.usp.icmc.movia.util.ConstantsUtil;

import br.usp.icmc.movia.R;

/**
 * Atividade para confirmacao do evento. A ideia eh garantir que o evento
 * capturado da agenda realmente eh o evento que o usuario esta participando
 * tambem.
 * 
 * @author Brunaru
 * 
 */
public class ConfirmEventActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm_event);

		final Intent data = getIntent();
		final String event = ContextOperators.getEvent(getContentResolver(), data.getStringExtra(ConstantsUtil.AUTHOR_VAR));

		if (event.isEmpty()) {
			startVideoActivity(data, "");
		}

		TextView tEvent = (TextView) findViewById(R.id.tEventName);
		tEvent.setText(event);

		Button bOk = (Button) findViewById(R.id.bEventOk);
		Button bNok = (Button) findViewById(R.id.bEventNok);

		bOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startVideoActivity(data, event);
			}
		});

		bNok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startVideoActivity(data, "");
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		final Intent data = getIntent();
		final String event = ContextOperators.getEvent(getContentResolver(), data.getStringExtra(ConstantsUtil.AUTHOR_VAR));
		if (event.isEmpty()) {
			finish();
		}
	}

	private void startVideoActivity(Intent data, String event) {
		Intent videoActivity = new Intent(this, VideoActivity.class);
		videoActivity.putExtra(ConstantsUtil.EVENT_VAR, event);
		videoActivity.putExtra(ConstantsUtil.AUTHOR_VAR, data.getStringArrayExtra(ConstantsUtil.AUTHOR_VAR));
		videoActivity.putExtra(ConstantsUtil.VIDEO_NAME_VAR, data.getStringExtra(ConstantsUtil.VIDEO_NAME_VAR));
		videoActivity.putExtra(ConstantsUtil.SCREENS_PATH_VAR, data.getStringExtra(ConstantsUtil.SCREENS_PATH_VAR));
		videoActivity.putExtra(ConstantsUtil.NOTES_PATH_VAR, data.getStringExtra(ConstantsUtil.NOTES_PATH_VAR));
		videoActivity.putExtra(ConstantsUtil.VIDEO_PATH_VAR, data.getStringExtra(ConstantsUtil.VIDEO_PATH_VAR));
		videoActivity.putExtra(ConstantsUtil.USER_VAR, data.getStringExtra(ConstantsUtil.USER_VAR));
		videoActivity.putExtra(ConstantsUtil.AUDIO_VAR, data.getBooleanExtra(ConstantsUtil.AUDIO_VAR, Boolean.TRUE));
		startActivityForResult(videoActivity, ConstantsUtil.PLAY_VIDEO_REQUEST_CODE);
	}

}
