package fr.iut.file;

import java.io.File;

public class PrintFileHandler implements FileHandler {

	@Override
	public void handleFile(File file) {
		System.out.println(file.getAbsolutePath());
	}
}
