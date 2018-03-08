package com.lunch.location.web.dto;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

public class RestTemplateTest {
	
	public static void main(String[] args) {
		RestTemplate restTemplate = new RestTemplate();
		
		Person p = new Person();
		p.age = 27;
		p.firstname = "Jennifer";
		p.lastname = "Kempa";
		HttpEntity<Person> request = new HttpEntity<>(p);
		Person foo = restTemplate.postForObject("http://localhost:8080/persons", request, Person.class);
		System.out.println(foo);
	}

}
