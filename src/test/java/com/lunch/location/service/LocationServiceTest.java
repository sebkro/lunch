package com.lunch.location.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.javatuples.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;

import com.google.common.collect.Lists;
import com.lunch.location.domain.Location;
import com.lunch.location.domain.LocationRepository;
import com.lunch.location.domain.LocationSpec;
import com.lunch.location.domain.MenuSpec;
import com.lunch.location.services.parser.Menu;
import com.lunch.location.services.parser.PriceOrientatedParser;

public class LocationServiceTest {

	@InjectMocks
	private LocationService locationService = new LocationService();

	@Mock
	private LocationRepository locationRepository;

	@Mock
	private PriceOrientatedParser menuParser;

	private List<Location> locations;
	private List<Menu> menusLocation1;
	private List<Menu> menusLocation2Part1;
	private List<Menu> menusLocation2Part2;

	private LocationSpec locationSpec = new LocationSpec();

	private MenuSpec menuSpec = new MenuSpec();

	@Before
	public void init() throws MalformedURLException {
		MockitoAnnotations.initMocks(this);

		locations = IntStream.range(1, 3).mapToObj(locationSpec::one).collect(Collectors.toList());
		menusLocation1 = IntStream.range(1, 3).mapToObj(menuSpec::one).collect(Collectors.toList());
		menusLocation2Part1 = IntStream.range(4, 5).mapToObj(menuSpec::one).collect(Collectors.toList());
		menusLocation2Part2 = IntStream.range(5, 6).mapToObj(menuSpec::one).collect(Collectors.toList());

		when(locationRepository.findByGeoLocationNear(any(Point.class), any(Distance.class))).thenReturn(locations);
		when(menuParser.getMenus(locations.get(0).getMenuUrls().get(0), false, false)).thenReturn(menusLocation1);
		when(menuParser.getMenus(locations.get(1).getMenuUrls().get(0), false, false)).thenReturn(menusLocation2Part1);
		when(menuParser.getMenus(locations.get(1).getMenuUrls().get(1), false, false)).thenReturn(menusLocation2Part2);

	}

	@Test
	public void itShouldReturnLocationsWithMenus() {

		// when
		List<Pair<Location, List<Menu>>> result = locationService.getLocations(53.543654, 9.982564,
				new Distance(1.0, Metrics.KILOMETERS));

		// then
		List<Pair<Location, List<Menu>>> expected = new ArrayList<>();
		expected.add(new Pair<Location, List<Menu>>(locations.get(0), menusLocation1));

		List<Menu> expectedMenusLocation2 = Lists.newArrayList(menusLocation2Part1);
		expectedMenusLocation2.addAll(menusLocation2Part2);
		expected.add(new Pair<Location, List<Menu>>(locations.get(1), expectedMenusLocation2));
		Assert.assertEquals(expected, result);
	}

}
