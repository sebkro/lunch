package com.lunch.location.services.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.javatuples.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FKHCParser implements MenuParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(FKHCParser.class);
	
	private String startTag = "h3";

	@Override
	public List<Menu> getMenus(URL url) {
		List<Menu> result = new ArrayList<>();
		try {
			Document doc = Jsoup.connect(url.toString()).get();
			Elements elements = doc.getElementsByTag(startTag).next("p");
			while(!elements.isEmpty() && !isAbort(elements)) {
				Menu m = parseMenu(elements);
				result.add(m);
				elements = elements.next("p");
			}
		} catch (IOException e) {
			LOGGER.error("Could not parse menu for {}", url);
		}
		return result;
	}

	private Menu parseMenu(Elements menuElement) {
		int lastCut = 0;
		List<Pair<String, String>> variants = new ArrayList<>();
		String[] html = menuElement.html().split("<br>");
		for (int i = 0; i < html.length; i++) {
			boolean isPriceLine = html[i].matches(".*\\d*.?€");
			if (isPriceLine) {
				String description = join(html, lastCut, i);
				String price = html[i];
				variants.add(new Pair<String, String>(description, price));
				lastCut = i+1;
			}
		}
		
		List<MenuVariant> menuVariants = getVariants(variants);
		Menu result = Menu.builder()
				.description(variants.get(0).getValue0())
				.price(variants.get(0).getValue1())
				.variants(menuVariants).build();
				
		
		return result;
	}

	private List<MenuVariant> getVariants(List<Pair<String, String>> variants) {
		List<MenuVariant> menuVariants = new ArrayList<>();
		for (int i = 1; i < variants.size(); i++) {
			String description = "";
			String price = "";
			if (StringUtils.isBlank(variants.get(i).getValue0())) {
				description = variants.get(i).getValue1().replaceAll("\\d+.\\d+.?€", "");
				price = variants.get(i).getValue1().replaceAll("[^\\d€,\\.]*", "");
			} else {
				description = variants.get(i).getValue0();
				price = variants.get(i).getValue1();
			}
			menuVariants.add(new MenuVariant(description.trim(), price.trim()));
		}
		return menuVariants;
	}
	
	private String join(String[] arr, int from, int toExclusive) {
		String result = "";
		for (int i = from; i < toExclusive; i++) {
			result += arr[i];
		}
		return result;
	}

	private boolean isAbort(Elements e) {
		return e.text().contains("Sie eine kleinere Portion");
	}
	
	public static void main(String[] args) throws MalformedURLException {
		URL url = new URL("http://feinkosthafencity.de/#front-page-2");
		FKHCParser parser = new FKHCParser();
		parser.getMenus(url).forEach(System.out::println);
	}

}
