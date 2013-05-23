package models;

import java.util.ArrayList;
import java.util.Collections;
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
	
	// Retourne 3 critiques des autres utilisateurs sur un film en particulier (utilisateur connecté)
	public static List<Critical> otherCriticals(User user, Movie movie){
		//return Critical.find("select c from Critical c where c.User != :id and c.Movie = :movie").bind("id", user).bind("movie", movie).fetch();
		return Critical.find("select c from Critical c where c.User != :id and c.Movie = :movie ORDER BY c.id DESC").bind("id", user).bind("movie", movie).fetch(3);
	}
	
	// Retourne 3 critiques des autres utilisateurs d'un film à partir d'un id (utilisateur connecté)
		public static List<Critical> criticalsload(User user, Movie movie, int id){
			return Critical.find("select c from Critical c where c.User != :idU and c.Movie = :idM and c.id < :id ORDER BY c.id DESC").bind("idU", user).bind("idM", movie).bind("id", id).fetch(3);
		}
		
		// Retourne le nombre de critiques d'un film excepté celle de l'utilisateur connecté
		public static int nbOtherCriticalsOnMovie(User user, Movie movie){
			return Critical.find("select c from Critical c where c.User != :id and c.Movie = :movie ORDER BY c.id DESC").bind("id", user).bind("movie", movie).fetch().size();
		}
	// Retourne le nombre de critiques d'un film
		public static int nbCriticalsOnMovie(Movie movie){
			return Critical.find("byMovie_id", movie).fetch().size();
		}
		
	// Retourne les 3 dernières critiques d'un film(utilisateur non connecté)
	public static List<Critical> criticalsOnMovie(Movie movie){
		return Critical.find("select c from Critical c where c.Movie = :id ORDER BY c.id DESC").bind("id", movie).fetch(3);
	}
	
	// Retourne 3 critiques d'un film à partir d'un id (utilisateur non connecté)
	public static List<Critical> criticalsload(Movie movie, int id){
		return Critical.find("select c from Critical c where c.Movie = :mov and c.id < :id ORDER BY c.id DESC").bind("mov", movie).bind("id", id).fetch(3);
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
	
	public static List<Critical> getTopTen(){
		List<Critical> allCriticals = Critical.findAll();
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
				return topTen;
			}
		}
		return null;
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
