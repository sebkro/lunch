import { Component, OnInit } from '@angular/core';
import { LocationService } from '../../services/location/location.service';

@Component({
  selector: 'app-location-list',
  templateUrl: './location-list.component.html',
  styleUrls: ['./location-list.component.css']
})
export class LocationListComponent implements OnInit {

  constructor(private locationService: LocationService) { }

  findLocations(formGroup) {
    this.locationService.findLocation(formGroup);
  }

  ngOnInit() {
  }

}
