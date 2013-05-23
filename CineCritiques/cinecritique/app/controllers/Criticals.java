package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.Critical;
import models.Movie;
import models.NoteCritical;
import models.NoteMovie;
import models.User;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.With;
@With(Secure.class)
public class Criticals extends Controller {

	public static void addCritical(String movie,
			@Required(message = "Critique vide !") String content, String title) {

		if (validation.hasErrors()) {
			Application.search(movie);
		}
		User user = User.find("byEmail", session.get("username")).first();
		Movie mvie = Movie.find("byTitle", movie).first();
		Critical newCritical = new Critical(title, content, mvie, user);
		newCritical.save();
		flash.success("Merci pour votre contribution !");
		Application.search(movie);
	}

	public static void myCriticals() {
		User user = User.find("byEmail", session.get("username")).first();
		List<Critical> criticals = Critical.myCriticals(user);
		render("Criticals/myCriticals.html", criticals, user);
	}

	public static void delete(long idCritical) {
		Critical.delete(idCritical);
		myCriticals();
	}

	public static void show(long idCritical) {
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

}
