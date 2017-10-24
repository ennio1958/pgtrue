package com.azserve.paginegialle;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PagineGialle {

	private static final String DOMINIO = "https://www.paginegialle.it/ricerca/";
	private static final String NEXT_CLASS_NAME = ".paginationBtn.arrowBtn.rightArrowBtn";
	private final String search;
	private final String filePathRoot;
	
	public PagineGialle(final String search, final String filePathRoot) {
		this.search=search;
		this.filePathRoot=filePathRoot;
	}

	
	public void run(){
		ProvinceItaliane pi = new ProvinceItaliane();
		try {
			pi.provinceItaliane().forEach(s->{
				System.out.println("PROVINCIA "+s);
				String[] datiProvincia = s.split("\\|");
				String siglaProvincia = datiProvincia[0];
				System.out.println("Sigla " + siglaProvincia);
				try {
					outputToFile(siglaProvincia, searchProvincia(s,siglaProvincia));
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private List<String> searchProvincia(String prefixGeoData, String sigla) throws IOException{
		List<String> ris = new ArrayList<String>();
		String link = DOMINIO+search+"/"+sigla;
		Document doc = Jsoup.connect(link).get();
		Elements ncn = doc.select(NEXT_CLASS_NAME);
		int numeroPagina=0;
		while(!ncn.isEmpty()){
			System.out.println("PAGINA " + ++numeroPagina);
			extractPage(prefixGeoData, ris, doc);
			link = ncn.get(0).attr("href");
			doc = Jsoup.connect(link).get();
			ncn = doc.select(NEXT_CLASS_NAME);
		}
		extractPage(prefixGeoData, ris, doc);
		
		return ris;
	}


	private void outputToFile(String siglaProvincia,
			List<String> barProvincia) {
		//Get the file reference
		Path path = Paths.get(filePathRoot+siglaProvincia+".txt");
		 
		//Use try-with-resource to get auto-closeable writer instance
		try (BufferedWriter writer = Files.newBufferedWriter(path))
		{
			barProvincia.forEach(b->{
			    try {
					writer.write(b+System.lineSeparator());
				} catch (Exception e) {
					e.printStackTrace();
				}
		});
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	private void extractPage(String prefixGeoData, List<String> ris, Document doc) {
		Elements tableElements = doc.select("div.table");
		for(Element bar:tableElements){
			StringBuffer sb = new StringBuffer();
			sb.append(searchAttribute(bar, "name"));
			sb.append("|");
			sb.append(searchAttribute(bar, "streetAddress"));
			sb.append("|");
			sb.append(searchAttribute(bar, "postalCode"));
			sb.append("|");
			sb.append(searchAttribute(bar, "addressLocality"));
			sb.append("|");
			sb.append(searchAttribute(bar, "telephone"));
			
			ris.add(prefixGeoData+"|"+sb.toString());
		}
	}
	
	private String searchAttribute(Element bar, String attrName){
		Elements aName = bar.select("[itemprop="+attrName+"]");
		if(!aName.isEmpty()){
			return aName.get(0).text();
		} else
			return "";
	}

}
