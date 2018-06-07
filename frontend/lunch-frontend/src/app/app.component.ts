import { Component, ViewChild, OnInit } from '@angular/core';
import { LocationListComponent } from './components/location-list/location-list.component';
import { Point } from './components/data-model';
import {
  trigger,
  state,
  style,
  animate,
  transition
} from '@angular/animations';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  animations: [
    trigger('flyInOut', [
      state('in', style({transform: 'translateY(0)'})),
      transition(':enter', [
        style({transform: 'translateY(100%)'}),
        animate(500)
      ])
    ])
  ]
})
export class AppComponent implements OnInit {
  title = 'LunchFinder';
  isLoading = false;
  initialized = false;
  state = 'in';

  @ViewChild(LocationListComponent) locationListComponent: LocationListComponent;

  ngOnInit() {
    const startInit: number = JSON.parse(window.localStorage.getItem('startInit'));
    const timeSinceInit = Date.now() - startInit;
    const isMobile = window.innerWidth < 992;
    const timeout = timeSinceInit < 1500 && isMobile ? 1500 - timeSinceInit : 0;
    setTimeout(() => {
      const d = document.getElementById('appInit');
      d.classList.add('d-none');
      d.classList.remove('d-block');
      this.initialized = true;
    }, timeout);
  }

  findLocationsButtonClicked(point: Point) {
    this.locationListComponent.findLocations(point);
  }
}
