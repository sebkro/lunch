package com.lunch.location.parser.classifier.input;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;
import com.lunch.location.services.parser.classifier.input.SiblingElementToRootElementDistanceCalculator;
import com.lunch.location.services.parser.nlp.StopWordsService;
import com.lunch.location.services.parser.nlp.WordListSimilarityCalculator;
import com.lunch.location.services.parser.nlp.WordListSimilarityCalculator.StringDistanceMetric;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SiblingElementToRootElementDistanceCalculatorIntegrationTest {
	
	private SiblingElementToRootElementDistanceCalculator calculator;
	
	@Autowired
	private StopWordsService stopWordsService;
	
	@Autowired
	private WordListSimilarityCalculator similarityCalculator;
	
	@Before
	public void init() {
		calculator = new SiblingElementToRootElementDistanceCalculator(stopWordsService, similarityCalculator);
	}
	
	@Test
	public void itShoudCalculate() {
		//given
		List<String> rootElement = Lists.newArrayList("seezungenfilet", "gebraten", "an", "blattspinat", "und", "butterkartoffeln");
		List<String> siblingsElement = Lists.newArrayList("seezunge", "im", "ganzen", "gebraten", "müllerin", "art", "dazu", "blattspinat", "in", "rahm");
		
		
		//when
		Map<StringDistanceMetric, List<Double>> result = calculator.findBestMatchingWords(siblingsElement, rootElement);
		
		//then
		double[] nLevenshteinDistances = {0.0, 0.0, 0.42857142857142855, 0.75, 0.75, 0.8125};
		double[] mlcsDistances = {0.0, 0.0,  0.4285714285714286, 0.625, 0.75, 0.75};

		double[] nlLevenshteinResult = result.get(StringDistanceMetric.NormalizedLevenshtein).stream().mapToDouble(Double::valueOf).toArray();
		double[] mlcsResult = result.get(StringDistanceMetric.MetricLCS).stream().mapToDouble(Double::valueOf).toArray();

		Assert.assertArrayEquals(nLevenshteinDistances, nlLevenshteinResult, 0.00001);
		Assert.assertArrayEquals(mlcsDistances, mlcsResult, 0.00001);
	}

}
