package com.lunch.location.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;

import lombok.Data;

@Data
public class Location {

	@Id
	private String id;
	private String name;
	private String description;
	private Point geoLocation;
	private List<String> menuUrls;
	private MenuParserConfig menuParserConfig;
	

}
