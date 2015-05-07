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
package br.usp.icmc.movia.multi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.usp.icmc.movia.annotation.AnnotationContainer;
import br.usp.icmc.movia.util.VideoUtil;


/**
 * Classe responsavel pela area de exibicao de multiplas anotacoes.
 * @author Brunaru
 *
 */
public class MultiAnnotations {

	private static final int MAX_TIME = 10;

	private SparseArray<List<AuthorText>> show = new SparseArray<List<AuthorText>>();

	private List<Integer> times = new ArrayList<Integer>();

	public void checkTextAnnotation(int time, List<String> authors, AnnotationContainer container, String mainAuthor, 
			LinearLayout layout, Context context) {
		boolean refresh = false;
		refresh = clear(time);
		if (!times.contains(time)) {
			List<AuthorText> list = new ArrayList<AuthorText>();
			for (String author : authors) {
				if (!author.equalsIgnoreCase(mainAuthor)) {
					if (container.getTextAnnotationMap().get(author).containsKey(time)) {
						AuthorText at = new AuthorText();
						String text = container.getTextAnnotationMap().get(author).get(time).getText();
						at.setAuthor(author);
						at.setText(text);
						list.add(at);
					}
				}
			}
			if (!list.isEmpty()) {
				times.add(time);
				show.append(time, list);
				refresh = true;
			}
		}
		if (refresh) {
			refreshMultiView(layout, context);
		}
	}

	private boolean clear(int time) {
		int ref1 = time - MAX_TIME;
		int ref2 = time + MAX_TIME;
		boolean removed = false;
		List<Integer> removeList = new ArrayList<Integer>();
		for (Integer t : times) {
			if (t < ref1 || t > ref2) {
				removeList.add(t);
				removed = true;
			}
		}
		for (Integer rt : removeList) {
			times.remove(rt);
			show.remove(rt);
		}
		return removed;
	}

	private void refreshMultiView(LinearLayout layout, Context context) {
		if (!times.isEmpty()) {
			List<String> texts = new ArrayList<String>();
			Collections.sort(times);
			Collections.reverse(times);
			for (int acTime : times) {
				List<AuthorText> list = show.get(acTime);
				for (AuthorText at : list) {
					String text = at.getAuthor() + " - " + VideoUtil.formatSeconds(acTime) + ": " + at.getText();
					texts.add(text);
				}
			}
			reconstructView(texts, layout, context);
		} else {
			reconstructView(null, layout, context);
		}
	}

	private void reconstructView(List<String> texts, LinearLayout layout, Context context) {
		layout.removeAllViews();
		if (texts != null && !texts.isEmpty()) {
			for (String text : texts) {
				TextView textView = new TextView(context);
				textView.setText(text);				
				textView.setTextAppearance(context, android.R.style.TextAppearance_Large);
				textView.setTextColor(Color.BLACK);
				layout.addView(textView);
			}
		}
	}

}
