package models;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Director extends Model{
	
	@MaxSize(64)
	@Required
	public String Name; //Name Producer
	
	@MaxSize(32)
	@Required
	public String Firstname; //FirstName Producer
	
	
	public Date Date; //Date Birth Producer
	
	
	public String Country; //Country Producer
	
	public Director (String name, String firstname, Date date, String country){
		this.Name = name;
		this.Firstname = firstname;
		this.Date = date;
		this.Country = country;
	}
	
	public Director(String name, String firstname){
		this.Name = name;
		this.Firstname = firstname;
	}
	
	public String toString(){
		return Firstname+" "+Name;
	}	
}
