package fr.iut.test;

import java.io.File;

import org.junit.Test;

import fr.iut.file.Sha1File;

public class Sha1FileTest {
	@Test
	public void testSha1F(){
		
		File f = new File("test/bidule.txt");
		
		System.out.println("TestSha1 sur un fichier");
		System.out.println(Sha1File.getSha1(f));
		System.out.println("");
	}
}
