package com.lunch.location.services.parser.nlp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class StopWordsService {
	
	private Set<String> stopWords;
	
	public StopWordsService(Path stopWordsPath) throws IOException {
		this.stopWords = new HashSet<>();
		Files.readAllLines(stopWordsPath).stream()
			.map(String::trim)
			.forEach(elem -> this.stopWords.add(elem));
	}
	public StopWordsService(Set<String> stopWords) {
		this.stopWords = stopWords;
	}
	
	public boolean isStopWord(String word) {
		return stopWords.contains(word);
	}

	public boolean isNotStopWord(String word) {
		return !isNotStopWord(word);
	}
	
	

}
