package com.lunch.location.domain;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LocationRepository extends PagingAndSortingRepository<Location, ObjectId> {

	public Location findOneById(String id);
	public List<Location> findByGeoLocationNear(Point point, Distance max);

}
