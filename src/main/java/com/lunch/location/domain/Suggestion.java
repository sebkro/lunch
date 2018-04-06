package com.lunch.location.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
public class Suggestion {

	private String name;
	private String formattedAddress;
	private String street;
	private String streetNumber;
	private String postalCode;
	private String city;
	private String country;
	// TODO
}
