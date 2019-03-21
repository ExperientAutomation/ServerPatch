package utils;

import java.io.File;
import java.io.FileFilter;

public class latestFile {

	public static File lastFileModified(String dir) {
		File fl = new File(dir);
		File[] files = fl.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isFile();
			}
		});
		long lastMod = Long.MIN_VALUE;
		File doc = null;
		for (File file : files) {
			if (file.lastModified() > lastMod) {
				doc = file;
				lastMod = file.lastModified();
			}
		}
		return doc;
	}
}
