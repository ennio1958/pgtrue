package com.azserve.paginegialle;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class PagineGialle {

	private final String search;
	private final String filePathRoot;

	public PagineGialle(final String search, final String filePathRoot) {
		this.search = search;
		this.filePathRoot = filePathRoot;
	}

	public void run() {
		ProvinceItaliane pi = new ProvinceItaliane();
		try {
			pi.provinceItaliane().forEach(s -> {
				System.out.println("PROVINCIA " + s);
				String[] datiProvincia = s.split("\\|");
				String siglaProvincia = datiProvincia[0];
				System.out.println("Sigla " + siglaProvincia);
				try {
					outputToFile(siglaProvincia, PagineGialleLocalSearch.getInstance(siglaProvincia, search, s).localResults());
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void outputToFile(String siglaProvincia, List<String> elements) {
		Path path = Paths.get(filePathRoot + siglaProvincia + ".txt");
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
}
