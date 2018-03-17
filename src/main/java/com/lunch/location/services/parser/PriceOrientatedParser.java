package com.lunch.location.services.parser;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.stereotype.Service;

@Service
public class PriceOrientatedParser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PriceOrientatedParser.class);

	public static final Pattern pricePattern = Pattern.compile("\\d{1,2}[.,]\\d{1,2}([^\\.\\d]|$)");

	public List<MenuCandidate> getMenus(String url) {
		List<MenuCandidate> result = new ArrayList<>();
		try {
			
			Document doc = Jsoup.connect(url.toString()).timeout(10000).get();
			return doc.getElementsMatchingOwnText(pricePattern).stream().map(elem -> toMenu(elem))
					.collect(Collectors.toList());

		} catch (Exception e) {
			LOGGER.error("Could not parse menu for {}", url);
		}
		return result;
	}
	
	private MenuCandidate toMenu(Element priceElement) {
		Element maxParent = getMaxParent(priceElement);
		List<String> previousElements = getPreviousNonEmptyElement(maxParent);
		List<String> nextElements = getNextNonEmptyElement(maxParent);
		
		return new MenuCandidate(maxParent.text(), previousElements, nextElements);
	}

	private List<String> getPreviousNonEmptyElement(Element e) {
		return findSiblingsNonEmptyElement(e, elem -> elem.previousElementSibling());
	}

	private List<String> getNextNonEmptyElement(Element e) {
		return findSiblingsNonEmptyElement(e, elem -> elem.nextElementSibling());
	}

	private List<String> findSiblingsNonEmptyElement(Element start, Function<Element, Element> traverser) {
		List<String> result = new ArrayList<>();
		Element current = null;
		Element next = traverser.apply(start);
		while (next != null && countPrices(next) == 0) {
			current = next;
			if (StringUtils.isNotBlank(current.text())) {
				result.add(current.text());
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
		return getPrices(element.text()).size();
	}
	
	public List<String> getPrices(String element) {
		List<String> result = new ArrayList<>();
		Matcher priceMatcher = pricePattern.matcher(element);
		while (priceMatcher.find()) {
			result.add(priceMatcher.group());
		}
		return result;
	}
}
