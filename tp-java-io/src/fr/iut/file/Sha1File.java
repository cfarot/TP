package fr.iut.file;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha1File {

	public static String getSha1(File f){
		
		InputStream in = null;	
		String line;
		String chaine = "";
		String chaine2 = "";
		try {
			in = new BufferedInputStream(new FileInputStream(f));
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			while((line=reader.readLine())!= null){
				chaine+=line;		
			}	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally { 
			if (in != null) 
				try { in.close(); 
				} catch (IOException ex){ } 
		}
		
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA1");
			md.update(("blob " + f.length()).getBytes());
			md.update((byte) 0);	
			md.update(chaine.getBytes());
		
			chaine2 = Hex.toHex(md.digest());
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return chaine2;
	}
}
