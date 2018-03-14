package com.lunch.location.services.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

@Service
public class MenuPosTagger {
	
	@Value("classpath:german-fast.tagger")
    private Resource posTaggerModelFile;
	
	private MaxentTagger tagger;
	
	@PostConstruct
	public void init() throws IOException {
		tagger = new MaxentTagger(posTaggerModelFile.getFile().getPath());
	}
	
	public List<List<TaggedWord>> posTag(Element element) {
		return MaxentTagger.tokenizeText(new BufferedReader(new StringReader(element.text()))).stream()
				.map(elem -> tagger.tagSentence(elem))
				.collect(Collectors.toList());
	}

}
