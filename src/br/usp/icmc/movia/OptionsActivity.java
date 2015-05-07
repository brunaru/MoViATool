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
import android.widget.RadioButton;
import br.usp.icmc.movia.util.ConstantsUtil;

import br.usp.icmc.movia.R;

/**
 * Opcoes de anotacoes (audio on e off).
 * 
 * @author Brunaru
 *
 */
public class OptionsActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);

		Button bOk = (Button) findViewById(R.id.buttonOk);
		bOk.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				RadioButton rbAudioYes = (RadioButton) findViewById(R.id.radioAudioYes);
				Boolean audioOn = Boolean.FALSE;
				if (rbAudioYes.isChecked()) {
					audioOn = Boolean.TRUE;
				}
				callVideoIntent(audioOn);
			}
		});
	}
	
	private void callVideoIntent(Boolean audioOn) {
		Intent eventActivity = new Intent(this, ConfirmEventActivity.class);
		Intent data = getIntent();
		eventActivity.putExtra(ConstantsUtil.AUTHOR_VAR, data.getStringArrayExtra(ConstantsUtil.AUTHOR_VAR));		
		eventActivity.putExtra(ConstantsUtil.VIDEO_NAME_VAR, data.getStringExtra(ConstantsUtil.VIDEO_NAME_VAR));
		eventActivity.putExtra(ConstantsUtil.SCREENS_PATH_VAR, data.getStringExtra(ConstantsUtil.SCREENS_PATH_VAR));
		eventActivity.putExtra(ConstantsUtil.NOTES_PATH_VAR, data.getStringExtra(ConstantsUtil.NOTES_PATH_VAR));
		eventActivity.putExtra(ConstantsUtil.VIDEO_PATH_VAR, data.getStringExtra(ConstantsUtil.VIDEO_PATH_VAR));
		eventActivity.putExtra(ConstantsUtil.USER_VAR, data.getStringExtra(ConstantsUtil.USER_VAR));
		eventActivity.putExtra(ConstantsUtil.AUDIO_VAR, audioOn);
		startActivityForResult(eventActivity, ConstantsUtil.CONFIRM_EVENT_REQUEST_CODE);
	}

}
