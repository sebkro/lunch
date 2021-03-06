package com.lunch.location;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.google.common.collect.Lists;
import com.lunch.location.services.parser.MenuPosTagger;
import com.lunch.location.services.parser.nlp.FoodListService;
import com.lunch.location.services.parser.nlp.StopWordsService;
import com.lunch.location.services.parser.nlp.WordListSimilarityCalculator;

import weka.classifiers.trees.RandomForest;
import weka.core.SerializationHelper;

@Configuration
@EnableMongoRepositories
public class LocationConfig {
	
	@Value("${jwt.publicKey}")
	private String publicKey;

	@Value("${jwt.issuer}")
	private String issuer;
	
	@Value("classpath:german-fast.tagger")
    private Resource posTaggerModelFile;

	@Value("classpath:randomForest.model")
	private Resource randomForestModel;

	@Value("classpath:foods/essenTrinken.txt")
	private Resource wordsEssenTrinken;

	@Value("classpath:foods/getraenke.txt")
	private Resource wordsGetraenke;

	@Value("classpath:foods/gemuese.txt")
	private Resource wordsGemuese;

	@Value("classpath:stopwords.txt")
	private Resource stopWords;
	
	@Autowired
	private WordListSimilarityCalculator wordListSimilarityCalculator;
	
	@Bean
	public MenuPosTagger menuPosTagger() throws IOException {
		return new MenuPosTagger(posTaggerModelFile.getFile().getPath());
	}

	@Bean
	public RandomForest randomForest() throws Exception {
		return (RandomForest) SerializationHelper.read(randomForestModel.getFile().getPath());
	}
	
	@Bean
	public StopWordsService stopWordsService() throws IOException {
		return new StopWordsService(stopWords.getFile().toPath());
	}

	@Bean
	public FoodListService foodListService() throws IOException {
		List<Path> foodPathes = Lists.newArrayList(wordsEssenTrinken.getFile().toPath(), wordsGemuese.getFile().toPath(), wordsGetraenke.getFile().toPath());
		return new FoodListService(foodPathes, wordListSimilarityCalculator);
	}

	
//	@Bean
//	public FilterRegistrationBean someFilterRegistration() {
//
//	    FilterRegistrationBean registration = new FilterRegistrationBean();
//	    registration.setFilter(jwtFilter());
//	    registration.addUrlPatterns("/*");
//	    registration.setName("jwtFilter");
//	    registration.setOrder(1);
//	    return registration;
//	} 
//
//	public JwtFilter jwtFilter() {
//	    JwtFilter filter = new JwtFilter(publicKey, issuer);
//	    filter.init();
//	    return filter;
//	}

}
