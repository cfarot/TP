package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Actor extends Model{
	
	@MaxSize(64)
	@Required
	public String Name; //Name Actor
	
	@MaxSize(32)
	@Required
	public String Firstname; //FirstName Actor
	
	
	public Date Date; //Date Birth
	
	public String Country;  //Country Actor
	
	public Actor(String name, String firstname, Date date, String country){
		this.Name = name;
		this.Firstname = firstname;
		this.Date = date;
		this.Country = country;
	}
	
	public Actor(String name, String firstname){
		this.Name = name;
		this.Firstname = firstname;
	}
	
	public String toString(){
		return Firstname+" "+Name; 
	} 
}
