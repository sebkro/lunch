package com.lunch.location.services.parser.classifier.input;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.lunch.location.services.parser.nlp.StopWordsService;
import com.lunch.location.services.parser.nlp.WordListSimilarityCalculator;
import com.lunch.location.services.parser.nlp.WordListSimilarityCalculator.StringDistanceMetric;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SiblingElementToRootElementDistanceCalculator {

	private StopWordsService stopWordsService;
	private WordListSimilarityCalculator wordListSimilarityCalculator;
	
	public Map<StringDistanceMetric, List<Double>> findBestMatchingWords(List<String> siblingsElements, List<String> rootElements) {
		Set<String> siblingNotStopWords = getNonStopWords(siblingsElements);
		Set<String> rootNotStopWords = getNonStopWords(rootElements);
		return wordListSimilarityCalculator.getBestMetricsFor(siblingNotStopWords, rootNotStopWords, StringDistanceMetric.NormalizedLevenshtein);
	}

	private Set<String> getNonStopWords(List<String> words) {
		return words.stream()
				.filter(elem -> stopWordsService.isNotStopWord(elem))
				.collect(Collectors.toSet());
	}
	
	
	
	
}
