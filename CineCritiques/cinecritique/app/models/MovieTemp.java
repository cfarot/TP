package models;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MovieTemp {

	public String Title;

	public String Image;

	public int Annee;

	public String MainActor;

	public String director;

	public boolean ExistOnCineCritique;

	public String linkAlloCine;

	public MovieTemp(String title, String image, int annee, String actor,
			String director, String link) {
		this.Title = title;
		this.Image = image;
		this.Annee = annee;
		this.MainActor = actor;
		this.director = director;
		this.linkAlloCine = link;

		String actorName = "";
		String actorFirstName = "";
		if (actor.split(" ").length > 1) {
			actorFirstName = actor.split(" ")[0];
			actorName = actor.split(" ")[1];
			if (actor.split(" ").length > 2) {
				for (int i = 2; i < actor.split(" ").length; i++) {
					actorName += " " + actor.split(" ")[i];
				}
			}
		} else
			actorFirstName = actor;

		String directorName = "";
		String directorFirstName = "";
		if (director.split(" ").length > 1) {
			directorFirstName = director.split(" ")[0];
			directorName = director.split(" ")[1];
			if (director.split(" ").length > 2) {
				for (int i = 2; i < director.split(" ").length; i++) {
					directorName += " " + director.split(" ")[i];
				}
			}
		} else
			directorFirstName = director;

		if (Movie
				.find("select m from Movie m where m.Title = ? and m.Actor = (select a from Actor a where a.Name = ? and a.Firstname = ?) and m.Director = (select d from Director d where d.Name = ? and d.Firstname = ?)",
						title, actorName, actorFirstName, directorName,
						directorFirstName).fetch().size() != 0)
			this.ExistOnCineCritique = true;
		else
			this.ExistOnCineCritique = false;
	}

	public static void AddMovieFromAlloCine(String title, String director,
			String actor, String urlAlloCine, int year) {

		DateFormat formater = DateFormat.getDateInstance(DateFormat.SHORT,
				Locale.FRANCE);

		try {
			Document doc = Jsoup.connect(urlAlloCine).timeout(0).get();
			Element element = doc.getElementsByClass("data_box").first();
			Element elementImage = element.getElementsByClass("poster").first();
			String image = elementImage.select("img").first().absUrl("src");

			Element elementSynop = doc.getElementsByClass("margin_20b").first();
			String synopsis = elementSynop.text();

			Element elementContent = element
					.select("table.data_box_table.margin_10b").first();
			Date date;
			// modifier récupération de la date
			String d = elementContent.select("tr").first().select("td").first().select("span").first().select("span").first().getElementsByAttribute("content").attr("content");
			
				if(d.split("-").length == 3){
					String dateFr = d.split("-")[2] + "/" + d.split("-")[1] + "/"
						+ d.split("-")[0];
					date = formater.parse(dateFr);
				}
				else
					date = null;
				
			String country = elementContent.select("tr").get(4).select("span").get(1).text();

			String directorName = "";
			String directorFirstName = "";
			if (director.split(" ").length > 1) {
				directorFirstName = director.split(" ")[0];
				directorName = director.split(" ")[1];
				if (director.split(" ").length > 2) {
					for (int i = 2; i < director.split(" ").length; i++) {
						directorName += " " + director.split(" ")[i];
					}
				}
			} else
				directorFirstName = director;

			Director directorData;
			Director directorCheck = Director.find("byFirstNameAndName",
					directorFirstName, directorName).first();
			if (directorCheck != null) {
				directorData = directorCheck;
			} else {
				directorData = new Director(directorName, directorFirstName);
				directorData.save();
			}

			String actorName = "";
			String actorFirstName = "";
			if (actor.split(" ").length > 1) {
				actorFirstName = actor.split(" ")[0];
				actorName = actor.split(" ")[1];
				if (actor.split(" ").length > 2) {
					for (int i = 2; i < actor.split(" ").length; i++) {
						actorName += " " + actor.split(" ")[i];
					}
				}
			} else
				actorFirstName = actor;

			Actor actorData;
			Actor actorCheck = Actor.find("byFirstNameAndName", actorFirstName,
					actorName).first();
			if (actorCheck != null) {
				actorData = actorCheck;
			} else {
				actorData = new Actor(actorName, actorFirstName);
				actorData.save();
			}

			if (!MovieExist(title, actorData, directorData)) {
				Movie newMovie = new Movie(title, synopsis, date, actorData,
						country, directorData, image);
				newMovie.save();
			}

		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static boolean MovieExist(String title, Actor actor,
			Director director) {
		return (Movie
				.find("byTitleAndActor_idAndDirector_id", title, actor,
						director).fetch().size() != 0);
	}
}
