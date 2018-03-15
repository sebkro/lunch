package com.lunch.location.services.parser;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.javatuples.Pair;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import edu.stanford.nlp.ling.TaggedWord;
import lombok.AllArgsConstructor;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;

@Service
@AllArgsConstructor
public class NlpParser {
	
	private PriceOrientatedParser parser;
	private MenuPosTagger posTagger;
	private RandomForest randomForest;
	private MenuToRandomTreeInput menuToRandomTreeInput;


	public List<Menu> getMenus(String url) {
		return parser.getMenus(url).stream()
			.map(elem -> map(elem))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.collect(Collectors.toList());
	}
	
	public Optional<Menu> map(Pair<Element, List<Element>> elem) {
		String text = elem.getValue1().stream()
				.filter(prevElem -> shouldAddPrevElement(prevElem))
				.map(Element::text)
				.reduce("", String::concat);
		if (shouldAddRootElement(elem.getValue0())) {
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
	
	public boolean shouldAddRootElement(Element elem) {
		String toClassify = elem.text();
		Instance instanceToClassizy = menuToRandomTreeInput.convert(toClassify);
		try {
			double[] result = randomForest.distributionForInstance(instanceToClassizy);
			return result[0] > result[1];
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean shouldAddPrevElement(Element elem) {
		List<List<TaggedWord>> tagged = posTagger.posTag(elem.text());
		Map<String, Long> taggedCount = tagged.stream().flatMap(sentence -> sentence.stream())
			.map(TaggedWord::tag)
			.collect(Collectors.groupingBy((String s) -> s, Collectors.counting()));
		double totalWords = (double) taggedCount.values().stream().collect(Collectors.counting());
		double vvFinWords = (double) taggedCount.getOrDefault("VVFIN", 0L); 
		double vvImp = (double) taggedCount.getOrDefault("VVIMP", 0L); 
		
		return vvFinWords + vvImp <= 0;
	}
}
