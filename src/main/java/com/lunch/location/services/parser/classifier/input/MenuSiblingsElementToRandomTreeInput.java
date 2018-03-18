package com.lunch.location.services.parser.classifier.input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.javatuples.Pair;

import com.google.common.collect.Lists;
import com.lunch.location.services.parser.MenuPosTagger;
import com.lunch.location.services.parser.nlp.FoodListService;
import com.lunch.location.services.parser.nlp.WordListSimilarityCalculator.StringDistanceMetric;

import edu.stanford.nlp.ling.TaggedWord;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class MenuSiblingsElementToRandomTreeInput extends PosTaggedToInput {
	
	public static final List<String> TAGS = Lists.newArrayList("NN", "NE", "ART", "ADJA", "ADJD", "$.", "$, ", "FM", "KON", "XY");
	public static final List<String> TAG_GROUP_VERBS = Lists.newArrayList("VVFIN", "VAFIN", "VMFIN", "VVINF", "VAINF", "VMINF", "VVIMP", "VAIMP", "VVPP", "VAPP", "VMPP", "VVIZU");
	
	public static final List<String> PRE_TAG_ATTRIBUTES = Lists.newArrayList("words", "sentences");
	
	private SiblingElementToRootElementDistanceCalculator siblingElementToRootElementDistanceCalculator;
	private FoodListService foodlistService;
	
	public MenuSiblingsElementToRandomTreeInput(MenuPosTagger tagger,
			SiblingElementToRootElementDistanceCalculator siblingElementToRootElementDistanceCalculator) {
		super(tagger);
		this.siblingElementToRootElementDistanceCalculator = siblingElementToRootElementDistanceCalculator;
	}
	
	
	
	@Override
	public Instance convert(String menuRootElement, Instances dataset, ArrayList<Attribute> attributes) {
		Pair<Map<String, Long>, List<List<TaggedWord>>> postaggedAndCounted = postagAndCount(menuRootElement);
		long totalWords = postaggedAndCounted.getValue0().values().stream().reduce(0L, (e1, e2) -> e1 + e2);
		long totalSentences = postaggedAndCounted.getValue1().size();
			
		Instance instance = new DenseInstance(attributes.size());
		instance.setValue(attributes.get(0), totalWords);
		instance.setValue(attributes.get(1), totalSentences);
		setTagAttributes(attributes, postaggedAndCounted.getValue0(), totalWords, instance, PRE_TAG_ATTRIBUTES.size());
		instance.setValue(attributes.get(attributes.size() - 2), findBestFoodValue(postaggedAndCounted.getValue1()));
		instance.setValue(attributes.get(attributes.size() - 1), "POSITIVE");
		return instance;
	}

	private double findBestFoodValue(List<List<TaggedWord>> words) {
		return words.stream().flatMap(elem -> elem.stream())
			.map(elem -> foodlistService.getBestMetricsFor(elem.word(), StringDistanceMetric.NormalizedLevenshtein))
			.map(elem -> elem.get(StringDistanceMetric.NormalizedLevenshtein))
			.reduce(Double.MAX_VALUE, (e1, e2) -> Double.min(e1, e2));
		
	}

	@Override
	protected ArrayList<Attribute> getAttributes() {
		ArrayList<Attribute> attributes = new ArrayList<>();
		attributes.add(new Attribute(PRE_TAG_ATTRIBUTES.get(0)));
		attributes.add(new Attribute(PRE_TAG_ATTRIBUTES.get(1)));
		getTagGroupNames().forEach(elem -> attributes.add(new Attribute(elem)));
		attributes.add(new Attribute("isFood"));
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
