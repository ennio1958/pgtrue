package com.azserve.paginegialle;

import java.io.IOException;

public class PagineGialleLocal extends AbstractPagineGialle {

	public PagineGialleLocal(String search, String filePathRoot) {
		super(search, filePathRoot);
	}

	@Override
	protected void run() {
		ProvinceItaliane pi = new ProvinceItaliane();
		try {
			//TODO TODO TODO DEBUG ATTENZIONE
			//TOgliere filter su MB e PU
			pi.provinceItaliane().stream().filter(s->s.startsWith("MB")||s.startsWith("PU")).forEach(s -> {
				System.out.println("PROVINCIA " + s);
				String[] datiProvincia = s.split("\\|");
				String siglaProvincia = datiProvincia[0];
				System.out.println("Sigla " + siglaProvincia);
				// casi particolari: MB Monza Brianza e PU Pesaro Urbino
				if (siglaProvincia.equalsIgnoreCase("MB")) {
					try {
						outputToFile("Monza", PagineGialleSearch
								.getInstance(siglaProvincia, search, "MB|Monza|Lombardia").localResults());
						outputToFile("Brianza", PagineGialleSearch
								.getInstance(siglaProvincia, search, "MB|Brianza|Lombardia").localResults());
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (siglaProvincia.equalsIgnoreCase("PU")) {
					try {
						outputToFile("Pesaro", PagineGialleSearch
								.getInstance(siglaProvincia, search, "PU|Pesaro|Marche").localResults());
						outputToFile("Urbino", PagineGialleSearch
								.getInstance(siglaProvincia, search, "PU|Urbino|Marche").localResults());
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {

					try {
						outputToFile(siglaProvincia,
								PagineGialleSearch.getInstance(siglaProvincia, search, s).localResults());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
