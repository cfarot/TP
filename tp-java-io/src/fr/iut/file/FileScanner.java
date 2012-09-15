package fr.iut.file;

import java.io.File;

public class FileScanner {

	public void handleRecursively(File dir, FileHandler handler) {
		// TODO ... call handler.handleFile(...) for each sub-dir/file in dir
		if (dir.isDirectory()) {
			File[] lstFile = dir.listFiles();
			//System.out.println(lstFile[0]);
			if (lstFile != null) {
				for (int i = 0; i < lstFile.length; i++) {
					if (lstFile[i].isDirectory()) {
						handleRecursively(lstFile[i], handler);
					}
					else{
						handler.handleFile(lstFile[i]);
					}
				}
			}
		} else {
			handler.handleFile(dir);
		}
	}
}
