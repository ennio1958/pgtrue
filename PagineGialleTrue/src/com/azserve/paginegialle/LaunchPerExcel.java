package com.azserve.paginegialle;

import java.io.IOException;

public class LaunchPerExcel {
	private static final String SEARCH = "ristorante";
	private static final String FILE_PATH_ROOT = "c:/temp/ristorante/";
	
	public static void main(String[] args) throws IOException {
		PagineGialleExcel x = new PagineGialleExcel(SEARCH,FILE_PATH_ROOT);
		x.run();
	}
}
