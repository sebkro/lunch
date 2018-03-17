package com.lunch.location.parser.classifier.input;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Matchers.anyString;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lunch.location.services.parser.classifier.input.SiblingElementToRootElementDistanceCalculator;
import com.lunch.location.services.parser.nlp.StopWordsService;
import com.lunch.location.services.parser.nlp.WordListSimilarityCalculator;
import com.lunch.location.services.parser.nlp.WordListSimilarityCalculator.StringDistanceMetric;

public class SiblingElementToRootElementDistanceCalculatorTest {
	
	private SiblingElementToRootElementDistanceCalculator calculator;
	
	@Mock
	private StopWordsService stopWordsService;
	
	@Mock
	private WordListSimilarityCalculator similarityCalculator;
	
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		when(stopWordsService.isNotStopWord(anyString())).thenReturn(false);
		calculator = new SiblingElementToRootElementDistanceCalculator(stopWordsService, similarityCalculator);
	}
	
	@Test
	public void itShoudCalculate() {
		//given
		List<String> rootElement = Lists.newArrayList("Seezungenfilet", "gebraten", "an", "Blattspinat", "und", "Butterkartoffeln");
		List<String> siblingsElement = Lists.newArrayList("Seezunge", "im", "ganzen", "gebraten", "Müllerin", "Art", "dazu", "Blattspinat", "in", "Rahm");
		
		Set<String> nonStopWordListRootElement = Sets.newHashSet("Seezungenfilet", "gebraten", "Blattspinat", "Butterkartoffeln");
		Set<String> nonStopWordListSiblingsElement = Sets.newHashSet("Seezunge", "gebraten", "ganzen", "Müllerin", "Art", "Blattspinat", "Rahm");
		
		nonStopWordListRootElement.forEach(elem -> when(stopWordsService.isNotStopWord(elem)).thenReturn(true));
		nonStopWordListSiblingsElement.forEach(elem -> when(stopWordsService.isNotStopWord(elem)).thenReturn(true));
		
		Map<StringDistanceMetric, List<Double>> distanceResult = new HashMap<>();
		distanceResult.put(StringDistanceMetric.NormalizedLevenshtein, Lists.newArrayList(0.0, 0.0, 0.4, 0.7, 0.8, 0.85, 0.9));
		
		when(similarityCalculator.getBestMetricsFor(nonStopWordListSiblingsElement,
				nonStopWordListRootElement, StringDistanceMetric.NormalizedLevenshtein)).thenReturn(distanceResult);
		
		//when
		Map<StringDistanceMetric, List<Double>> result = calculator.findBestMatchingWords(siblingsElement, rootElement);
		
		//then
		Assert.assertEquals(distanceResult, result);
	}

}
