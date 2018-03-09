import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { FilterComponent } from './components/filter/filter.component';
import { LocationListComponent } from './components/location-list/location-list.component';
import { GoogleService } from './services/google/google.service';
import { LocationService } from './services/location/location.service';

@NgModule({
  declarations: [
    AppComponent,
    FilterComponent,
    LocationListComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [GoogleService, LocationService],
  bootstrap: [AppComponent]
})
export class AppModule { }
