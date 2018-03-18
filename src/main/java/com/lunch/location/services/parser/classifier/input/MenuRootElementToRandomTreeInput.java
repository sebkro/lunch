package com.lunch.location.services.parser.classifier.input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.javatuples.Pair;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.lunch.location.services.parser.MenuPosTagger;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

@Service
public class MenuRootElementToRandomTreeInput extends PosTaggedToInput {
	
	public static final List<String> TAGS = Lists.newArrayList("NN", "NE", "ART", "ADJA", "ADJD", "$.", "$, ", "FM", "KON", "XY");
	public static final List<String> TAG_GROUP_VERBS = Lists.newArrayList("VVFIN", "VAFIN", "VMFIN", "VVINF", "VAINF", "VMINF", "VVIMP", "VAIMP", "VVPP", "VAPP", "VMPP", "VVIZU");
	
	public static final List<String> PRE_TAG_ATTRIBUTES = Lists.newArrayList("words", "sentences");
	
	public MenuRootElementToRandomTreeInput(MenuPosTagger tagger) {
		super(tagger);
	}
	
	@Override
	public Instance convert(String menuRootElement, Instances dataset, ArrayList<Attribute> attributes) {
		Pair<Map<String, Long>, Integer> postaggedAndCounted = postagAndCount(menuRootElement);
		long totalWords = postaggedAndCounted.getValue0().values().stream().reduce(0L, (e1, e2) -> e1 + e2);
		long totalSentences = postaggedAndCounted.getValue1();
			
		Instance instance = new DenseInstance(attributes.size());
		instance.setValue(attributes.get(0), totalWords);
		instance.setValue(attributes.get(1), totalSentences);
		setTagAttributes(attributes, postaggedAndCounted, totalWords, instance, PRE_TAG_ATTRIBUTES.size());
		instance.setValue(attributes.get(attributes.size() - 1), "POSITIVE");
		return instance;
	}

	@Override
	protected ArrayList<Attribute> getAttributes() {
		ArrayList<Attribute> attributes = new ArrayList<>();
		attributes.add(new Attribute(PRE_TAG_ATTRIBUTES.get(0)));
		attributes.add(new Attribute(PRE_TAG_ATTRIBUTES.get(1)));
		getTagGroupNames().forEach(elem -> attributes.add(new Attribute(elem)));
		attributes.add(new Attribute("classification", Arrays.asList("POSITIVE", "NEGATIVE")));
		return attributes;
		
	}

	@Override
	protected List<Pair<String, List<String>>> getTagGroups() {
		List<Pair<String, List<String>>> result = TAGS.stream()
				.map(elem -> new Pair<String, List<String>>(elem, Lists.newArrayList(elem)))
				.collect(Collectors.toList());
		result.add(new Pair<String, List<String>>("V", TAG_GROUP_VERBS));
		return result;
	}
	
}
