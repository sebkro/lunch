package com.lunch.location.services.parser.nlp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.lunch.location.services.parser.nlp.WordListSimilarityCalculator.StringDistanceMetric;

public class FoodListService {
	
	private Set<String> food;
	private WordListSimilarityCalculator similarityCalculator;
	
	public FoodListService(List<Path> foodlistPathes, WordListSimilarityCalculator similarityCalculator) throws IOException {
		this.similarityCalculator = similarityCalculator;
		this.food = new HashSet<>();
		for (Path path : foodlistPathes) {
			Files.readAllLines(path).stream()
				.map(String::trim)
				.map(String::toLowerCase)
				.forEach(elem -> this.food.add(elem));
		}
	}
	
	public FoodListService(Set<String> foods, WordListSimilarityCalculator similarityCalculator) {
		this.similarityCalculator = similarityCalculator;
		this.food = foods.stream()
				.map(String::trim)
				.map(String::toLowerCase)
				.collect(Collectors.toSet());
	}
	
	public Map<StringDistanceMetric, Double> getBestMetricsFor(String elem, StringDistanceMetric... metricsToApply) {
		return similarityCalculator.getBestMetricsFor(elem, food, metricsToApply);
	}

	public Map<StringDistanceMetric, List<Double>> getBestMetricsFor(List<String> elems, StringDistanceMetric... metricsToApply) {
		Map<StringDistanceMetric, List<Double>> result = new HashMap<>();
		for (StringDistanceMetric metric : metricsToApply) {
			List<Double> distances = elems.stream()
					.map(elem -> getBestMetricsFor(elem, metric).get(metric))
					.collect(Collectors.toList());
			Collections.sort(distances);
			result.put(metric, distances);
		}
		return  result;
	}
	
	

}
