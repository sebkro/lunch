package com.lunch.location.services.parser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

public class CiaoMammaParser implements MenuParser {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CiaoMammaParser.class);
	private static final Set<String> headers = Sets.newHashSet("fleisch", "fisch", "pasta", "pizza", "antipasti", "suppen", "salate");
	
	@Override
	public List<Menu> getMenus(URL url) {
		List<Menu> result = new ArrayList<>();
		try {
			Document doc = Jsoup.connect(url.toString()).get();
			result = doc.getElementsByTag("h3").stream()
				.filter(elem -> headers.contains(elem.text().trim().toLowerCase()))
				.map(elem -> elem.nextElementSibling().getElementsByTag("dt"))
				.flatMap(elem -> elem.stream())
				.map(elem -> toMenu(elem))
				.collect(Collectors.toList());
		} catch (Exception e) {
			LOGGER.error("Could not parse menu for {} with {}", url , CiaoMammaParser.class);
		}
		return result;
	}
	
	private Menu toMenu(Element dishNameElement) {
		String title = dishNameElement.text();
		String description = "";
		String price = "";
		Element details = dishNameElement.nextElementSibling();
		Elements descriptionElement = details.getElementsByTag("small");
		if (descriptionElement != null) {
			description = descriptionElement.text();
		}
		Elements priceElem = details.getElementsByTag("strong");
		if (priceElem != null) {
			price = priceElem.text();
		}
		return Menu.builder()
			.title(title)
			.description(description)
			.price(price).build();
	}

	public static void main(String[] args) throws MalformedURLException {
		URL url = new URL("http://www.ciaomamma.de/index.php/menu");
		CiaoMammaParser parser = new CiaoMammaParser();
		parser.getMenus(url).forEach(System.out::println);
	}

}
