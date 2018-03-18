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
		List<Pair<String, List<String>>> tagGroups = getTagGroups();
		for (int i = prevAttributes; i < attributes.size() - 1; i++) {
			String attrName = attributes.get(i).name();
			List<String> tagsForTagGroup = getValueFor(tagGroups, attrName);
			instance.setValue(attributes.get(i), getCount(postaggedAndCounted.getValue0(), tagsForTagGroup, totalWords));
		}
	}
	
	private List<String> getValueFor(List<Pair<String, List<String>>> in, String key) {
		return in.stream()
				.filter(elem -> elem.getValue0().equals(key))
				.findAny()
				.map(elem -> elem.getValue1())
				.orElse(new ArrayList<>());
	}
	
	private double getCount(Map<String, Long> taggedCount, List<String> tagGroup, long totalWords) {
		long count = tagGroup.stream()
			.mapToLong(elem -> taggedCount.getOrDefault(elem, 0L))
			.reduce(0L, (e1, e2) -> e1 + e2);
		
		return ((double) count) / totalWords;
	}
	
	protected List<String> getTagGroupNames() {
		return getTagGroups().stream().map(Pair::getValue0).collect(Collectors.toList());
		
	}
	
	protected abstract List<Pair<String, List<String>>> getTagGroups();

}
