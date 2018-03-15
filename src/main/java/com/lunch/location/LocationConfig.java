package com.lunch.location;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.lunch.location.services.parser.MenuPosTagger;

@Configuration
@EnableMongoRepositories
public class LocationConfig {
	
	@Value("${jwt.publicKey}")
	private String publicKey;

	@Value("${jwt.issuer}")
	private String issuer;
	
	@Value("classpath:german-fast.tagger")
    private Resource posTaggerModelFile;
	
	@Bean
	MenuPosTagger menuPosTagger() throws IOException {
		return new MenuPosTagger(posTaggerModelFile.getFile().getPath());
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
