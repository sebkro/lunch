package com.lunch.location;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.lunch.location.services.parser.MenuPosTagger;
import com.lunch.location.services.parser.nlp.StopWordsService;

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

	@Value("classpath:essenTriken.txt")
	private Resource wordsEssenTrinken;

	@Value("classpath:getraenke.txt")
	private Resource wordsGetraenke;

	@Value("classpath:gemuese.txt")
	private Resource wordsGemuese;

	@Value("classpath:stopwords.txt")
	private Resource stopWords;
	
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
