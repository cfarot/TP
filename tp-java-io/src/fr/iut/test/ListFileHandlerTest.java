package fr.iut.test;

import java.io.File;
import java.util.List;

import org.junit.Test;

import fr.iut.file.FileScanner;
import fr.iut.file.ListFileHandler;

public class ListFileHandlerTest {

	@Test
	public void testLFH(){
		//prepare
		File dir = new File("test");
		ListFileHandler lh = new ListFileHandler();
		
		//perform
		new FileScanner(). handleRecursively(dir, lh);
		List<File> files = lh.getResultList();
		
		// post check
		System.out.println("Test ListFileHandler");
		System.out.println("found files:" + files);
		System.out.println("");
	}
}
