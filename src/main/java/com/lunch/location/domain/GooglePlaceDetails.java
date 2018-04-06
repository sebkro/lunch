package com.lunch.location.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
public class GooglePlaceDetails {

	private String name;
	private String formatted_address;
	private GoogleAddressComponent[] address_components;

	public Optional<GoogleAddressComponent> getAddressComponent(GoogleAddressPart addressPart) {
		List<GoogleAddressComponent> addressComponents = Arrays.stream(address_components)
				.filter(a -> Arrays.stream(a.getTypes()).anyMatch(t -> t.equals(addressPart.toString()))).collect(Collectors.toList());
		return addressComponents.isEmpty() ? Optional.empty() : Optional.of(addressComponents.get(0));
	}
}
