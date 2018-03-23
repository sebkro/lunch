package com.lunch.location.parser.nlp;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lunch.location.services.parser.nlp.FoodListService;
import com.lunch.location.services.parser.nlp.WordListSimilarityCalculator;
import com.lunch.location.services.parser.nlp.WordListSimilarityCalculator.StringDistanceMetric;


public class FoodListServiceTest {
	
	private FoodListService foodlistService;
	private WordListSimilarityCalculator similarityCalculator = new WordListSimilarityCalculator();
	
	@Before
	public void init() {
		Set<String> words = Sets.newHashSet("Lachs", "Rotkohl", "Spaghetti");
		foodlistService = new FoodListService(words, similarityCalculator);
	}
	
	@Test
	public void shouldCalcNormalizedLevenshteinForSingleWordFullMatch() {
		//when
		Map<StringDistanceMetric, Double> result = foodlistService.getBestMetricsFor("Spaghetti", StringDistanceMetric.NormalizedLevenshtein);
		
		//then
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(0.0, result.get(StringDistanceMetric.NormalizedLevenshtein), 0.001);
	}

	@Test
	public void shouldCalcNormalizedLevenshteinForSingleWordPartialMatch() {
		//when
		Map<StringDistanceMetric, Double> result = foodlistService.getBestMetricsFor("Grünkohl", StringDistanceMetric.NormalizedLevenshtein);
		
		//then
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(3.0/8.0, result.get(StringDistanceMetric.NormalizedLevenshtein), 0.001);
	}

	@Test
	public void shouldCalcMetricLCSForSingleWordFullMatch() {
		//when
		Map<StringDistanceMetric, Double> result = foodlistService.getBestMetricsFor("Spaghetti", StringDistanceMetric.MetricLCS);
		
		//then
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(0.0, result.get(StringDistanceMetric.MetricLCS), 0.001);
	}
	
	@Test
	public void shouldCalcMetricLCSForSingleWordPartialMatch() {
		//when
		Map<StringDistanceMetric, Double> result = foodlistService.getBestMetricsFor("Grünkohl", StringDistanceMetric.MetricLCS);
		
		//then
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(0.375, result.get(StringDistanceMetric.MetricLCS), 0.001);
	}
	
	@Test
	public void shouldCalcMetricsForMultipleWords() {
		//when
		Map<StringDistanceMetric, List<Double>> result = foodlistService.getBestMetricsFor(
				Lists.newArrayList("Grünkohl", "Spaghetti", "Kuchen"), 
				StringDistanceMetric.MetricLCS, StringDistanceMetric.NormalizedLevenshtein);
		
		//then
		Assert.assertEquals(2, result.size());
		double[] expectedNl = {0.0, 3.0/8.0, 4.0/6.0};
		Assert.assertArrayEquals(expectedNl, result.get(StringDistanceMetric.NormalizedLevenshtein).stream()
				.mapToDouble(Double::valueOf).toArray(), 0.0001);
		double[] expectedMlcs = {0.0, 0.375, 0.6666666666666667};
		Assert.assertArrayEquals(expectedMlcs, result.get(StringDistanceMetric.MetricLCS).stream()
				.mapToDouble(Double::valueOf).toArray(), 0.0001);
	}
}
