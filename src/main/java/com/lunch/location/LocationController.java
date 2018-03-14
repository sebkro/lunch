package com.lunch.location;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lunch.location.service.LocationService;
import com.lunch.location.web.LocationDto;

@RestController
@RefreshScope
public class LocationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocationController.class);

	@Autowired
	private LocationService locationService;

	@CrossOrigin
	@RequestMapping(method=RequestMethod.GET, produces="application/json", path="/find/{latitude}/{longitude:.+}")
	public List<LocationDto> getValues(
			@PathVariable("longitude") double longitude,
			@PathVariable("latitude") double latitude,
			@RequestParam(value = "distance", defaultValue = "1.0", required = false) double distance) {
		LOGGER.info("Search Locations with latitude {} and longitude {}", latitude, longitude);
		Distance d = new Distance(distance, Metrics.KILOMETERS);
		return locationService.getLocations(latitude, longitude, d).stream()
				.map(elem -> {
					LocationDto dto = new LocationDto();
					dto.setDescription(elem.getValue0().getDescription());
					dto.setId(elem.getValue0().getId());
					dto.setName(elem.getValue0().getName());
					dto.setGeoLocation(elem.getValue0().getGeoLocation());
					dto.setMenus(elem.getValue1());
					return dto;
				})
				.collect(Collectors.toList());
	}
	
}
