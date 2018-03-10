package com.lunch.location.service;

import java.util.List;
import java.util.stream.Collectors;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import com.lunch.location.domain.Location;
import com.lunch.location.domain.LocationRepository;
import com.lunch.location.domain.MenuParserConfig;
import com.lunch.location.services.parser.Menu;
import com.lunch.location.services.parser.PriceOrientatedParser;

@Service
public class LocationService {
	
	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private PriceOrientatedParser menuParser;
	
	public List<Pair<Location, List<Menu>>> getLocations(double longitude, double latitude, Distance distance) {
		return locationRepository.findByGeoLocationNear(new Point(latitude, longitude), distance).parallelStream()
				.map(elem -> new Pair<Location, List<Menu>>(elem, getMenus(elem)))
				.collect(Collectors.toList());
	}

	private List<Menu> getMenus(Location location) {
		MenuParserConfig config = location.getMenuParserConfig();
		boolean searchPre = config == null ? false : config.isParsePreviousElement();
		boolean searchPost = config == null ? false : config.isParseNextElement();
		return location.getMenuUrls().parallelStream()
				.map(elem ->  menuParser.getMenus(elem, searchPre, searchPost))
				.flatMap(elem -> elem.stream())
				.collect(Collectors.toList());
	}

}
