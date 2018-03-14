package com.lunch.location.service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.lunch.location.domain.Location;
import com.lunch.location.domain.LocationRepository;
import com.lunch.location.services.parser.Menu;
import com.lunch.location.services.parser.NlpParser;

@Service
public class LocationService {
	
	public static final long MENU_LOOKUP_INTERVAL_SECONDS = 60 * 60 * 3L;
	
	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private MongoOperations mongoOperations;
	
	@Autowired
	private NlpParser menuParser;
	
	public List<Pair<Location, List<Menu>>> getLocations(double latitude, double longitude, Distance distance) {
		ZonedDateTime currentDate = ZonedDateTime.now( ZoneOffset.UTC );
		long timestampSeconds = currentDate.toInstant().getEpochSecond();
		return locationRepository.findByGeoLocationNear(new Point(latitude, longitude), distance).parallelStream()
				.map(elem -> new Pair<Location, List<Menu>>(elem, getMenus(elem, timestampSeconds)))
				.collect(Collectors.toList());
	}
	
	private List<Menu> getMenus(Location location, long timestampSeconds) {
		if (useStoredMenus(location, timestampSeconds)) {
			return location.getMenus();
		} else {
			List<Menu> result = location.getMenuUrls().parallelStream()
					.map(elem ->  menuParser.getMenus(elem))
					.flatMap(elem -> elem.stream())
					.collect(Collectors.toList());
			updateLocationWithMenus(location, result, timestampSeconds);
			return result;
		}
	}

	private boolean useStoredMenus(Location location, long timestampSeconds) {
		return timestampSeconds - MENU_LOOKUP_INTERVAL_SECONDS < location.getLastMenuLookup();
	}
	
	private void updateLocationWithMenus(Location l, List<Menu> menus, long currentTimestampSeconds) {
		mongoOperations.updateFirst(Query.query(Criteria.where("_id").is(l.getId())),
				new Update().set("menus", menus).set("lastMenuLookup", currentTimestampSeconds), Location.class);
	}
	
}
