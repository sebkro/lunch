package com.lunch.location.services.parser;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
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
	private MenuRootElementToRandomTreeInput menuToRandomTreeInput;


	public List<Menu> getMenus(String url) {
		return parser.getMenus(url).stream()
			.map(elem -> map(elem))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.collect(Collectors.toList());
	}
	
	public Optional<Menu> map(MenuCandidate elem) {
		String text = elem.getPrevElements().stream()
				.filter(prevElem -> shouldAddPrevElement(prevElem))
				.reduce("", String::concat);
		if (shouldAddRootElement(elem.getRootElement())) {
			text += " " + elem.getRootElement();
		}
		if (StringUtils.isBlank(text)) {
			return Optional.empty();
		} else {
			List<String> prices = parser.getPrices(elem.getRootElement());
			if (prices.size() == 1) {
				text = PriceOrientatedParser.pricePattern.matcher(text).replaceAll("").replaceAll("€", "").trim();
			}
			Menu menu = Menu.builder().description(text).price(prices.get(0)).build();
			return Optional.of(menu);
		}
	}
	
	public boolean shouldAddRootElement(String elem) {
		Instance instanceToClassizy = menuToRandomTreeInput.convert(elem);
		try {
			double[] result = randomForest.distributionForInstance(instanceToClassizy);
			return result[0] > result[1];
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean shouldAddPrevElement(String elem) {
		List<List<TaggedWord>> tagged = posTagger.posTag(elem);
		Map<String, Long> taggedCount = tagged.stream().flatMap(sentence -> sentence.stream())
			.map(TaggedWord::tag)
			.collect(Collectors.groupingBy((String s) -> s, Collectors.counting()));
		double vvFinWords = (double) taggedCount.getOrDefault("VVFIN", 0L); 
		double vvImp = (double) taggedCount.getOrDefault("VVIMP", 0L); 
		
		return vvFinWords + vvImp <= 0;
	}
}
