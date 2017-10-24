package com.azserve.paginegialle;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class PagineGialleExcel {

	private static final String TITLE = "PROV.|DESCR.PROV.|REGIONE|CONTATTO|INDIRIZZO|CAP|LOCALITA|TELEFONI";
	private static final String EXCEL_FILE_NAME = "results.xls";
	private final String search;
	private final String filePathRoot;

	public PagineGialleExcel(final String search, final String filePathRoot) {
		this.search = search;
		this.filePathRoot = filePathRoot;
	}

	public void run() {

		try (Workbook wb = new HSSFWorkbook();
				FileOutputStream fileOut = new FileOutputStream(filePathRoot + EXCEL_FILE_NAME)) {

			ProvinceItaliane pi = new ProvinceItaliane();
			try {
				pi.provinceItaliane().forEach(s -> {
					System.out.println("PROVINCIA " + s);
					String[] datiProvincia = s.split("\\|");
					String siglaProvincia = datiProvincia[0];
					Sheet sheet = wb.createSheet(siglaProvincia);
					System.out.println("Sigla " + siglaProvincia);
					try {
						outputToFile(sheet, siglaProvincia,
								PagineGialleLocalSearch.getInstance(siglaProvincia, search, s).localResults());
//						wb.write(fileOut);
//						fileOut.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
			wb.write(fileOut);
			fileOut.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private void outputToFile(Sheet sheet, String siglaProvincia, List<String> elements) {
		Row rowTitle = sheet.createRow(0);
		writeRow(rowTitle,TITLE);
		elements.forEach(el -> {
			try {
				Integer lastRow = sheet.getLastRowNum();
				Row row = sheet.createRow(lastRow + 1);
				writeRow(row, el);
				// writer.write(el + System.lineSeparator());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}

	private void writeRow(Row row, String elementiRiga) {
		// TODO Auto-generated method stub
		String[] els = elementiRiga.split("\\|");
		int i=0;
		for(String s:els){
			Cell cell = row.createCell(i++);
			cell.setCellValue(s);
		}
	}
}
