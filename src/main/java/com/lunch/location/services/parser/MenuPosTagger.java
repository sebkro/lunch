package com.lunch.location.services.parser;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

@Service
public class MenuPosTagger {
	
	private MaxentTagger tagger;
	
	public MenuPosTagger(String modelFile) {
		this.tagger = new MaxentTagger(modelFile);
	}
	
	public List<List<TaggedWord>> posTag(String element) {
		return MaxentTagger.tokenizeText(new BufferedReader(new StringReader(element))).stream()
				.map(elem -> tagger.tagSentence(elem))
				.collect(Collectors.toList());
	}

}
