package controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import models.Actor;
import models.Movie;
import models.Director;
import models.MovieTemp;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Movies extends Controller {

	public static void ShowCreateMovie(String key) {
		if(key != "")
			render("Movies/CreateMovie.html", key);
		else
			render("Movies/CreateMovie.html");
	}

	public static void resultAlloCine(
			@Required(message = "Veuillez renseiger le titre du film.") String Title) {

		if (validation.hasErrors())
			render("Movies/CreateMovie.html");

		String[] splitTitle = Title.split(" ");
		String TitleForUrl = "";
		if (splitTitle.length > 1) {
			for (int j = 0; j < splitTitle.length; j++) {
				TitleForUrl += splitTitle[j];
				if (j < splitTitle.length - 1)
					TitleForUrl += "+";
			}
		} else
			TitleForUrl = Title;

		ArrayList<MovieTemp> lstMovieRsltAlloCine = new ArrayList<MovieTemp>();

		if (hasResultAlloCine(TitleForUrl)) {
			int nbPage = getNbPageResultAlloCine(TitleForUrl);
			for (int k = 0; k < nbPage; k++) {
				Element element = getElementByPageAllocine(TitleForUrl, k + 1);
				Elements elements = element.select("tr");

				for (int i = 0; i < elements.size(); i += 2) {
					String image = elements.get(i).select("td").first()
							.select("img").first().absUrl("src");

					// exclusion des films n'ayant pas d'images
					if (!image
							.equals("http://fr.web.img4.acsta.net/r_75_106/commons/emptymedia/Affichette_Recherche.gif")) {
						String title = elements.get(i).select("td").get(1)
								.select("a").first().text();
						int annee;
						String[] splitSpace = elements.get(i).select("td")
								.get(1).getElementsByClass("fs11").text()
								.split(" ");
						if (!elements.get(i).select("td").get(1)
								.getElementsByClass("fs11").text().isEmpty()
								&& !splitSpace[0].equals("de")
								&& !splitSpace[0].equals("avec")) {
							annee = Integer.parseInt(splitSpace[0]);
						} else
							annee = 0;
						String[] splitDe = elements.get(i).select("td").get(1)
								.getElementsByClass("fs11").text()
								.split(" de ");
						String[] splitAvec = elements.get(i).select("td")
								.get(1).getElementsByClass("fs11").text()
								.split(" avec ");

						String director;
						if (splitDe.length > 1 && splitAvec.length > 1)
							director = splitDe[1].split(" avec ")[0].split(",")[0];
						else if (elements.get(i).select("td").get(1)
								.getElementsByClass("fs11").text()
								.split(" de ").length == 1)
							director = "";
						else
							director = splitDe[1].split(",")[0];

						String actor;
						if (splitAvec.length > 1)
							actor = splitAvec[1].split(",")[0];
						else
							actor = "";

						String linkAlloCine = elements.get(i).select("td")
								.get(1).select("a").first().absUrl("href");

						// uniquement si l'acteur, le réalisateur et l'année non vide
						if (!actor.isEmpty() && !director.isEmpty()
								&& annee != 0) {
							MovieTemp newMovieTemp = new MovieTemp(title,
									image, annee, actor, director, linkAlloCine);
							lstMovieRsltAlloCine.add(newMovieTemp);
						}
					}
				}
			}
			if (lstMovieRsltAlloCine.isEmpty()) {
				validation.addError("Title",
						"Il n'y a pas de propositions d'ajout pour ce titre.");
				if (validation.hasErrors())
					render("Movies/CreateMovie.html");
			}
			render("Movies/CreateMovie.html", lstMovieRsltAlloCine);
		}

		else {

			validation.addError("Title",
					"Il n'y a pas de propositions d'ajout pour ce titre.");
			if (validation.hasErrors())
				render("Movies/CreateMovie.html");
		}
	}

	public static Element getElementByPageAllocine(String UrlTitle, int page) {

		if (page == 1) {
			try {
				Document doc = Jsoup
						.connect(
								"http://www.allocine.fr/recherche/1/?q="
										+ UrlTitle).timeout(0).get();
				return doc.getElementsByClass("vmargin10t").first();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("probleme jsoup");
				ShowCreateMovie("");
			}
		} else {
			try {
				Document doc = Jsoup
						.connect(
								"http://www.allocine.fr/recherche/1/?p=" + page
										+ "&q=" + UrlTitle).timeout(0).get();
				return doc.getElementsByClass("vmargin10t").first();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("probleme jsoup");
				ShowCreateMovie("");
			}
		}
		return null;
	}

	public static int getNbPageResultAlloCine(String UrlTitle) {

		try {
			Document doc = Jsoup
					.connect(
							"http://www.allocine.fr/recherche/1/?q=" + UrlTitle)
					.timeout(0).get();
			
			if(doc.getElementsByClass("colcontent").first().getElementsByClass("morezone").first() != null)
				return Integer.parseInt(doc.getElementsByClass("colcontent")
						.first().getElementsByClass("morezone").first()
						.getElementsByClass("navcenterdata").first().text()
						.split(" / ")[1]);
			else
				return 1;
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("probleme jsoup");
			ShowCreateMovie("");
		}
		return 0;
	}

	public static boolean hasResultAlloCine(String UrlTitle) {
		try {
			Document doc = Jsoup
					.connect(
							"http://www.allocine.fr/recherche/1/?q=" + UrlTitle)
					.timeout(0).get();
			return (doc.getElementsByClass("vmargin10t").first() != null);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("probleme jsoup");
			ShowCreateMovie("");
		}
		return false;
	}

	public static void AddMvieFromAlloCine(String title, String director,
			String actor, String urlAlloCine, int year) {
		MovieTemp.AddMovieFromAlloCine(title, director, actor, urlAlloCine,
				year);
		Application.search(title);
	}
}
