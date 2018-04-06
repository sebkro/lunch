package com.lunch.location.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.lunch.location.domain.GoogleAddressComponent;
import com.lunch.location.domain.GoogleAddressPart;
import com.lunch.location.domain.GooglePlace;
import com.lunch.location.domain.GooglePlaceDetails;
import com.lunch.location.domain.GooglePlaceDetailsResponse;
import com.lunch.location.domain.GooglePlaceResponse;
import com.lunch.location.domain.Suggestion;

@Service
public class SuggestionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SuggestionService.class);

	private static final String GOOGLE_PLACES_ENDPOINT = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

	private static final String GOOGLE_PLACES_DETAILS_ENDPOINT = "https://maps.googleapis.com/maps/api/place/details/json?";

	private static final int KILOMETER_IN_METERS = 1000;

	public List<Suggestion> getSuggestions(double latitude, double longitude, Distance distance, String keyword) {

		String location = StringUtils.join(latitude + "," + longitude);
		String radius = String.valueOf(distance.getValue() * KILOMETER_IN_METERS);

		List<GooglePlace> places = getGooglePlaces(location, radius, keyword);
		List<GooglePlaceDetails> placesDetails = places.stream().map(p -> getGooglePlaceDetails(p)).collect(Collectors.toList());
		List<Suggestion> suggestions = placesDetails.stream().map(p -> mapGooglePlaceDetailsToSuggestion(p)).collect(Collectors.toList());

		LOGGER.info(suggestions.toString());

		return suggestions;
	}

	private List<GooglePlace> getGooglePlaces(String location, String radius, String keyword) {

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(GOOGLE_PLACES_ENDPOINT)
				.queryParam("key", "AIzaSyAZuu3uYIFrFyOQOrOpPYxj8InKJodPDjQ")
				.queryParam("location", location)
				.queryParam("radius", radius)
				.queryParam("keyword", keyword);

		LOGGER.info("Calling Google Places API '{}'", builder.toUriString());

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<GooglePlaceResponse> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, GooglePlaceResponse.class);
		return Arrays.asList(response.getBody().getResults());
	}

	private GooglePlaceDetails getGooglePlaceDetails(GooglePlace googlePlace) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(GOOGLE_PLACES_DETAILS_ENDPOINT)
				.queryParam("key", "AIzaSyAZuu3uYIFrFyOQOrOpPYxj8InKJodPDjQ")
				.queryParam("placeid", googlePlace.getPlace_id());

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<GooglePlaceDetailsResponse> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null,
				GooglePlaceDetailsResponse.class);
		return response.getBody().getResult();
	}

	private Suggestion mapGooglePlaceDetailsToSuggestion(GooglePlaceDetails placeDetails) {
		Suggestion suggestion = new Suggestion();

		suggestion.setName(placeDetails.getName());
		suggestion.setFormattedAddress(placeDetails.getFormatted_address());
		suggestion.setStreet(getLongName(placeDetails.getAddressComponent(GoogleAddressPart.route)));
		suggestion.setStreetNumber(getLongName(placeDetails.getAddressComponent(GoogleAddressPart.street_number)));
		suggestion.setCity(getLongName(placeDetails.getAddressComponent(GoogleAddressPart.locality)));
		suggestion.setPostalCode(getLongName(placeDetails.getAddressComponent(GoogleAddressPart.postal_code)));
		suggestion.setCountry(getLongName(placeDetails.getAddressComponent(GoogleAddressPart.country)));

		return suggestion;
	}

	private String getLongName(Optional<GoogleAddressComponent> addressComponent) {
		if (addressComponent.isPresent()) {
			return addressComponent.get().getLong_name();
		} else {
			return "";
		}
	}

	public static void main(String[] args) {
		SuggestionService suggestionService = new SuggestionService();
		suggestionService.getSuggestions(53.611424, 9.883784, new Distance(1.0, Metrics.KILOMETERS), "Restaurant");
	}
}
