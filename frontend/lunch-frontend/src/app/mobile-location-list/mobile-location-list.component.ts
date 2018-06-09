import { LocationService } from './../services/location/location.service';
import { AbstractLocationListComponent } from './../abstract-location-list/abstract-location-list.component';
import { Component, OnInit } from '@angular/core';
import { Location } from '../components/data-model';

@Component({
  selector: 'app-mobile-location-list',
  templateUrl: './mobile-location-list.component.html'
})
export class MobileLocationListComponent extends AbstractLocationListComponent implements OnInit {

  location: Location;
  index = 0;
  private swipeCoord?: [number, number];
  private swipeTime?: number;

  constructor(locationService: LocationService) {
    super(locationService);
  }

  geoLocationsFound() {
    if (this.locations.length > 0) {
      this.location = this.locations[this.index];
    }
    this.isLoading = false;
  }

  swipe(e: TouchEvent, when: string): void {
    const coord: [number, number] = [e.changedTouches[0].pageX, e.changedTouches[0].pageY];
    const time = new Date().getTime();

    if (when === 'start') {
      this.swipeCoord = coord;
      this.swipeTime = time;
    } else if (when === 'end') {
      const direction = [coord[0] - this.swipeCoord[0], coord[1] - this.swipeCoord[1]];
      const duration = time - this.swipeTime;

      if (this.locations.length > 1 && duration < 1000 && Math.abs(direction[0]) > 30
        && Math.abs(direction[0]) > Math.abs(direction[1] * 3)) {
      const swipe = direction[0] < 0 ? 'next' : 'previous';
      this.index = direction[0] < 0 ? this.index + 1 : this.index - 1;
      if (this.index > this.locations.length) {
        this.index = 0;
      }
      if (this.index < 0) {
        this.index = this.locations.length - 1;
      }
      this.location = this.locations[this.index];
      }
    }
  }

}
