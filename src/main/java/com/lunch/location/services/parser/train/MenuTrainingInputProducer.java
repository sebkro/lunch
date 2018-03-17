package com.lunch.location.services.parser.train;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.lunch.location.services.parser.MenuCandidate;
import com.lunch.location.services.parser.PriceOrientatedParser;

@Service
public class MenuTrainingInputProducer {

	public static Optional<String> toJson(MenuCandidate o, ObjectMapper mapper) {
		try {
			return Optional.of(mapper.writeValueAsString(o));
		} catch (JsonProcessingException e) {
			return Optional.empty();
		}
	}
	
	public static void writeToFile(String line, PrintWriter fw) {
		fw.write(line);
	}

	public static void main(String[] args) throws IOException {
		
		List<String> urls = Lists.newArrayList("http://www.thegreek.hamburg/speisekarte/",
				"https://www.avarina.de/vorspeisen.htm",
				"https://www.avarina.de/tagesgerichte.htm",
				"http://www.restaurant-india-house-hamburg.de/speisen/",
				"http://www.ciaomamma.de/index.php/menu",
				"https://www.greenlovers.de/essen/salate-bowls",
				"https://www.maredo.de/speisekarte/steaks/",
				"https://www.maredo.de/speisekarte/salate/",
				"https://www.maredo.de/speisekarte/vorspeisen-suppen/",
				"https://www.maredo.de/speisekarte/klassiker/",
				"https://www.maredo.de/speisekarte/burger/",
				"https://www.maredo.de/lunch/",
				"http://feinkosthafencity.de/",
				"https://www.aalspeicher.de/",
				"http://www.andronaco.info/standorte/hamburg-hafencity/bistro-speisekarte/",
				"http://www.4mosa-restaurant.de/speisekarte/",
				"http://dergrieche.biz/vorsp.html",
				"http://dergrieche.biz/salat.html",
				"http://dergrieche.biz/suppe.html",
				"http://dergrieche.biz/grill.html",
				"http://dergrieche.biz/imbiss.html",
				"http://dergrieche.biz/mix.html",
				"http://dergrieche.biz/fisch.html",
				"http://www.parkblick-hamburg.de/speisekarte/hauptspeisen/",
				"http://qasr-restaurant.de/#menu",
				"http://www.trattoriaitaliana.de/de/138290-MITTAGSKARTE",
				"http://www.trattoriaitaliana.de/de/138286-PIZZA",
				"http://www.trattoriaitaliana.de/de/138287-VORSPEISEN",
				"http://www.trattoriaitaliana.de/de/138288-HAUPTSPEISEN"
				
				);
		PriceOrientatedParser parser = new PriceOrientatedParser();
		ObjectMapper mapper = new ObjectMapper();
		
		PrintWriter fw = new PrintWriter("/Users/kromes/Desktop/trainDataPrevNext.txt");
		List<List<MenuCandidate>> menus = urls.parallelStream()
				.map(elem -> parser.getMenus(elem))
				.collect(Collectors.toList());
		for (List<MenuCandidate> site : menus) {
			for (int i = 0; i < site.size(); i++) {
				String prevRoot = i > 0 ? site.get(i - 1).getRootElement() : "";
				String nextRoot = i < site.size() - 1 ? site.get(i + 1).getRootElement() : "";
				MenuCandidate current = site.get(i);
				current.getNextElements().forEach(ne -> {
					fw.println("root1: " + current.getRootElement());
					fw.println("root2: " + nextRoot);
					fw.println("elem: " + ne);
				});
				current.getPrevElements().forEach(ne -> {
					fw.println("root1: " + current.getRootElement());
					fw.println("root2: " + prevRoot);
					fw.println("elem: " + ne);
				});
				
			}
			
		}
		
		fw.close();
	}

}
