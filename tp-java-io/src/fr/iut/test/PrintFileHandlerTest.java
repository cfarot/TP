package fr.iut.test;

import java.io.File;

import org.junit.Test;

import fr.iut.file.FileScanner;
import fr.iut.file.PrintFileHandler;

public class PrintFileHandlerTest {
	@Test
	public void testPFH(){
		// prepare  perform
		File f = new File("test");
		FileScanner fs = new FileScanner();
		PrintFileHandler handler = new PrintFileHandler();
		
		// post check
		System.out.println("Test PrintFileHandler");
		fs.handleRecursively(f, handler);
		System.out.println("");
	}
}
