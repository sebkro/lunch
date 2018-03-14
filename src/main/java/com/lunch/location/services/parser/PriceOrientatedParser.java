package com.lunch.location.services.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.javatuples.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PriceOrientatedParser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PriceFocusedParser.class);

	public static final Pattern pricePattern = Pattern.compile("\\d{1,2}[.,]\\d{1,2}([^\\.\\d]|$)");

	public List<Pair<Element, List<Element>>> getMenus(String url) {
		List<Pair<Element, List<Element>>> result = new ArrayList<>();
		try {
			
			Document doc = Jsoup.connect(url.toString()).timeout(10000).get();
			return doc.getElementsMatchingOwnText(pricePattern).stream().map(elem -> toMenu(elem))
					.collect(Collectors.toList());

		} catch (Exception e) {
			LOGGER.error("Could not parse menu for {}", url);
		}
		return result;
	}
	
	private Pair<Element, List<Element>> toMenu(Element priceElement) {
		Element maxParent = getMaxParent(priceElement);
		List<Element> previousElements = getPreviousNonEmptyElement(maxParent).stream()
				.collect(Collectors.toList());
		return new Pair<Element, List<Element>>(maxParent, previousElements);
	}

	private List<Element> getPreviousNonEmptyElement(Element e) {
		return findSiblingsNonEmptyElement(e, elem -> elem.previousElementSibling());
	}

	private List<Element> findSiblingsNonEmptyElement(Element start, Function<Element, Element> traverser) {
		List<Element> result = new ArrayList<>();
		Element current = null;
		Element next = traverser.apply(start);
		while (next != null && countPrices(next) == 0) {
			current = next;
			if (StringUtils.isNotBlank(current.text())) {
				result.add(current);
			}
			next = traverser.apply(current);
		}
		return result;
	}

	private Element getMaxParent(Element price) {
		int startPriceCount = countPrices(price);
		Element current = price;
		int priceCount = 0;
		while (current.hasParent() && priceCount <= startPriceCount) {
			Element parent = current.parent();
			priceCount = countPrices(parent);
			if (priceCount <= startPriceCount) {
				current = parent;
			}
		}
		return current;
	}

	private int countPrices(Element element) {
		return getPrices(element).size();
	}
	
	public List<String> getPrices(Element element) {
		List<String> result = new ArrayList<>();
		Matcher priceMatcher = pricePattern.matcher(element.text());
		while (priceMatcher.find()) {
			result.add(priceMatcher.group());
		}
		return result;
	}

	public static void main(String[] args) {
//		 URL url = new URL("http://feinkosthafencity.de/#front-page-2");
//		 URL url = new URL("http://www.thegreek.hamburg/speisekarte/");
		// URL url = new URL("https://www.avarina.de/vorspeisen.htm");
		// URL url = new URL("https://www.avarina.de/tagesgerichte.htm");
//		 URL url = new URL("http://www.restaurant-india-house-hamburg.de/speisen/");
		// URL url = new URL("http://www.salathai.de/speisekarte/");
//		URL url = new URL("http://www.ciaomamma.de/index.php/menu");
		// URL url = new
		// URL("http://www.restaurant-fischmarkt.de/vorspeisen.html");
//		 URL url = new URL("https://www.greenlovers.de/essen/salate-bowls");
//		 URL url = new URL("https://www.maredo.de/lunch/");
//		 URL url = new URL("http://feinkosthafencity.de/");
//		 URL url = new URL("https://www.maredo.de/lunch/");
		PriceOrientatedParser parser = new PriceOrientatedParser();
		parser.getMenus("https://www.maredo.de/lunch").forEach(elem -> {
			System.out.println(elem.getValue0().text());
			System.out.println(elem.getValue1().stream().map(Element::text).collect(Collectors.toList()));
		});
	}

}
