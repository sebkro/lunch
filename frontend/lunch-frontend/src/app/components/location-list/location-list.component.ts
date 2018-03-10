import { Component, OnInit } from '@angular/core';
import { LocationService } from '../../services/location/location.service';
import { Point, Location } from './../data-model';

@Component({
  selector: 'app-location-list',
  templateUrl: './location-list.component.html',
  styles: []
})
export class LocationListComponent implements OnInit {

  locations: Location[];

  constructor(private locationService: LocationService) { }

  findLocations(point: Point) {
    console.log('liste filtern');
    this.locationService.findLocations(point).subscribe(elem => {
      this.locations = elem;
    });
  }

  ngOnInit() {
  }

}
