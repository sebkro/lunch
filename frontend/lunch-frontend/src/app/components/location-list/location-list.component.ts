import { Component, OnInit, ViewChild } from '@angular/core';
import { LocationService } from '../../services/location/location.service';
import { Point, Location, Menu } from './../data-model';

@Component({
  selector: 'app-location-list',
  templateUrl: './location-list.component.html',
  styles: []
})
export class LocationListComponent implements OnInit {

  mobile = false;
  locations: Location[] = [];
  currentMenus: Menu[] = [];
  currentLocation = '';
  isLoading = false;

  constructor(private locationService: LocationService) { }

  findLocations(point: Point) {
    console.log('liste filtern');
    this.isLoading = true;
    this.locationService.findLocations(point).subscribe(elem => {
      this.locations = elem;
      if (this.locations.length > 0) {
        this.currentMenus = this.locations[0].menus;
        this.currentLocation = this.locations[0].id;
      }
    });
    this.isLoading = false;
  }

  ngOnInit() {
    if (document.getElementsByTagName('body')[0].clientWidth <= 768) {
      this.mobile = true;
    }
  }

  locationClicked(event: any) {
    const clickedLocation = this.locations.find(elem => elem.id === event.target.id);
    if (clickedLocation) {
      this.currentMenus = clickedLocation.menus;
      this.currentLocation = clickedLocation.id;
    }
  }

}
