package com.lunch.location.services.parser.classifier.input;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.javatuples.Pair;

import com.lunch.location.services.parser.MenuPosTagger;

import edu.stanford.nlp.ling.TaggedWord;
import weka.core.Attribute;
import weka.core.Instance;

public abstract class PosTaggedToInput extends ToRandomTreeInput {
	
	private MenuPosTagger tagger;
	
	public PosTaggedToInput(MenuPosTagger tagger) {
		this.tagger = tagger;
	} 
	
	protected Pair<Map<String, Long>, Integer> postagAndCount(String element) {
		List<List<TaggedWord>> tagged = tagger.posTag(element);
		Map<String, Long> taggedCount = tagged.stream().flatMap(sentence -> sentence.stream())
				.map(TaggedWord::tag)
				.collect(Collectors.groupingBy((String s) -> s, Collectors.counting()));
		return new Pair<Map<String,Long>, Integer>(taggedCount, tagged.size());
	}
	
	protected void setTagAttributes(ArrayList<Attribute> attributes, Pair<Map<String, Long>, Integer> postaggedAndCounted,
			long totalWords, Instance instance, int prevAttributes) {
		for (int i = prevAttributes; i < attributes.size() - 1; i++) {
			instance.setValue(attributes.get(i), getCount(postaggedAndCounted.getValue0(), attributes, i, totalWords));
		}
	}
	

	private double getCount(Map<String, Long> taggedCount, List<Attribute> attributes, int index, long totalWords) {
		return ((double) taggedCount.getOrDefault(attributes.get(index).name(), 0L)) / totalWords;
	}
	
	protected abstract List<String> getTags();

}
