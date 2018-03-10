package com.lunch.location.services.parser;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GreenLoversParser implements MenuParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(GreenLoversParser.class);

	@Override
	public List<Menu> getMenus(URL url) {
		List<Menu> result = new ArrayList<>();
		try {
			Document doc = Jsoup.connect(url.toString()).get();
			result = doc.select(".content-left>h5").stream()
				.map(elem -> parseMenu(elem))
				.collect(Collectors.toList());
		} catch (IOException e) {
			LOGGER.error("Could not parse menu for {}", url);
		}
		return result;
	}

	private Menu parseMenu(Element menuElement) {
		String title = menuElement.text().replaceAll("\\d+.\\d+.?€", "");
		String price = menuElement.text().replaceAll("[^\\d€,\\.]*", "");
		Menu result = Menu.builder()
				.title(title.trim())
				.price(price.trim()).build();
		return result;
	}

//	public static void main(String[] args) throws MalformedURLException {
//		URL url = new URL("https://www.greenlovers.de/essen/salate-bowls");
//		GreenLoversParser parser = new GreenLoversParser();
//		parser.getMenus(url).forEach(System.out::println);
//	}

}
