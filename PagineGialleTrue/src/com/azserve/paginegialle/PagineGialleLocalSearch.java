package com.azserve.paginegialle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PagineGialleLocalSearch {
	private static final String DOMINIO = "https://www.paginegialle.it/ricerca/";
	private static final String NEXT_CLASS_NAME = ".paginationBtn.arrowBtn.rightArrowBtn";

	private final String search;
	private final String local;
	private final String prefix;
	
	private PagineGialleLocalSearch(final String local, final String search, final String prefix) {
		// TODO Auto-generated constructor stub
		this.local=local;
		this.search=search;
		this.prefix=prefix;
	}
	
	public static PagineGialleLocalSearch getInstance(String local, String search, String prefix){
		return new PagineGialleLocalSearch(local, search, prefix);
	}
	public List<String> localResults() throws IOException {
		List<String> ris = new ArrayList<String>();
		String link = DOMINIO + search + "/" + local;
		Document doc = Jsoup.connect(link).get();
		Elements ncn = doc.select(NEXT_CLASS_NAME);
		int numeroPagina = 0;
		while (!ncn.isEmpty()) {
			System.out.println("PAGINA " + ++numeroPagina);
			extractPage(ris, doc);
			link = ncn.get(0).attr("href");
			doc = Jsoup.connect(link).get();
			ncn = doc.select(NEXT_CLASS_NAME);
		}
		extractPage(ris, doc);

		return ris;
	}
	
	private void extractPage(List<String> ris, Document doc) {
		Elements tableElements = doc.select("div.table");
		for (Element e : tableElements) {
			StringBuffer sb = new StringBuffer();
			sb.append(searchAttribute(e, "name"));
			sb.append("|");
			sb.append(searchAttribute(e, "streetAddress"));
			sb.append("|");
			sb.append(searchAttribute(e, "postalCode"));
			sb.append("|");
			sb.append(searchAttribute(e, "addressLocality"));
			sb.append("|");
			sb.append(searchAttribute(e, "addressRegion"));
			sb.append("|");
			sb.append(searchAttribute(e, "telephone"));

			ris.add(prefix + "|" + sb.toString());
		}
	}
	

	private String searchAttribute(Element element, String attrName) {
		Elements aName = element.select("[itemprop=" + attrName + "]");
		if (!aName.isEmpty()) {
			return aName.get(0).text();
		} else
			return "";
	}
	
}
