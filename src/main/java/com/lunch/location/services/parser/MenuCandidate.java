package com.lunch.location.services.parser;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuCandidate {
	
	private String rootElement;
	private List<String> prevElements;

}
