package fr.iut.test;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import fr.iut.file.FileScanner;
import fr.iut.file.MapByFullNameFileHandler;

public class MapByFullNameFileHandlerTest {

	@Test
	public void testMBFNFH(){
		//prepare
		File dir = new File("test");
		MapByFullNameFileHandler lh = new MapByFullNameFileHandler();
		
		// perform
		new FileScanner(). handleRecursively(dir, lh);
		Map<String, List<File>> map = lh.getResultMap();
		
		//post-check
		System.out.println("Test MapByFullNameFileHandler");
		// affichage des doublons
		for (String mapKey : map.keySet()) {
			if(map.get(mapKey).size() >1){
				System.out.println(mapKey+" "+map.get(mapKey));
			}
		}
		System.out.println("");
	}
}
