package com.azserve.paginegialle;

import java.io.IOException;

public class Launch {
	private static final String SEARCH = "scarpe";
	private static final String FILE_PATH_ROOT = "c:/temp/ristorante/";
	
	public static void main(String[] args) throws IOException {
		PagineGialle x = new PagineGialle(SEARCH,FILE_PATH_ROOT);
		x.run();
	}
}
