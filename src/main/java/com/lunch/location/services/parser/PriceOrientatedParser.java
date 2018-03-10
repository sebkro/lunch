package com.lunch.location.services.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PriceOrientatedParser implements MenuParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(PriceOrientatedParser.class);

	private static final Pattern pricePattern = Pattern.compile("\\d{1,2}[.,]\\d{1,2}([^\\.\\d]|$)");
	
	private boolean doSearchPre;
	private boolean doSearchPost;
	
	public PriceOrientatedParser(boolean doSearchPre, boolean doSearchPost) {
		this.doSearchPost = doSearchPost;
		this.doSearchPre = doSearchPre;
	}	

	@Override
	public List<Menu> getMenus(URL url) {
		List<Menu> result = new ArrayList<>();
		try {
			Document doc = Jsoup.connect(url.toString()).timeout(5000).get();
			return doc.getElementsMatchingOwnText(pricePattern).stream().map(elem -> toMenu(elem))
					.filter(Optional::isPresent)
					.map(Optional::get)
					.collect(Collectors.toList());

		} catch (Exception e) {
			LOGGER.error("Could not parse menu for {}", url);
		}
		return result;
	}
	
	private Optional<Menu> toMenu(Element priceElement) {
		Element maxParent = getMaxParent(priceElement);
		String text = maxParent.text();
		if (doSearchPost) {
			text = text + " " + getNextNonEmptyElement(maxParent).map(Element::text).orElse("");
		}
		if (doSearchPre) {
			text = getPreviousNonEmptyElement(maxParent).map(Element::text).orElse("") + " " + text;
		}
		List<String> prices = getPrices(priceElement);
		if (prices.size() == 1) {
			text = pricePattern.matcher(text).replaceAll("").replaceAll("€", "").trim();
		}
		if (text.length() < 5) {
			return Optional.empty();
		} else {
			return Optional.of(Menu.builder().description(text).price(prices.get(0)).build());
		}
		
	}

	private Optional<Element> getPreviousNonEmptyElement(Element e) {
		return findSiblingsNonEmptyElement(e, elem -> elem.previousElementSibling());
	}

	private Optional<Element> getNextNonEmptyElement(Element e) {
		return findSiblingsNonEmptyElement(e, elem -> elem.nextElementSibling());
	}

	private Optional<Element> findSiblingsNonEmptyElement(Element start, Function<Element, Element> traverser) {
		Element current = null;
		String currentText = null;
		Element next = traverser.apply(start);
		while (next != null && countPrices(next) == 0 && StringUtils.isBlank(currentText)) {
			current = next;
			currentText = next.text();
			next = traverser.apply(current);
		}
		return Optional.ofNullable(current);
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
	
	private List<String> getPrices(Element element) {
		List<String> result = new ArrayList<>();
		Matcher priceMatcher = pricePattern.matcher(element.text());
		while (priceMatcher.find()) {
			result.add(priceMatcher.group());
		}
		return result;
	}

//	public static void main(String[] args) throws MalformedURLException {
////		 URL url = new URL("http://feinkosthafencity.de/#front-page-2");
////		 URL url = new URL("http://www.thegreek.hamburg/speisekarte/");
//		// URL url = new URL("https://www.avarina.de/vorspeisen.htm");
//		// URL url = new URL("https://www.avarina.de/tagesgerichte.htm");
////		 URL url = new URL("http://www.restaurant-india-house-hamburg.de/speisen/");
//		// URL url = new URL("http://www.salathai.de/speisekarte/");
////		URL url = new URL("http://www.ciaomamma.de/index.php/menu");
//		// URL url = new
//		// URL("http://www.restaurant-fischmarkt.de/vorspeisen.html");
////		 URL url = new URL("https://www.greenlovers.de/essen/salate-bowls");
////		 URL url = new URL("https://www.maredo.de/lunch/");
////		 URL url = new URL("http://feinkosthafencity.de/");
//		 URL url = new URL("https://www.maredo.de/lunch/");
//		PriceOrientatedParser parser = new PriceOrientatedParser(false, false);
//		PriceOrientatedParser parser1 = new PriceOrientatedParser(true, false);
//		PriceOrientatedParser parser2= new PriceOrientatedParser(false, true);
////		System.out.println(parser.getMenus(url));
//		System.out.println(parser1.getMenus(url));
////		System.out.println(parser2.getMenus(url));
//	}

}
