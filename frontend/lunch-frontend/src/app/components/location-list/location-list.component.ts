import { Component, OnInit } from '@angular/core';
import { LocationService } from '../../services/location/location.service';
import { Point } from './../data-model';

@Component({
  selector: 'app-location-list',
  templateUrl: './location-list.component.html',
  styleUrls: ['./location-list.component.css']
})
export class LocationListComponent implements OnInit {

  constructor(private locationService: LocationService) { }

  findLocations(point: Point) {
    console.log('liste filtern');
    this.locationService.findLocations(point).subscribe(elem => {
      console.log(elem);
    });
  }

  ngOnInit() {
  }

}
