package com.lunch.location.parser;

import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.lunch.location.services.parser.FoodListService;
import com.lunch.location.services.parser.WordListSimilarityCalculator;
import com.lunch.location.services.parser.WordListSimilarityCalculator.StringDistanceMetric;


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
}
