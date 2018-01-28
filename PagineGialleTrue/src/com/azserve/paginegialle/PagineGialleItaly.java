package com.azserve.paginegialle;

public class PagineGialleItaly extends AbstractPagineGialle{

	public PagineGialleItaly(String search, String filePathRoot) {
		super(search, filePathRoot);
	}

	@Override
	protected void run() {
		try {
			outputToFile("ITALY", PagineGialleSearch.getInstance(search).results(1, Integer.MAX_VALUE));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
