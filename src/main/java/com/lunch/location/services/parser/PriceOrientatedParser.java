package com.lunch.location.services.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PriceOrientatedParser implements MenuParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(PriceOrientatedParser.class);
	
	private Pattern euroPattern = Pattern.compile("(?=€).*");
	private Pattern pricePattern = Pattern.compile("\\d{1,2}.\\d{1,2}");

	@Override
	public List<Menu> getMenus(URL url) {
		List<Menu> result = new ArrayList<>();
		try {
			Document doc = Jsoup.connect(url.toString()).get();
			doc.getElementsMatchingOwnText(euroPattern).stream()
				.map(elem -> getMaxParent(elem))
				.forEach(elem -> {
//					System.out.println("next elem");
//					System.out.println(elem);
//					System.out.println("text");
//					System.out.println(elem.previousElementSibling() == null ? "" : elem.previousElementSibling().text());
//					System.out.println(getPreviousNonEmptyElement(elem).map(Element::text).orElse(""));
					System.out.println(elem.previousElementSibling().text());
					System.out.println(elem.text());
//					System.out.println(getNextNonEmptyElement(elem).map(Element::text).orElse(""));
				});
			
		} catch (IOException e) {
			LOGGER.error("Could not parse menu for {}", url);
		}
		return result;
	}
	
	private Optional<Element> getPreviousNonEmptyElement(Element e) {
		Element current = e;
		Element next = e.previousElementSibling();
		while (next != null && countPrices(next) == 0 && StringUtils.isBlank(next.text())) {
			current = next;
			next = current.previousElementSibling();
		}
		return Optional.ofNullable(current);
	}
	private Optional<Element> getNextNonEmptyElement(Element e) {
		Element current = e;
		Element next = e.nextElementSibling();
		while (next != null && countPrices(next) == 0 && StringUtils.isBlank(next.text())) {
			current = next;
			next = current.nextElementSibling();
		}
		return Optional.ofNullable(current);
	}
	
	private Element getMaxParent(Element price) {
		Element current = price;
		int priceCount = 0;
		while(current.hasParent() && priceCount < 2) {
			Element parent = current.parent();
			priceCount = countPrices(parent);
			if (priceCount < 2) {
				current = parent;
			}
		}
		return current;
	}

	private int countPrices(Element parent) {
		int currentPriceCount = 0;
		Matcher priceMatcher = pricePattern.matcher(parent.text());
		while (priceMatcher.find()) {
			currentPriceCount++;
		}
		return currentPriceCount;
	}

	public static void main(String[] args) throws MalformedURLException {
//		URL url = new URL("http://feinkosthafencity.de/#front-page-2");
//		URL url = new URL("http://www.thegreek.hamburg/speisekarte/");
//		URL url = new URL("https://www.avarina.de/vorspeisen.htm");
//		URL url = new URL("https://www.avarina.de/tagesgerichte.htm");
//		URL url = new URL("http://www.restaurant-india-house-hamburg.de/speisen/");
//		URL url = new URL("http://www.salathai.de/speisekarte/");
//		URL url = new URL("http://www.ciaomamma.de/index.php/menu");
		URL url = new URL("http://www.restaurant-fischmarkt.de/vorspeisen.html");
//		URL url = new URL("https://www.greenlovers.de/essen/salate-bowls");
		PriceOrientatedParser parser = new PriceOrientatedParser();
		parser.getMenus(url);
	}

}
