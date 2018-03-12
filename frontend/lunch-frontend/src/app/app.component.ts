import { Component, ViewChild } from '@angular/core';
import { LocationListComponent } from './components/location-list/location-list.component';
import { Point } from './components/data-model';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'LunchFinder';
  isLoading = false;

  @ViewChild(LocationListComponent) locationListComponent: LocationListComponent;

  findLocationsButtonClicked(point: Point) {
    this.locationListComponent.findLocations(point);
  }
}
