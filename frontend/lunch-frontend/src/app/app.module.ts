import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { FilterComponent } from './components/filter/filter.component';
import { GoogleService } from './services/google/google.service';
import { LocationService } from './services/location/location.service';
import { HttpModule } from '@angular/http';
import { SpinLoaderComponent } from './components/shared/spin-loader/spin-loader.component';
import { ServiceWorkerModule } from '@angular/service-worker';
import { environment } from '../environments/environment';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MobileLocationListComponent } from './mobile-location-list/mobile-location-list.component';
import { DesktopLocationListComponent } from './components/desktop-location-list/desktop-location-list.component';

@NgModule({
  declarations: [
    AppComponent,
    FilterComponent,
    SpinLoaderComponent,
    MobileLocationListComponent,
    DesktopLocationListComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,
    environment.production ? ServiceWorkerModule.register('ngsw-worker.js') : []
  ],
  exports : [
    SpinLoaderComponent
  ],
  providers: [GoogleService, LocationService],
  bootstrap: [AppComponent]
})
export class AppModule { }
