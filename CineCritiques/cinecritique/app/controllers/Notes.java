package controllers;

import java.util.List;

import models.Critical;
import models.Movie;
import models.NoteCritical;
import models.NoteMovie;
import models.User;
import play.data.validation.Max;
import play.data.validation.Min;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Notes extends Controller {

	public static void addNoteMovie(@Min(0) @Max(20) @Required int note, String movie) {

		if (validation.hasErrors()) {
			Application.search(movie);
		}
		User user = User.find("byEmail", session.get("username")).first();
		Movie mvie = Movie.find("byTitle", movie).first();
		NoteMovie newNote = new NoteMovie(note, user, mvie);
		newNote.save();
		flash.success("Merci pour votre contribution !");
		Application.search(movie);
	}

	public static void deleteNoteMovie(long idNote) {
		NoteMovie.delete(idNote);
		myNotes();
	}
	
	public static void addNoteCritical(@Min(0) @Max(20) @Required int note, String movie, long critique) {

		if (validation.hasErrors()) {
			Criticals.show(critique);
		}
		User user = User.find("byEmail", session.get("username")).first();
		Critical crit = Critical.find("byId", critique).first();
		NoteCritical newNote = new NoteCritical(note, user, crit);
		newNote.save();
		flash.success("Merci pour votre contribution !");
		Criticals.show(critique);
	}

	public static void deleteNoteCritical(long idNote) {
		NoteCritical.delete(idNote);
		myNotes();
	}

	public static void myNotes() {
		User user = User.find("byEmail", session.get("username")).first();
		List<NoteMovie> notesM = NoteMovie.myNotes(user);
		List<NoteCritical> notesC = NoteCritical.myNotes(user);
		render("Notes/myNotes.html", notesM, notesC);
	}
}
