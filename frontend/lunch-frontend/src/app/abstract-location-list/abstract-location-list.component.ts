import { LocationService } from './../services/location/location.service';
import { Menu, Point, Location } from './../components/data-model';
import { Component, OnInit, Input } from '@angular/core';

export abstract class AbstractLocationListComponent implements OnInit {

  private _geolocation: Point;

  locations: Location[] = [];
  isLoading = false;

  constructor(private locationService: LocationService) { }

  @Input()
  set geolocation(geolocation: Point) {
    this._geolocation = geolocation;
    this.isLoading = true;
    this.locationService.findLocations(geolocation).subscribe(elem => {
      this.locations = elem;
      this.geoLocationsFound();
    });
  }

  abstract geoLocationsFound();

  ngOnInit() {
  }



}
