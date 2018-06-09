import { Menu } from './../data-model';
import { AbstractLocationListComponent } from './../../abstract-location-list/abstract-location-list.component';
import { Component, OnInit } from '@angular/core';
import { LocationService } from '../../services/location/location.service';

@Component({
  selector: 'app-desktop-location-list',
  templateUrl: './desktop-location-list.component.html'
})
export class DesktopLocationListComponent extends AbstractLocationListComponent {

  currentMenus: Menu[] = [];
  currentLocation = '';

  constructor(locationService: LocationService) {
    super(locationService);
  }

  locationClicked(event: any) {
    const clickedLocation = this.locations.find(elem => elem.id === event.target.id);
    if (clickedLocation) {
      this.currentMenus = clickedLocation.menus;
      this.currentLocation = clickedLocation.id;
    }
  }

  geoLocationsFound() {
    if (this.locations.length > 0) {
      this.currentMenus = this.locations[0].menus;
      this.currentLocation = this.locations[0].id;
    }
    this.isLoading = false;
  }

}
