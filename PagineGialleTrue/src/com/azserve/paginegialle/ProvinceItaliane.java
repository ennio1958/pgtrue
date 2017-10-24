package com.azserve.paginegialle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ProvinceItaliane {

	private final static String FONTE = "http://www.aci.it/i-servizi/normative/codice-della-strada/elenco-sigle-province-ditalia.html";

	public static void main(String[] args) throws IOException {
		ProvinceItaliane x = new ProvinceItaliane();
		x.provinceItaliane().forEach(s->System.out.println(s));
	}
	public List<String> provinceItaliane() throws IOException{
		List<String> ris = new ArrayList<String>();
		Document doc = Jsoup.connect(FONTE).get();
		Element esTable = doc.select("table[summary]").get(0);
		System.out.println(esTable.attr("summary"));
		Elements provinceDoc = esTable.select("tr");
		provinceDoc.remove(0);
		for(Element p:provinceDoc){
			Elements fs = p.select("td");
			ris.add(fs.get(0).text()+"|"+fs.get(1).text()+"|"+fs.get(2).text());
		}
		return ris;
	}
}
