package com.azserve.paginegialle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PagineGialleSearch {
	private static final String HOME="https://www.paginegialle.it";
	private static final String DOMINIO = "https://www.paginegialle.it/ricerca/";
	private static final String NEXT_CLASS_NAME = ".paginationBtn.arrowBtn.rightArrowBtn";

	private final String search;
	private final String local;
	private final String prefix;
	
	private PagineGialleSearch(final String local, final String search, final String prefix) {
		this.local=local;
		this.search=search;
		this.prefix=prefix;
	}
	
	private PagineGialleSearch(final String search) {
		this.local=null;
		this.search=search;
		this.prefix=null;
	}
	public static PagineGialleSearch getInstance(String search){
		return new PagineGialleSearch(search);
	}
	public static PagineGialleSearch getInstance(String local, String search, String prefix){
		return new PagineGialleSearch(local, search, prefix);
	}
	public List<String> localResults() throws IOException {
		List<String> ris = new ArrayList<String>();
		try {
			String longLocal = prefix.split("\\|")[1];
			String link = DOMINIO + search + "/" + longLocal;
			Document doc = Jsoup.connect(link).get();
			Elements ncn = doc.select(NEXT_CLASS_NAME);
			int numeroPagina = 0;
			while (!ncn.isEmpty()) {
				System.out.println("PAGINA " + ++numeroPagina);
				extractPage(ris, doc);
				link = HOME+ncn.get(0).attr("href");
				doc = Jsoup.connect(link).get();
				ncn = doc.select(NEXT_CLASS_NAME);
			}
			extractPage(ris, doc);

			return ris;
		} catch (Exception e) {
			e.printStackTrace();
			return ris;
		}
	}
	public List<String> results(int startPage, int endPage) {
		List<String> ris = new ArrayList<String>();
		try {
			
			String link = DOMINIO + search;
			Document doc = Jsoup.connect(link).get();
			Elements ncn = doc.select(NEXT_CLASS_NAME);
			int numeroPagina = 0;
			while (!ncn.isEmpty() && numeroPagina<endPage) {
				System.out.println("PAGINA " + ++numeroPagina);
				if(numeroPagina>=startPage){
				extractPage(ris, doc);
				}
				link = HOME+ncn.get(0).attr("href");
				doc = Jsoup.connect(link).get();
				ncn = doc.select(NEXT_CLASS_NAME);
			}

			return ris;
		} catch (Exception e) {
			e.printStackTrace();
			return ris;
		}
	}
	
	
	private boolean inRange(int probe, int start, int end) {
		return probe>=start && probe<=end;
	}

	private void extractPage(List<String> ris, Document doc) {
		Elements tableElements = doc.select("div.table");
		for (Element e : tableElements) {
			StringBuffer sb = new StringBuffer();
			String provincia = searchClassAttribute(e, "region").replaceAll("\\(", "").replaceAll("\\)","");
			if((local!=null) && ProvinceItaliane.normalizedSigla(provincia).equalsIgnoreCase(ProvinceItaliane.normalizedSigla(local)) || (local==null)){
			sb.append(searchItemPropAttribute(e, "name"));
			sb.append("|");
			sb.append(searchClassAttribute(e, "street-address"));
			sb.append("|");
			sb.append(searchClassAttribute(e, "postal-code"));
			sb.append("|");
			sb.append(searchClassAttribute(e, "locality"));
			sb.append("|");
			sb.append(provincia);
			sb.append("|");
			sb.append(searchClassAttribute(e, "phoneNumbers"));
			ris.add(prefix + "|" + sb.toString());
			}
		}
	}
	

	private String searchItemPropAttribute(Element element, String attrName) {
		Elements aName = element.select("[itemprop=" + attrName + "]");
		if (!aName.isEmpty()) {
			return cleanText(aName.get(0).text());
		} else
			return "";
	}

	private String searchClassAttribute(Element element, String attrName) {
		Elements aName = element.select("[class=" + attrName + "]");
		if (!aName.isEmpty()) {
			return cleanText(aName.get(0).text());
		} else
			return "";
	}

	private  String cleanText(String text) {
		return text.replaceAll("\\|", "").replaceAll("\"", "").replaceAll("\'", "");
	}

	
}
