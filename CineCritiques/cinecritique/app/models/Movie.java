package models;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Movie extends Model implements Comparable {

	@MaxSize(128)
	@Required
	public String Title;

	@Lob
	@Required
	@MaxSize(10000)
	public String Synopsis;

	@Required
	public Date Date;

	@Required
	@ManyToOne
	public Actor Actor;

	public String Country;

	@Required
	@ManyToOne
	public Director Director;
	
	public String Image;

	public Movie(String title, String synopsis, Date date, Actor actor,
			String country, Director director, String image) {
		this.Title = title;
		this.Synopsis = synopsis;
		this.Date = date;
		this.Country = country;
		this.Actor = actor;
		this.Director = director;
		this.Image = image;
	}

	// Calcul de la note moyenne d'un film, retourne 21
	// si le film n'a jamais été notée.
	public double moyenneNote() {
		return NoteMovie.moyenne(this);
	}

	public String toString() {
		return Title;
	}

	public static List<Movie> getTopTen(){
	
		List<Movie> allMovies = Movie.findAll();
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
				return topTen;
			}
		}
		return null;
	}
	
	@Override
	public int compareTo(Object other) {
		double moyenneOther = ((Movie) other).moyenneNote();
		double moyenne = this.moyenneNote();

		if (moyenneOther > moyenne)
			return -1;
		else if (moyenneOther == moyenne)
			return 0;
		else
			return 1;
	}
	
}
