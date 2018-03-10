package com.lunch.location.domain;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.geo.Point;

public class LocationSpec {
	
	public Location one(int variant) {
		Location result = new Location();
		result.setId(String.format("id_%s", variant));
		result.setDescription(String.format("description_%s", variant));
		result.setName(String.format("name_%s", variant));
		result.setMenuUrls(IntStream.range(0, variant)
				.mapToObj(elem -> String.format("http://testurl%s/%s", variant, elem))
				.collect(Collectors.toList()));
		result.setMenuParserConfig(new MenuParserConfig(false, false));
		result.setGeoLocation(new Point(Double.valueOf(variant), Double.valueOf(variant)));
		return result;
		
	}
}
