package fr.iut.file;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapBySHA1FileHandler implements FileHandler {

	private Map<String, List<File>> map;

	public MapBySHA1FileHandler() {
		// TODO Auto-generated constructor stub
		this.map = new HashMap<String, List<File>>();
	}

	@Override
	public void handleFile(File file) {
		registerInList(this.map, Sha1File.getSha1(file), file);
	}

	public void registerInList(Map<String, List<File>> map, String key,
			File value) {
		List<File> ls = map.get(key);
		if (ls == null) {
			ls = new ArrayList<File>();
			map.put(key, ls);
		}
		ls.add(value);
	}

	public Map<String, List<File>> getResultMap() {
		return this.map;
	}
}
