package com.lunch.location.domain;

import com.lunch.location.services.parser.Menu;

public class MenuSpec {
	
	public Menu one(int variant) {
		Menu result = Menu.builder()
				.description(String.format("description_%s", variant))
				.price(String.format("%s,99", variant))
				.title(String.format("title_%s", variant))
				.build();
		return result;
		
	}
}
