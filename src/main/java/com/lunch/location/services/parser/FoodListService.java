package com.lunch.location.services.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.lunch.location.services.parser.WordListSimilarityCalculator.StringDistanceMetric;

import info.debatty.java.stringsimilarity.MetricLCS;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;

public class FoodListService {
	
	private Set<String> food;
	private WordListSimilarityCalculator similarityCalculator;
	
	private NormalizedLevenshtein normalizedLevenshtein = new NormalizedLevenshtein();
	private MetricLCS metricLcs = new MetricLCS();
	
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
	
	

}
