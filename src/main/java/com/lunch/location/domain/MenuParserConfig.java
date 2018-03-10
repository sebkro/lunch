package com.lunch.location.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuParserConfig {
	
	private boolean parsePreviousElement;
	private boolean parseNextElement;

}
