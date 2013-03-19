package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToOne;


import play.data.validation.Max;
import play.data.validation.Min;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class NoteMovie extends Model {

	@Min(0)
	@Max(20)
	@Required
	public int Note; // Note Critical Between 0 and 20

	@Required
	@OneToOne
	public User User; // User Note Movie

	@Required
	@OneToOne
	public Movie Movie; // Movie Note Movie

	public NoteMovie(int note, User user, Movie movie) {
		this.Note = note;
		this.User = user;
		this.Movie = movie;
	}

	public static double moyenne(Movie movie) {
		List<NoteMovie> lstNote = NoteMovie.NotesOnMovie(movie);

		if (lstNote.size() > 0) {
			double sum = 0;
			for (int i = 0; i < lstNote.size(); i++) {
				sum += (double) lstNote.get(i).Note;
			}
			return sum / lstNote.size();
		}
		return 21;
	}

	// Retourne la note d'un utilisateur sur un film donné
	public static NoteMovie myNote(User user, Movie movie) {
		return NoteMovie.find("byUser_idAndMovie_id", user.id, movie.id)
				.first();
	}

	// Suppression d'une note de film
	public static void delete(long idNoteMovie) {
		NoteMovie noteSup = NoteMovie.findById(idNoteMovie);
		noteSup.delete();
	}

	// Retourne toutes les notes de film d'un utilisateur
	public static List<NoteMovie> myNotes(User user) {
		return NoteMovie.find("byUser_id", user.id).fetch();
	}

	// Retourne toutes les notes d'un film
	public static List<NoteMovie> NotesOnMovie(Movie movie) {
		return NoteMovie.find("byMovie_id", movie).fetch();
	}

	// Retourne le nombre de note sur film posté par un utilisateur
	public static int nbNoteMovie(User user) {
		return NoteMovie.find("select n from NoteMovie n where n.User = :id")
				.bind("id", user).fetch().size();
	}
}
