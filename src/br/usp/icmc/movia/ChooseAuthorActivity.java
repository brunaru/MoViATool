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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import br.usp.icmc.movia.annotation.operators.ContextOperators;
import br.usp.icmc.movia.util.ConstantsUtil;
import br.usp.icmc.movia.util.VideoUtil;

import br.usp.icmc.movia.R;

/**
 * Atividade de selecao de autor.
 * 
 * @author Brunaru
 * 
 */
public class ChooseAuthorActivity extends Activity {

	private ListView listView;

	private AuthorArrayAdapter adapterListView;
	
	private List<String> authors;
	
	private boolean initiated = Boolean.FALSE;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_author_page);
		TextView text = (TextView) findViewById(R.id.tListViewTitle);
		text.setText(R.string.chooseAuthor);
		setTitle(R.string.chooseAuthor);
		listView = (ListView) findViewById(R.id.listView);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		Intent data = getIntent();
		final String user = data.getStringExtra(ConstantsUtil.USER_VAR);
		
		authors = identifyAllAuthors();
		/*if (!authors.contains("janedoe")) {
			authors.add("janedoe");
		}*/
		final int size = authors.size();
		/* Retira usuario da lista de selecao. */
		if (authors.contains(user)) {
			authors.remove(user);
		}
		if (authors != null && !authors.isEmpty()) {
			String[] authorsArray = authors.toArray(new String[authors.size()]);
			int index = authors.indexOf(user);
			adapterListView = new AuthorArrayAdapter(this, authorsArray);
			listView.setAdapter(adapterListView);
			listView.setAdapter(adapterListView);
			listView.setItemChecked(index, true);
		} else {
			/* Se nao ha outros adiciona o usuario e prossegue. */
			String[] selectedAuthors = new String[1];
			selectedAuthors[0] = user;
			callOptionsIntent(selectedAuthors);
		}
		Button bOk = (Button) findViewById(R.id.buttonOk);
		bOk.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				String[] selectedAuthors = new String[size+1];
				int j = 0;
				for (int i = 0; i < listView.getChildCount(); i++) {
					CheckedTextView ctext = (CheckedTextView) listView.getChildAt(i);
					if(ctext.isChecked()) {
						String authorName = ctext.getText().toString();
						selectedAuthors[j] = authorName;
						j++;
					}
				}
				/* Adiciona usuario na lista a ser enviada. */
				selectedAuthors[j] = user;
				callOptionsIntent(selectedAuthors);
			}
		});
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        if (initiated) {
        	if (authors.isEmpty()) {
				finish();
			}
        }
        initiated = Boolean.TRUE;
    }
	
	private void callOptionsIntent(String[] selectedAuthors) {
		Intent optionsActivity = new Intent(this, OptionsActivity.class);
		Intent data = getIntent();
		optionsActivity.putExtra(ConstantsUtil.AUTHOR_VAR, selectedAuthors);		
		optionsActivity.putExtra(ConstantsUtil.VIDEO_NAME_VAR, data.getStringExtra(ConstantsUtil.VIDEO_NAME_VAR));
		optionsActivity.putExtra(ConstantsUtil.SCREENS_PATH_VAR, data.getStringExtra(ConstantsUtil.SCREENS_PATH_VAR));
		optionsActivity.putExtra(ConstantsUtil.NOTES_PATH_VAR, data.getStringExtra(ConstantsUtil.NOTES_PATH_VAR));
		optionsActivity.putExtra(ConstantsUtil.VIDEO_PATH_VAR, data.getStringExtra(ConstantsUtil.VIDEO_PATH_VAR));
		optionsActivity.putExtra(ConstantsUtil.USER_VAR, data.getStringExtra(ConstantsUtil.USER_VAR));
		startActivityForResult(optionsActivity, ConstantsUtil.CONFIRM_OPTIONS_REQUEST_CODE);
	}

	/**
	 * Identifica usuario com base em sua conta Google e donos de anotacoes e
	 * cria tela de selecao.
	 */
	private List<String> identifyAllAuthors() {
		AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
		List<String> authors = ContextOperators.getGoogleUsers(manager);
		Intent data = getIntent();
		String notesPath = data.getStringExtra("notesPath");
		String videoName = data.getStringExtra("videoName");
		List<String> authorsCreated = VideoUtil.authorList(notesPath, videoName);
		for (String newAuthor : authorsCreated) {
			if (!authors.contains(newAuthor)) {
				authors.add(newAuthor);
			}
		}
		return authors;
	}

}
