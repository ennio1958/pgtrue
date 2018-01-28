package com.azserve.paginegialle;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.azserve.sme.commons.database.ConnectionFactoryMysql;

public class ExportDbToExcel {

	private static final String FILE_HOME_DIR = "C:/temp/ristorantebis/italy/all/";
	private static final String EXCEL_FILE_NAME = "results.xls";
	private static final String TITLE =  "REGIONE|PROV.|DESCR.PROV.|LOCALITA|CONTATTO|INDIRIZZO|CAP|TELEFONI";

	public static void main(String[] args) throws Exception {
		Connection conn = ConnectionFactoryMysql.getConnection("localhost", "bar", "root", "IncredibleLength0109");
		String sql = "select * from elenco_bar order by regione,siglaProvincia, localita, nomeEsercizio";
		
		try (Workbook wb = new HSSFWorkbook();
				FileOutputStream fileOut = new FileOutputStream(FILE_HOME_DIR + EXCEL_FILE_NAME)) {

			try(Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)){
				String currentSiglaProvincia="";
				Sheet sheet=null;
				while(rs.next()){
					String siglaProvincia = rs.getString("siglaProvincia");
					if(!siglaProvincia.equalsIgnoreCase(currentSiglaProvincia)){
						currentSiglaProvincia=siglaProvincia;
						sheet = wb.createSheet(siglaProvincia);
						writeRow(sheet,TITLE);
					}
					writeRow(sheet,elements(rs));
					System.out.println(rs.getString("nomeEsercizio"));
				}
			}catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
			}
			wb.write(fileOut);
			fileOut.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
	
	
	

	private static String elements(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		
		StringBuffer sb = new StringBuffer();
		sb.append(rs.getString("regione"));
		sb.append("|");
		sb.append(rs.getString("siglaProvincia"));
		sb.append("|");
		sb.append(rs.getString("nomeProvincia"));
		sb.append("|");
		sb.append(rs.getString("localita"));
		sb.append("|");
		sb.append(rs.getString("nomeEsercizio"));
		sb.append("|");
		sb.append(rs.getString("indirizzo"));
		sb.append("|");
		sb.append(rs.getString("cap"));
		sb.append("|");
		sb.append(rs.getString("telefoni"));
		return sb.toString();
	}




	private static void writeRow(Sheet sheet, String elements) {
		Integer lastRow = sheet.getLastRowNum();
		if(lastRow==null)
			lastRow = -1;
		Row row = sheet.createRow(lastRow + 1);
		String[] els = elements.split("\\|");
		int i = 0;
		for (String s : els) {
			Cell cell = row.createCell(i++);
			cell.setCellValue(s);
		}
	}
	
	
}
