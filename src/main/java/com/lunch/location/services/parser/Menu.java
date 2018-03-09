package com.lunch.location.services.parser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Menu {
	
	private String title;
	private String description;
	private String price;

}
