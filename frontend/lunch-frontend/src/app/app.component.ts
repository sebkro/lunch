import { Component, ViewChild } from '@angular/core';
import { LocationListComponent } from './components/location-list/location-list.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'LunchFinder';

  @ViewChild(LocationListComponent) locationListComponent: LocationListComponent;

  findLocationsButtonClicked(formGroup) {
    this.locationListComponent.findLocations(formGroup);
  }
}
