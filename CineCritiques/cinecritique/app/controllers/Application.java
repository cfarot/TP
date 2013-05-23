package controllers;

import play.cache.Cache;
import play.data.validation.*;
import play.libs.Codec;
import play.libs.Images;
import play.mvc.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import models.*;

public class Application extends Controller {

	public static final int AUTOCOMPLETE_MAX = 4;
	
	public static void Inscription(
			@MaxSize(64) @MinSize(2) @Required(message = "Le Nom doit être renseigné.") String name,
			@MaxSize(32) @MinSize(2) @Required(message = "Le Prénom doit être renseigné.") String firstname,
			@Email @Required(message = "L'email doit être renseigné.") String email,
			@Required(message = "La date de naissance doit être renseigné.") String date,
			@MinSize(6) @MaxSize(32) @Required(message = "Le mot de passe doit être renseigné.") String password,
			@Required(message = "La confirmation du mot de passe doit être renseigné.") String confirmePassword,
			@Required(message = "Veuillez entrer le code") String code,
			String randomID) {

		validation.equals(code, Cache.get(randomID)).message(
				"Code invalide. Veuillez le retapez");
		validation.equals(password, confirmePassword).message(
				"La confirmation du mot de passe ne correspond pas.");

		if (validation.hasErrors())
			render("Secure/login.html", randomID);

		User user = User.find("byEmail", email).first();
		if (user != null)
			validation
					.addError(email, "Vous êtes déja inscrit sur notre site.");

		if (validation.hasErrors())
			render("Secure/login.html", randomID);

		User newUser = new User(name, firstname, email, password, date);
		newUser.save();
		flash.success("Merci pour votre inscription ! %s", firstname);

		Cache.delete(randomID);
		try {
			Secure.login();
		} catch (Throwable e) {
			render("Secure/login.html");
		}
	}

	public static void search(
			@MaxSize(128) @Required(message = "Le Titre est inexistante ou trop long.") String keywords) {

		Movie movie = Movie.find("byTitle", keywords).first();
		if (movie != null) {
			int nbCrit;
			if (session.contains("username")) {
				User me = User.find("byEmail", session.get("username")).first();
				Critical critical = Critical.myCritical(me, movie);
				NoteMovie note = NoteMovie.myNote(me, movie);
				List<Critical> criticals = Critical.otherCriticals(me, movie);
				nbCrit = Critical.nbOtherCriticalsOnMovie(me, movie);

				render("Application/search.html", movie, critical, criticals,
						me, note, nbCrit);
			} else {
				List<Critical> criticals = Critical.criticalsOnMovie(movie);
				nbCrit = Critical.nbCriticalsOnMovie(movie);
				render("Application/search.html", movie, criticals, nbCrit);
			}
		}
		else{	
			List<Movie> movieList = Movie.find("Title like ?", "%"+keywords+"%").fetch();
			render("Application/search.html", movie, movieList);
		}
		render("Application/search.html", movie);
	}

	public static void showCritical(long idCritical) {

		Critical crit = Critical.findById(idCritical);
		if (crit != null) {
			if (session.contains("username")) {
				User me = User.find("byEmail", session.get("username")).first();
				NoteCritical note = NoteCritical.myNote(me, crit);
				render("Criticals/show.html", crit, note);
			}
		}
		render("Criticals/show.html", crit);
	}

	// Affiche les 10 films les plus populaire
	public static void topFilms() {
		List<Movie> topTen = Movie.getTopTen();
		if(topTen != null)
			render("Movies/TopFilms.html", topTen);
		else
			render("Movies/TopFilms.html");
		/*List<Movie> allMovies = Movie.findAll();
		List<Movie> topTen = new ArrayList<Movie>();

		if (allMovies.size() > 0) {
			// suppression des films non notés
			for (int j = 0; j < allMovies.size(); j++) {
				if (allMovies.get(j).moyenneNote() == 21) {
					allMovies.remove(j);
					j--;
				}
			}
			// mise en place du top 10
			if (allMovies.size() > 0) {
				Collections.sort(allMovies, Collections.reverseOrder());
				if (allMovies.size() > 10) {
					for (int i = 0; i < 10; i++) {
						topTen.add(allMovies.get(i));
					}
				} else {
					topTen = allMovies;
				}
				render("Movies/TopFilms.html", topTen);
			}
		}
		render("Movies/TopFilms.html");*/
	}

	// Affiche les 10 utilisateurs les plus actif sur le site.
	public static void topUsers() {
		List<User> topTen = User.getTopTen();
		if(topTen != null)
			render("Users/TopUsers.html", topTen);
		else
			render("Users/TopUsers.html");;
		/*List<User> allUsers = User.findAll();
		List<User> topTen = new ArrayList<User>();

		if (allUsers.size() > 0) {
			// suppression des utilisateurs non actif
			for (int j = 0; j < allUsers.size(); j++) {
				if (allUsers.get(j).nbCritical() == 0
						&& allUsers.get(j).nbNoteCritical() == 0
						&& allUsers.get(j).nbNoteMovie() == 0) {
					allUsers.remove(j);
					j--;
				}
			}
			// mise en place du top 10
			if (allUsers.size() > 0) {
				Collections.sort(allUsers, Collections.reverseOrder());
				if (allUsers.size() > 10) {
					for (int i = 0; i < 10; i++) {
						topTen.add(allUsers.get(i));
					}
				} else {
					topTen = allUsers;
				}
				render("Users/TopUsers.html", topTen);
			}
		}
		render("Users/TopUsers.html");*/
	}

	// Affiche les 10 critiques les plus populaire
	public static void topCritiques() {
		List<Critical> topTen = Critical.getTopTen();
		if(topTen != null)
			render("Criticals/TopCriticals.html", topTen);
		else
			render("Criticals/TopCriticals.html");
		/*List<Critical> allCriticals = Critical.findAll();
		List<Critical> topTen = new ArrayList<Critical>();

		if (allCriticals.size() > 0) {
			// suppression des films non notés
			for (int j = 0; j < allCriticals.size(); j++) {
				if (allCriticals.get(j).moyenneNote() == 21) {
					allCriticals.remove(j);
					j--;
				}
			}
			// mise en place du top 10
			if (allCriticals.size() > 0) {
				Collections.sort(allCriticals, Collections.reverseOrder());
				if (allCriticals.size() > 10) {
					for (int i = 0; i < 10; i++) {
						topTen.add(allCriticals.get(i));
					}
				} else {
					topTen = allCriticals;
				}
				render("Criticals/TopCriticals.html", topTen);
			}
		}
		render("Criticals/TopCriticals.html");*/
	}

	public static void filmsAffiche() {

		Map map = new HashMap<String, String>();
		try {
			Document doc = Jsoup.connect(
					"http://www.allocine.fr/film/alaffiche.html").get();

			Element element = doc.getElementsByClass("colcontent").first();
			Elements elements = element.getElementsByClass("mainzone");

			for (int i = 0; i < elements.size(); i++) {
				String image = elements.get(i).select("img").first()
						.absUrl("src");
				String title = elements.get(i).getElementsByClass("contenzone")
						.first().select("a").first().text();
				map.put(title, image);

				String director = elements.get(i)
						.getElementsByClass("contenzone").first().select("p")
						.get(1).select("a").first().text().split(",")[0];
				String actor = elements.get(i).getElementsByClass("contenzone")
						.first().select("p").get(2).select("a").first().text()
						.split(",")[0];
				String linkFilm = elements.get(i).select("a").first()
						.absUrl("href");
				MovieTemp.AddMovieFromAlloCine(title, director, actor,
						linkFilm, 0);
			}
			render("Movies/Affiche.html", map);
		} catch (IOException e) {
			System.out.println("erreur connexion à allociné");
			filmsAffiche();
		}
	}

	public static void listMovies(final String term) {
		
		final List<Movie> response = new ArrayList<Movie>();
		for (Movie movie : moviesLocations()) {
			if (movie.Title.toLowerCase().startsWith(term.toLowerCase())) {
				response.add(movie);
			}
			if (response.size() == AUTOCOMPLETE_MAX) {
				break;
			}
		}
		renderJSON(response);
	}
	
	public static ArrayList<Movie> moviesLocations() {
		return (ArrayList) Movie.findAll();
	}
	
	public static String loadCriticals(int last, long idMovie) {
		
		String critiques = "<br />";
		List<Critical> lst;
		Movie movie = Movie.findById(idMovie);
		
		if (session.contains("username")) {
			User me = User.find("byEmail", session.get("username")).first();
			lst = Critical.criticalsload(me, movie, last);
		} else {
			lst = Critical.criticalsload(movie, last);
		}
		
		String dateEng = lst.get(0).Date.toString().split(" ")[0];
		String[] tabDate = dateEng.split("-");
		String dateFr = tabDate[2]+"/"+tabDate[1]+"/"+tabDate[0];
		for(int i=0; i<lst.size(); i++){
			critiques += "<div class=\"criticals\" id=\""+lst.get(i).id+"\"> <a href=\"/showCritical/"+lst.get(i).id+"\"> "+lst.get(i).Title+"</a> <span class=\"critical-author\"> Posté par "+lst.get(i).User.Firstname+" "+lst.get(i).User.Name+", </span> <span class=\"critical-date\">le "+dateFr+"</span> </div>";
		}
		return critiques;
	}
}