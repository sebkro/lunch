package com.lunch.location.parser;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.lunch.location.services.parser.WordListSimilarityCalculator;
import com.lunch.location.services.parser.WordListSimilarityCalculator.StringDistanceMetric;


public class WordListSimilarityCalculatorTest {
	
	private Set<String> words = Sets.newHashSet("Lachs", "Rotkohl", "Spaghetti");
	private WordListSimilarityCalculator calculator = new WordListSimilarityCalculator();
	
	@Test
	public void shouldCalcNormalizedLevenshteinForSingleWordFullMatch() {
		//when
		Map<StringDistanceMetric, Double> result = calculator.getBestMetricsFor("Spaghetti", words, StringDistanceMetric.NormalizedLevenshtein);
		
		//then
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(0.0, result.get(StringDistanceMetric.NormalizedLevenshtein), 0.001);
	}

	@Test
	public void shouldCalcNormalizedLevenshteinForSingleWordPartialMatch() {
		//when
		Map<StringDistanceMetric, Double> result = calculator.getBestMetricsFor("Grünkohl", words, StringDistanceMetric.NormalizedLevenshtein);
		
		//then
		Assert.assertEquals(1, result.size());
		Assert.assertEquals((3.0/8.0), result.get(StringDistanceMetric.NormalizedLevenshtein), 0.001);
	}

	@Test
	public void shouldCalcMetricLCSForSingleWordFullMatch() {
		//when
		Map<StringDistanceMetric, Double> result = calculator.getBestMetricsFor("Spaghetti", words, StringDistanceMetric.MetricLCS);
		
		//then
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(0.0, result.get(StringDistanceMetric.MetricLCS), 0.001);
	}
	
	@Test
	public void shouldCalcMetricLCSForSingleWordPartialMatch() {
		//when
		Map<StringDistanceMetric, Double> result = calculator.getBestMetricsFor("Grünkohl", words, StringDistanceMetric.MetricLCS);
		
		//then
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(0.375, result.get(StringDistanceMetric.MetricLCS), 0.001);
	}

	@Test
	public void shouldCalcNormalizedLevenshteinForWordListMatch() {
//		NormalizedLevenshtein(Seezungenfilet, Seezunge) = 0.42857142857142855
//		NormalizedLevenshtein(Seezungenfilet, Blattspinat) = 0.8571428571428571
//		NormalizedLevenshtein(frischen, Seezunge) = 1.0
//		NormalizedLevenshtein(frischen, Blattspinat) = 0.9090909090909091
//		NormalizedLevenshtein(Gemüse, Seezunge) = 0.75
//		NormalizedLevenshtein(Gemüse, Blattspinat) = 0.9090909090909091

		//given
		Set<String> wordList1 = Sets.newHashSet("Seezunge", "Blattspinat");
		Set<String> wordList2 = Sets.newHashSet("Seezungenfilet", "frischen", "Gemüse");
		//when
		Map<StringDistanceMetric, List<Double>> result = calculator.getBestMetricsFor(wordList1, wordList2, StringDistanceMetric.NormalizedLevenshtein);
		
		//then
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(2, result.get(StringDistanceMetric.NormalizedLevenshtein).size());
		Assert.assertEquals(0.42857142857142855, result.get(StringDistanceMetric.NormalizedLevenshtein).get(0), 0.001);
		Assert.assertEquals(0.8571428571428571, result.get(StringDistanceMetric.NormalizedLevenshtein).get(1), 0.001);
	}
}
