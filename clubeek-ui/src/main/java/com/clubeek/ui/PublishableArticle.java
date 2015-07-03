package com.clubeek.ui;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.clubeek.model.Publishable;
import com.clubeek.model.IUnique;
import com.clubeek.ui.views.Navigation.ViewId;

/**
 * Trida zapouzdrujici informace nutne k publikovani clanku
 * 
 * @author Marek Svarc
 * 
 */
public class PublishableArticle {

	/* PRIVATE */

	/** Clanek urceny k publikovani */
	private Publishable article;

	/** Unikatni identifikator clanku */
	private int id;

	/** Identifikator komponenty pro zobrazeni detailu clanku */
	private ViewId view;

	/* public */

	public PublishableArticle(Publishable article, int id, ViewId view) {
		this.article = article;
		this.id = id;
		this.view = view;
	}

	/** Vraci clanek urceny pro publikovani */
	public Publishable getArticle() {
		return article;
	}

	/** Vraci unikatni identifikator clanku */
	public int getId() {
		return id;
	}

	/** Vraci identifikator komponenty pro zobrazeni detailu clanku */
	public ViewId getView() {
		return view;
	}

	public static <T extends Publishable & IUnique> void addArticlesToContainer(List<PublishableArticle> container,
			List<T> articles, ViewId view) {
		for (T item : articles) {
			container.add(new PublishableArticle(item, item.getId(), view));
		}
	}

	/**
	 * Set��d� �l�nky podle priority a datumu posledni upravy
	 */
	public static void sortArticlesByLastChange(List<PublishableArticle> container, final boolean ascending) {
		Collections.sort(container, new Comparator<PublishableArticle>() {

			@Override
			public int compare(PublishableArticle o1, PublishableArticle o2) {
				// porovnani dle priority
				int result = Boolean.compare(o1.article.getPriority(), o2.article.getPriority());
				// porovnani dle casu posledni zmeny
				if (result == 0)
					result = o1.article.getLastChangeDate().compareTo(o2.article.getLastChangeDate());
				
				// tridit sestupne
				if (!ascending)
					result *= -1;

				return result;
			}

		});
	}

}
