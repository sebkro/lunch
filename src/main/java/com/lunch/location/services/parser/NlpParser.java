package com.lunch.location.services.parser;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.javatuples.Pair;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.stanford.nlp.ling.TaggedWord;

@Service
public class NlpParser {
	
	@Autowired
	private PriceOrientatedParser parser;
	
	@Autowired
	private MenuPosTagger posTagger;


	public List<Menu> getMenus(String url) {
		return parser.getMenus(url).stream()
			.map(elem -> map(elem))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.collect(Collectors.toList());
	}
	
	public Optional<Menu> map(Pair<Element, List<Element>> elem) {
		String text = elem.getValue1().stream()
				.filter(prevElem -> shouldAddElement(prevElem))
				.map(Element::text)
				.reduce("", String::concat);
		if (shouldAddElement(elem.getValue0())) {
			text += " " + elem.getValue0().text();
		}
		if (StringUtils.isBlank(text)) {
			return Optional.empty();
		} else {
			List<String> prices = parser.getPrices(elem.getValue0());
			if (prices.size() == 1) {
				text = PriceOrientatedParser.pricePattern.matcher(text).replaceAll("").replaceAll("€", "").trim();
			}
			Menu menu = Menu.builder().description(text).price(prices.get(0)).build();
			return Optional.of(menu);
		}
	}
	
	public boolean shouldAddElement(Element elem) {
		List<List<TaggedWord>> tagged = posTagger.posTag(elem);
		Map<String, Long> taggedCount = tagged.stream().flatMap(sentence -> sentence.stream())
			.map(TaggedWord::tag)
			.collect(Collectors.groupingBy((String s) -> s, Collectors.counting()));
		double totalWords = (double) taggedCount.values().stream().collect(Collectors.counting());
		double nnWords = (double) taggedCount.getOrDefault("NN", 0L); 
		double neWords = (double) taggedCount.getOrDefault("NE", 0L); 
		double adjdWords = (double) taggedCount.getOrDefault("ADJD", 0L); 
		
		return (nnWords + neWords) / totalWords > 0.5;
	}
}
