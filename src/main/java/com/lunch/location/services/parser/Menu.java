package com.lunch.location.services.parser;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@Builder
@ToString
public class Menu {
	
	private String title;
	private String description;
	private String price;
	private List<MenuVariant> variants;

}
