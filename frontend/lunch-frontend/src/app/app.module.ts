import { AgmCoreModule } from '@agm/core';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { FilterComponent } from './components/filter/filter.component';
import { LocationListComponent } from './components/location-list/location-list.component';
import { GoogleService } from './services/google/google.service';
import { LocationService } from './services/location/location.service';
import { HttpModule } from '@angular/http';
import { MatSliderModule } from '@angular/material';
import { SpinLoaderComponent } from './components/shared/spin-loader/spin-loader.component';
import { ServiceWorkerModule } from '@angular/service-worker';
import { environment } from '../environments/environment';

@NgModule({
  declarations: [
    AppComponent,
    FilterComponent,
    LocationListComponent,
    SpinLoaderComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,
    MatSliderModule,
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyAZuu3uYIFrFyOQOrOpPYxj8InKJodPDjQ',
      libraries: ['places']
    }),
    environment.production ? ServiceWorkerModule.register('ngsw-worker.js') : []
  ],
  exports : [
    SpinLoaderComponent
  ],
  providers: [GoogleService, LocationService],
  bootstrap: [AppComponent]
})
export class AppModule { }
