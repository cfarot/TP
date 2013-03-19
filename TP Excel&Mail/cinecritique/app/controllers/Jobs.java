package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import play.jobs.*;
import play.mvc.Mailer;

import models.Movie;

import org.apache.commons.mail.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

@On("0 * * * * ?")
public class Jobs extends Job {

	public void doJob() {

		System.out.println("Envoi de la liste des films");

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("ma feuille");

		HSSFRow row1 = sheet.createRow(0);
		row1.createCell(0)
				.setCellValue("Liste des films présent sur le site :");

		List<Movie> lstMovie = Movie.findAll();
		if (lstMovie.size() > 0) {
			for (int i = 0; i < lstMovie.size(); i++) {
				sheet.createRow(i + 2).createCell(0)
						.setCellValue(lstMovie.get(i).Title);
			}
		} else {
			sheet.createRow(2)
					.createCell(0)
					.setCellValue(
							"Il n'y a pas de films pour le moment sur le site.");
		}

		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream("ListeFilms.xls");
			wb.write(fileOut);
			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Create the attachment
		EmailAttachment attachment = new EmailAttachment();
		attachment.setPath("ListeFilms.xls");
		attachment.setDisposition(EmailAttachment.ATTACHMENT);
		attachment.setDescription("Liste films");
		attachment.setName("Films.xls");

		// Create the email message
		MultiPartEmail email = new MultiPartEmail();
		email.setHostName("smtp.gmail.com");
		email.setAuthentication("...", "...");
		email.setSSL(true);
		email.setDebug(true);
		email.setSubject("The picture");
		try {
			email.addTo("charlyfarot@gmail.com", "Charly");
			email.setFrom("test@gmail.com", "Me");
			email.setMsg("Ci-joint la liste des films présnt sur le site;");
			// add the attachment
			email.attach(attachment);
			// send the email
			email.send();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("erreur envoi email");
		}

	}
}
