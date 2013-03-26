package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import models.Movie;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import play.jobs.Job;
import play.jobs.On;

@On("0 */5 * * * ?")
public class Jobs extends Job {

	public void doJob() {

		System.out.println("Envoi de la liste des films");

		try {
			XSSFWorkbook wb2 = new XSSFWorkbook("templateFilms.xlsx");
			XSSFSheet sheet2 = wb2.getSheet("Films");
			List<Movie> lstMovie = Movie.findAll();
			if (lstMovie.size() > 0) {
				for (int i = 0; i < lstMovie.size(); i++) {
					sheet2.createRow(i + 2).createCell(0)
							.setCellValue(lstMovie.get(i).Title);
				}
			} else {
				sheet2.createRow(2)
						.createCell(0)
						.setCellValue(
								"Il n'y a pas de films pour le moment sur le site.");
			}
			FileOutputStream fileOut;

			fileOut = new FileOutputStream("ListeFilms.xlsx");
			wb2.write(fileOut);
			fileOut.close();
			
			// Create the attachment
			EmailAttachment attachment = new EmailAttachment();
			attachment.setPath("ListeFilms.xlsx");
			attachment.setDisposition(EmailAttachment.ATTACHMENT);
			attachment.setDescription("Liste films");
			attachment.setName("Films.xlsx");

			// Create the email message
			MultiPartEmail email = new MultiPartEmail();
			email.setHostName("smtp.gmail.com");
			email.setAuthentication("", "");
			email.setSSL(true);
			email.setDebug(true);
			email.setSubject("Liste des films du site");
			email.addTo("charlyfarot@gmail.com", "Charly");
			email.setFrom("test@gmail.com", "Me");
			email.setMsg("Ci-joint la liste des films présent sur le site.");
			email.attach(attachment);
			email.send();			

		} catch (IOException | EmailException e1) {
			e1.printStackTrace();
		} 
	}
}
