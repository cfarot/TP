package fr.iut.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class TreeBySizeFileHandler implements FileHandler {

	private TreeMap<Long,List<File>> treeM;
	
	
	public TreeBySizeFileHandler() {
		this.treeM = new TreeMap<Long, List<File>>();
	}

	@Override
	public void handleFile(File file) {
		registerInTree(this.treeM, file.length(), file);
	}

	public void registerInTree(TreeMap<Long,List<File>> tree, Long key, File value) {
		List<File> ls = tree.get(key);
		if (ls == null) {
			ls = new ArrayList<File>();
			tree.put(key, ls);
		}
		ls.add(value);
	}

	public TreeMap<Long,List<File>> getResultTree() {	
		return this.treeM;
	}
}
