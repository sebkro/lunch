package com.lunch.location.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
public class GoogleAddressComponent {
	
	String long_name;
	String short_name;
	String[] types;

}
