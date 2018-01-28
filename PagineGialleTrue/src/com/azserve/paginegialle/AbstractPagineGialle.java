package com.azserve.paginegialle;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public abstract class AbstractPagineGialle{

	protected final String search;
	protected final String filePathRoot;

	public AbstractPagineGialle(final String search, final String filePathRoot) {
		this.search = search;
		this.filePathRoot = filePathRoot;
	}

	protected void outputToFile(String nomeFile, List<String> elements) {
		if(elements==null || elements.isEmpty())
			return;
		Path path = Paths.get(filePathRoot+"/" + nomeFile + ".txt");
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			elements.forEach(el -> {
				try {
					writer.write(el + System.lineSeparator());
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	protected abstract void run();

}
