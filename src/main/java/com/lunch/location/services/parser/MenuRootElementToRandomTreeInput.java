package com.lunch.location.services.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.javatuples.Pair;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import edu.stanford.nlp.ling.TaggedWord;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

@Service
public class MenuRootElementToRandomTreeInput {
	
	private MenuPosTagger tagger;
	
	public MenuRootElementToRandomTreeInput(MenuPosTagger tagger) {
		this.tagger = tagger;
	}
	
	public Instance convert(String menuRootElement, Instances dataset, ArrayList<Attribute> attributes) {
		List<List<TaggedWord>> tagged = tagger.posTag(menuRootElement);
		Map<String, Long> taggedCount = tagged.stream().flatMap(sentence -> sentence.stream())
				.map(TaggedWord::tag)
				.collect(Collectors.groupingBy((String s) -> s, Collectors.counting()));
			long totalWords = taggedCount.values().stream().reduce(0L, (e1, e2) -> e1 + e2);
			long totalSentences = tagged.size();
			
			Instance instance = new DenseInstance(attributes.size());
			instance.setValue(attributes.get(0), totalWords);
			instance.setValue(attributes.get(1), totalSentences);
			for (int i = 2; i < attributes.size() - 1; i++) {
				instance.setValue(attributes.get(i), getCount(taggedCount, attributes, i, totalWords));
			}
			instance.setValue(attributes.get(attributes.size() - 1), "POSITIVE");
			return instance;
	}

	public Instance convert(String menuRootElement) {
		Pair<Instances, ArrayList<Attribute>> instancesAndAttributes = getInstances("Rel", 1);
		Instance result = convert(menuRootElement, instancesAndAttributes.getValue0(), instancesAndAttributes.getValue1());
		result.setDataset(instancesAndAttributes.getValue0());
		return result;
	}

	private double getCount(Map<String, Long> taggedCount, List<Attribute> attributes, int index, long totalWords) {
		return ((double) taggedCount.getOrDefault(attributes.get(index).name(), 0L)) / totalWords;
	}
	
	public Pair<Instances, ArrayList<Attribute>> getInstances(String name, int noInstances) {
		ArrayList<Attribute> attributes = getAttributes();
		Instances result = new Instances(name, attributes, noInstances);
		result.setClassIndex(attributes.size() - 1);
		return new Pair<Instances, ArrayList<Attribute>>(result, attributes);
	}
	
	private static ArrayList<Attribute> getAttributes() {
		List<String> tags = Lists.newArrayList("NN", "XY", "ART", "ADJA", "ADJD", "PRF", "VVINF", "PPOSAT", "VVIMP", "PPER", "$.", "$, "
				+ "", "VMFIN", "CARD", "ADV", "PTKVZ", "FM", "TRUNC", "PROAV", "VVFIN", "PIDAT", "VVPP", "KON", "APPR", "NE", "VAFIN");
		ArrayList<Attribute> attributes = new ArrayList<>();
		attributes.add(new Attribute("words"));
		attributes.add(new Attribute("sentences"));
		tags.forEach(elem -> attributes.add(new Attribute(elem)));
		attributes.add(new Attribute("classification", Arrays.asList("POSITIVE", "NEGATIVE")));
		return attributes;
		
	}
	
}
