package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Entity;

import play.data.validation.Email;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class User extends Model implements Comparable{

	@MaxSize(64)
	@Required
	public String Name;

	@MaxSize(32)
	@Required
	public String Firstname;

	@MaxSize(128)
	@Email
	@Required
	public String Email;

	@MinSize(6)
	@MaxSize(32)
	@Required
	public String Password;

	@Required
	public String Date;

	public User(String Name, String Firstname, String Email, String Password,
			String date) {
		this.Name = Name;
		this.Firstname = Firstname;
		this.Email = Email;
		this.Password = Password;
		this.Date = date;
	}

	public static User connect(String Email, String Password) {
		return find("byEmailAndPassword", Email, Password).first();
	}

	// Retourne le nombre de critiques postés par l'utilisateur
	public int nbCritical(){
		return Critical.nbCritical(this);
	}
	
	// Retourne le nombre de note sur film de l'utilisateur
	public int nbNoteMovie(){
		return NoteMovie.nbNoteMovie(this);
	}
	
	// Retourne le nombre de note sur critique de l'utilisateur
	public int nbNoteCritical(){
		return NoteCritical.nbNoteCritical(this);
	}
	
	// calcul de la moyenne des moyennes de toutes les critiques d'un
	// utilisateur
	public double moyenneGlobale() {
		List<Critical> criticals = Critical.myCriticals(this);
		double nbMoy = 0;
		double sum = 0;
		if (criticals.size() != 0) {
			for (int i = 0; i < criticals.size(); i++) {
				if (criticals.get(i).moyenneNote() != 21) {
					nbMoy++;
					sum += criticals.get(i).moyenneNote();
				}
			}
			if (nbMoy != 0)
				return sum / nbMoy;
		}
		return 21;
	}
	
	public int nbUserTotal(){
		return User.findAll().size();
	}
	
	public int nbMovieTotal(){
		return Movie.findAll().size();
	}
	
	public int nbCriticalTotal(){
		return Critical.findAll().size();
	}
	
	public int nbNoteCriticalTotal(){
		return NoteCritical.findAll().size();
	}
	
	public int nbNoteMovieTotal(){
		return NoteMovie.findAll().size();
	}
	
	public int nbPoint(){
		int nbCritical = nbCritical();
		int nbNoteMovie = nbNoteMovie();
		int nbNoteCritical = nbNoteCritical();
		
		nbCritical *=2;
		int sum = nbCritical+nbNoteCritical+nbNoteMovie; 
		return sum;
	}
	
	public static List<User> getTopTen(){
		List<User> allUsers = User.findAll();
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
				return topTen;
			}
		}
		return null;
	}
	
	public String toString() {
		return Email;
	}

	@Override
	public int compareTo(Object other) {
		
		int nbCriticalOther = ((User) other).nbCritical();
		int nbNoteMovieOther = ((User) other).nbNoteMovie();
		int nbNoteCriticalOther = ((User) other).nbNoteCritical();
		int nbCritical = nbCritical();
		int nbNoteMovie = nbNoteMovie();
		int nbNoteCritical = nbNoteCritical();
		
		// Une critique à plus de valeur qu'une note
		nbCriticalOther *=2;
		nbCritical *=2;
		
		int sumOther = nbCriticalOther+nbNoteCriticalOther+nbNoteMovieOther;
		int sum = nbCritical+nbNoteCritical+nbNoteMovie; 
		
		if (sumOther > sum)
			return -1;
		else if (sumOther == sum)
			return 0;
		else
			return 1;
	}
}
