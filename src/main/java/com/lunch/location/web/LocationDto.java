package com.lunch.location.web;

import java.util.List;

import org.springframework.data.geo.Point;

import com.lunch.location.services.parser.Menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
	
	private String id;
	private String name;
	private String description;
	private Point geoLocation;
	private List<Menu> menus;

}
