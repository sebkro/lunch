package com.lunch.location.services.parser.nlp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import info.debatty.java.stringsimilarity.MetricLCS;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;

@Service
public class WordListSimilarityCalculator {
	
	public enum StringDistanceMetric {
		MetricLCS, NormalizedLevenshtein
	}
	
	private NormalizedLevenshtein normalizedLevenshtein = new NormalizedLevenshtein();
	private MetricLCS metricLcs = new MetricLCS();
	
	/**
	 * calculate the passed metrics for the given target word with each word in wordList. Return the best
	 * value for each metric.
	 * example
	 * input: word="test", wordList= ["test", "abc"], metrics=NormalizedLevenshtein,MetricLCS
	 * output: {NormalizedLevenshtein -> min(NormalizedLevenshtein("test", "test"), NormalizedLevenshtein("test", "abc")),
	 *          MetricLCS -> min(MetricLCS("test", "test"), MetricLCS("test", "abc"))}
	 * @param targetword to compare with each word in wordlist
	 * @param wordList the word list
	 * @param metrics metrics to use for string similarity calulation
	 * @return map metric -> best distance for this metric 
	 */
	public Map<StringDistanceMetric, Double> getBestMetricsFor(String targetword, Set<String> wordList, StringDistanceMetric... metrics) {
		Set<String> wordListLower = wordList.stream().map(String::toLowerCase).map(String::trim).collect(Collectors.toSet());
		Map<StringDistanceMetric, Double> result = new HashMap<>();
		String elemLower = targetword.toLowerCase();
		for (String word : wordListLower) {
			for (StringDistanceMetric metric : metrics) {
				if (metric == StringDistanceMetric.MetricLCS) {
					double distance = metricLcs.distance(word, elemLower);
					double bestDisance = result.getOrDefault(metric, Double.MAX_VALUE);
					if (distance < bestDisance) {
						result.put(metric, distance);
					}
				}
				if (metric == StringDistanceMetric.NormalizedLevenshtein) {
					double distance = normalizedLevenshtein.distance(word, elemLower);
					double bestDistance = result.getOrDefault(metric, Double.MAX_VALUE);
					if (distance < bestDistance) {
						result.put(metric, distance);
					}
				}
				
			}
		}
		return result;
	}

	/**
	 * calculate the passed metrics for the given target words with each word in wordList. Return the best
	 * value for each metric.
	 * @param targetwords words to compare with each word in wordlist
	 * @param wordList the word list
	 * @param metrics metrics to use for string similarity calulation
	 * @return map metric -> list of all best distances of target words sorted
	 */
	public Map<StringDistanceMetric, List<Double>> getBestMetricsFor(Set<String> targetwords, Set<String> wordList, StringDistanceMetric... metrics) {
		Map<StringDistanceMetric, List<Double>> result = new HashMap<>();
		for (StringDistanceMetric metric : metrics) {
			List<Double> l = new ArrayList<>();
			for (String w1 : targetwords) {
				l.add(getBestMetricsFor(w1, wordList, metric).get(metric));
			}
			l.sort((e1, e2) -> Double.compare(e1, e2));
			result.put(metric, l);
		}
		return result;
	}
	
	

}
