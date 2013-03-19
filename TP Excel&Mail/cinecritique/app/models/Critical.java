package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Critical extends Model implements Comparable{
	
	@MaxSize(128)
	@Required
	public String Title;
	
	@Lob
	@Required
	@MaxSize(10000)
	public String Content; 
	
	@Required
	public Date Date; 
	
	@Required
	@ManyToOne
	public Movie Movie;
	
	@Required
	@ManyToOne
	public User User;
	
	public Critical(String title, String content, Movie movie, User user){

		this.Title = title;
		this.Content = content;
		this.Date = new Date();
		this.Movie = movie;
		this.User = user;	
	}
	
	// Retourne la critique d'un utilisateur sur un film en particulier
	public static Critical myCritical(User user, Movie movie){
		return Critical.find("byUser_idAndMovie_id", user.id, movie.id).first();
	}
	
	// Retourne toutes les critiques d'un utilisateur
	public static List<Critical> myCriticals(User user){
		return Critical.find("byUser_id", user.id).fetch();
	}
	
	// Retourne les critiques des autres utilisateurs sur un film en particulier
	public static List<Critical> otherCriticals(User user, Movie movie){
		return Critical
		.find("select c from Critical c where c.User != :id and c.Movie = :movie")
		.bind("id", user).bind("movie", movie).fetch();
	}
	
	// Retourne les critiques d'un film
	public static List<Critical> criticalsOnMovie(Movie movie){
		return Critical.find("byMovie_id", movie).fetch();
	}
	
	// Suppression d'une critique
	public static void delete(long idCritical){
		Critical crtlSup = Critical.findById(idCritical);
		NoteCritical.deleteNotes(crtlSup);
		crtlSup.delete();
	}
	
	// Calcul de la note moyenne d'une critique, retourne 21 
	//si la critique n'a jamais été notée.
	public double moyenneNote(){
		return NoteCritical.moyenne(this);
	}
	
	// Retourne le nombre de critique posté par un utilisateur
	public static int nbCritical(User user){
		return Critical
				.find("select c from Critical c where c.User = :id")
				.bind("id", user).fetch().size();
	}
	
	// pagination
	public Critical previous() {
		return Critical.find("postedAt < ? order by postedAt desc", Date)
				.first();
	}

	public Critical next() {
		return Critical.find("postedAt > ? order by postedAt asc", Date)
				.first();
	}
	
	public String toString(){
		return this.id.toString();
	}

	@Override
	public int compareTo(Object other) {
		double moyenneOther = ((Critical) other).moyenneNote();
		double moyenne = this.moyenneNote();

		if (moyenneOther > moyenne)
			return -1;
		else if (moyenneOther == moyenne)
			return 0;
		else
			return 1;
	}
}
