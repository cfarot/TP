package fr.iut.test;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import fr.iut.file.FileScanner;
import fr.iut.file.MapBySHA1FileHandler;

public class MapBySHA1FileHandlerTest {

	@Test
	public void testMBSha1FH(){
		File dir = new File("test");
		MapBySHA1FileHandler lh = new MapBySHA1FileHandler();
		new FileScanner().handleRecursively(dir, lh);
		Map<String, List<File>> map = lh.getResultMap();

		System.out.println("Test MapBySha1Filehandler");
		// affichage des doublons
		for (String mapKey : map.keySet()) {
			if (map.get(mapKey).size() > 1) {
				System.out.println(mapKey + " " + map.get(mapKey));
			}
		}
		System.out.println("");
	}
}
