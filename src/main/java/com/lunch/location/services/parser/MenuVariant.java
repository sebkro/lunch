package com.lunch.location.services.parser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@Builder
@ToString
public class MenuVariant {
	
	private String description;
	private String price;

}
