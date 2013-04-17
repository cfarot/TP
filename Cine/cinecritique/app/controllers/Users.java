package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.Critical;
import models.NoteCritical;
import models.NoteMovie;
import models.User;
import play.data.validation.Email;
import play.data.validation.Required;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Users extends Controller {

	 @Before
	    static void setConnectedUser() {
	        if(Secure.Security.isConnected()) {
	        	
	            User user = User.find("byEmail", Secure.Security.connected()).first();
	            renderArgs.put("user", user);
	        }
	    }

	public static void index() {
		render();
	}
	
}
