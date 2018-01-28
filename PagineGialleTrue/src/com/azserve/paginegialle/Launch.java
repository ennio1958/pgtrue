package com.azserve.paginegialle;

import java.io.IOException;

public class Launch {
	private static final String SEARCH = "ristorante";
	private static final String FILE_PATH_ROOT = "c:/temp/ristorantebis/italy";
	
	public static void main(String[] args) throws IOException {
//		PagineGialleItaly x = new PagineGialleItaly(SEARCH,FILE_PATH_ROOT);
//		x.run();
		PagineGialleLocal y = new PagineGialleLocal(SEARCH,FILE_PATH_ROOT);
		y.run();
	}
}
