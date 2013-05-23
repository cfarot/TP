package controllers;

import java.util.Date;

import play.cache.Cache;
import play.data.validation.Email;
import play.data.validation.Required;
import play.libs.Images;
import models.*;

public class Security extends Secure.Security {

	static boolean authenticate(String email, String password) {
		return User.connect(email, password) != null;
	}

	static void onDisconnected() {
		
	}

	static void onAuthenticated() {
		Users.index();
	}

	public static void captcha(String id) {
		Images.Captcha captcha = Images.captcha();
		String code = captcha.getText("#E4EAFD");
		Cache.set(id, code, "10mn");
		renderBinary(captcha);
	}

}
