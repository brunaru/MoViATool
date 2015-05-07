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

import java.util.List;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import br.usp.icmc.movia.annotation.operators.ContextOperators;
import br.usp.icmc.movia.util.ConstantsUtil;

import br.usp.icmc.movia.R;

/**
 * Atividade de selecao de usuario.
 * 
 * @author Brunaru
 * 
 */
public class ChooseUserActivity extends Activity implements OnItemClickListener {

	private ListView listView;

	private UserArrayAdapter adapterListView;
	
	private List<String> authors;
	
	private boolean initiated = Boolean.FALSE;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_page);
		TextView text = (TextView) findViewById(R.id.tListViewTitle);
		text.setText(R.string.chooseUser);
		setTitle(R.string.chooseUser);
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(this);

		authors = identifyGoogleUsers();
		// authors.add("janedoe");
		// authors.add("johndoe");
		if (authors != null && !authors.isEmpty()) {
			if (authors.size() > 1) {
				String[] authorsArray = authors.toArray(new String[authors.size()]);
				adapterListView = new UserArrayAdapter(this, authorsArray);
				listView.setAdapter(adapterListView);
				listView.setAdapter(adapterListView);
			} else {
				videoRecordOrSelect(authors.get(0));
			}			
		}
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        if (initiated) {
        	if (authors.size() == 1) {
				finish();
			}
        }
        initiated = Boolean.TRUE;
    }

	/**
	 * Identifica usuario com base em sua conta Google.
	 */
	private List<String> identifyGoogleUsers() {
		AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
		List<String> authors = ContextOperators.getGoogleUsers(manager);
		return authors;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		String userName = (String) adapterListView.getItem(position);
		videoRecordOrSelect(userName);
	}
	
	private void videoRecordOrSelect(String userName) {
		int type = getIntent().getIntExtra(ConstantsUtil.ANNOTATION_TYPE, 0);
		if (type == ConstantsUtil.ANNOTATE_OLD) {
			final Intent chooseVideoIntent = new Intent(this, ChooseVideoActivity.class);
			chooseVideoIntent.putExtra(ConstantsUtil.USER_VAR, userName);
			startActivityForResult(chooseVideoIntent, ConstantsUtil.VIDEO_CHOOSER_REQUEST_CODE);	
		} else {
			final Intent recordVideoIntent = new Intent(this, RecordVideoActivity.class);
			recordVideoIntent.putExtra(ConstantsUtil.USER_VAR, userName);
			startActivityForResult(recordVideoIntent, ConstantsUtil.RECORD_VIDEO_REQUEST_CODE);
		}
	}

}
