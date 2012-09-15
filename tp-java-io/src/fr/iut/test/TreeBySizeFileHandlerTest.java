package fr.iut.test;

import java.io.File;
import java.util.List;
import java.util.TreeMap;

import org.junit.Test;

import fr.iut.file.FileScanner;
import fr.iut.file.TreeBySizeFileHandler;

public class TreeBySizeFileHandlerTest {

	@Test
	public void testTBSFH(){
		//prepare
		File dir = new File("test");
		TreeBySizeFileHandler lh = new TreeBySizeFileHandler();
		
		//perform
		new FileScanner(). handleRecursively(dir, lh);
		TreeMap<Long,List<File>> tree = lh.getResultTree();	
		
		//post check
		System.out.println("Test TreeBySizeFileHandler");
		System.out.println("     Nombre de fichier: "+tree.size()+"\n");
		System.out.println("Top 10 bigFiles:\n");
		int count=1;
		for (Long mapKey : tree.descendingMap().keySet()) {
			System.out.println(count+" - "+mapKey+" octets "+tree.get(mapKey));
			count++;
			if(count==11)break;
		}
		System.out.println("");
	}
}
