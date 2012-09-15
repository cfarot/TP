package fr.iut.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListFileHandler implements FileHandler {

	private List<File> lstFile;

	public ListFileHandler() {
		this.lstFile = new ArrayList<File>();
	}

	@Override
	public void handleFile(File file) {
		this.lstFile.add(file);

	}

	public List<File> getResultList() {
		return this.lstFile;
	}
}
