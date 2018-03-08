package com.lunch.location;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lunch.location.domain.Location;
import com.lunch.location.domain.LocationRepository;

@RestController
@RefreshScope
public class LocationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocationController.class);

	@Value("${info.value:no value}")
	private String special;

	@Value("${info.bla:no value}")
	private String bla;

	@Autowired
	private LocationRepository repository;

	@RequestMapping(method=RequestMethod.GET, produces="application/json", path="/find/{latitude}/{longitude:.+}")
	
	public List<Location> getValues(
			@PathVariable("longitude") double longitude,
			@PathVariable("latitude") double latitude,
			@RequestParam(value = "distance", defaultValue = "1.0", required = false) double distance) {
		LOGGER.info("Search Locations with latitude {} and longitude {}", latitude, longitude);
		Point p = new Point(latitude, longitude);
		Distance d = new Distance(distance, Metrics.KILOMETERS);
		return repository.findByGeoLocationNear(p, d);
	}

	
	

}
