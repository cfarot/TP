package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;

import play.data.validation.Max;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class NoteCritical extends Model {

	@Min(0)
	@Max(10)
	@Required
	public int Note; // Note Critical Between 0 and 10

	@Required
	@OneToOne
	public User User; // User Note Critical

	@Required
	@OneToOne
	public Critical Critical; // Critical Note Critical

	public NoteCritical(int note, User user, Critical critical) {
		this.Note = note;
		this.User = user;
		this.Critical = critical;
	}

	// calcul la moyenne des notes d'une critique, retourne 21 si la critique
	// n'a jamais été noté
	public static double moyenne(Critical crit) {
		List<NoteCritical> lstNote = NoteCritical.NotesOnCritical(crit);
		if (lstNote.size() > 0) {
			double sum = 0;
			for (int i = 0; i < lstNote.size(); i++) {
				sum += (double) lstNote.get(i).Note;
			}
			return sum / lstNote.size();
		}
		return 21;
	}

	// Retourne la note d'un utilisateur sur une critique donné
	public static NoteCritical myNote(User user,Critical crit){
		return NoteCritical.find("byUser_idAndCritical_id", user.id, crit.id).first();
	}
	
	// Suppression d'une note d'une critique
	public static void delete(long idNoteCritical) {
		NoteCritical noteSup = NoteCritical.findById(idNoteCritical);
		noteSup.delete();
	}
	
	// Suppression de toute les notes d'une critique
	public static void deleteNotes(Critical crit){
		List<NoteCritical> lstNoteCritical = NoteCritical.find("byCritical_id", crit).fetch();
		if(lstNoteCritical.size() != 0){
			for(int i=0; i<lstNoteCritical.size(); i++){
				delete(lstNoteCritical.get(i).id);
			}
		}
	}

	// Retourne toutes les notes de critique d'un utilisateur
	public static List<NoteCritical> myNotes(User user) {
		return NoteCritical.find("byUser_id", user.id).fetch();
	}
	
	//Retourne toutes les notes d'une critique
	public static List<NoteCritical> NotesOnCritical(Critical crit){
		return NoteCritical.find("byCritical_id", crit).fetch();
	}
	
	// Retourne le nombre de note sur critique posté par un utilisateur
		public static int nbNoteCritical(User user){
			return NoteCritical
					.find("select n from NoteCritical n where n.User = :id")
					.bind("id", user).fetch().size();
		}
}
