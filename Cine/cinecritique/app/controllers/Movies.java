package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import models.Actor;
import models.Movie;
import models.Director;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Movies extends Controller {

	public static void CreateMovie(
			@MaxSize(128) @Required(message = "Veuillez renseigner le titre du film.") String Title,
			@MaxSize(1024) @Required(message = "Veuillez renseigner le synopsis.") String Synopsis,
			@Required(message = "Veuillez renseigner la date de sortie.") Date date,
			@MaxSize(64) @Required(message = "Veuillez renseigner le nom de l'acteur principal.") String ActorName,
			@MaxSize(32) @Required(message = "Veuillez renseigner le prénom de l'acteur principal.") String ActorFirstName,
			@Required(message = "Veuillez renseigner le pays d'origine du film.") String country,
			@MaxSize(64) @Required(message = "Veuillez renseigner nom du réalisateur.") String DirectorName,
			@MaxSize(32) @Required(message = "Veuillez renseigner le prénom du réalisateur.") String DirectorFirstName) {

		if (validation.hasErrors())
			render("Movies/CreateMovie.html");

		Movie movieCheck = Movie.find("byTitle", Title).first();

		if (movieCheck != null) {
			validation.addError(Title, "Ce film existe déjà.");
			ShowCreateMovie();
		}

		Actor actorData;
		Actor actorCheck = Actor.find("byFirstNameAndName", ActorFirstName,
				ActorName).first();
		if (actorCheck != null) {
			actorData = actorCheck;
		} else {
			actorData = new Actor(ActorName, ActorFirstName);
			actorData.save();
		}

		Director directorData;
		Director directorCheck = Director.find("byFirstNameAndName",
				DirectorFirstName, DirectorName).first();

		if (directorCheck != null) {
			directorData = directorCheck;
		} else {
			directorData = new Director(DirectorName, DirectorFirstName);
			directorData.save();
		}

		Movie newMovie = new Movie(Title, Synopsis, date, actorData, country,
				directorData,"");
		newMovie.save();
		flash.success("Merci pour votre contribution ! ");

		Application.search(Title);
	}

	public static void ShowCreateMovie() {
		render("Movies/CreateMovie.html");
	}

	public static void update(String movie) {
		Movie mvie = Movie.find("byTitle", movie).first();
		render("Movies/Update.html", mvie);
	}

	public static void updateMovie(
			@MaxSize(128) @Required(message = "Veuillez renseigner le titre du film.") String Title,
			@MaxSize(1024) @Required(message = "Veuillez renseigner le synopsis.") String Synopsis,
			@Required(message = "Veuillez renseigner la date de sortie.") Date date,
			@MaxSize(64) @Required(message = "Veuillez renseigner le nom de l'acteur principal.") String ActorName,
			@MaxSize(32) @Required(message = "Veuillez renseigner le prénom de l'acteur principal.") String ActorFirstName,
			@Required(message = "Veuillez renseigner le pays d'origine du film.") String country,
			@MaxSize(64) @Required(message = "Veuillez renseigner nom du producteur.") String ProducerName,
			@MaxSize(32) @Required(message = "Veuillez renseigner le prénom du producteur.") String ProducerFirstName,
			long idMovie) {

		Movie mvie = Movie.findById(idMovie);
		if (validation.hasErrors())
			render("Movies/Update.html", mvie);

		Actor actorData;
		Actor actorCheck = Actor.find("byFirstNameAndName", ActorFirstName,
				ActorName).first();
		if (actorCheck != null) {
			actorData = actorCheck;
		} else {
			actorData = new Actor(ActorName, ActorFirstName);
			actorData.save();
		}

		Director directorData;
		Director producerCheck = Director.find("byFirstNameAndName",
				ProducerFirstName, ProducerName).first();

		if (producerCheck != null) {
			directorData = producerCheck;
		} else {
			directorData = new Director(ProducerName, ProducerFirstName);
			directorData.save();
		}

		mvie.Title = Title;
		mvie.Synopsis = Synopsis;
		mvie.Date = date;
		mvie.Actor = actorData;
		mvie.Country = country;
		mvie.Director = directorData;
		mvie.save();
		flash.success("Merci pour votre contribution ! ");
		Application.search(mvie.Title);
	}

}