import { Component, OnInit, Output, EventEmitter, NgZone, ElementRef, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { } from 'googlemaps';
import { MapsAPILoader } from '@agm/core';
import { Point } from './../data-model';
import { GoogleService } from '../../services/google/google.service';

@Component({
  selector: 'app-filter',
  templateUrl: './filter.component.html',
  styles: []
})
export class FilterComponent implements OnInit {

  @Output() notifyParent: EventEmitter<Point> = new EventEmitter<Point>();

  @ViewChild('search') public searchElementRef: ElementRef;

  filterForm: FormGroup;
  error = false;
  address: string;
  latitude: number;
  longitude: number;
  distanceM: string;
  zoom: number;
  onlineState: string;
  userAgent: string;
  supportsGeo: string;

  constructor(
    private formBuilder: FormBuilder,
    private mapsAPILoader: MapsAPILoader,
    private ngZone: NgZone,
    private googleService: GoogleService
  ) { }

  ngOnInit() {
    this.zoom = 12;
    this.address = '';

    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        success => {
          this.latitude = success.coords.latitude;
          this.longitude = success.coords.longitude;
          this.getGeoLocation(this.latitude, this.longitude);
        },
       error => {
        this.latitude = 53.542733;
        this.longitude = 9.986065;
        this.address = 'Am Sandtorkai 72-73, 20457 Hamburg, Deutschland';
       });
    }
    this.distanceM = '1000';

    this.createForm();
    this.userAgent = navigator.userAgent;
    this.onlineState = navigator.onLine ? 'online' : 'offline';
    this.supportsGeo = navigator.geolocation ? 'true' : 'false';
  }

  filterButtonClicked($event) {
    if (this.filterForm.valid) {
      console.log(this.filterForm.controls['distanceM'].value);
      this.notifyParent.emit(new Point(this.latitude, this.longitude, this.filterForm.controls['distanceM'].value));
    } else {
      this.error = true;
    }
  }

  addressChanged() {
    this.mapsAPILoader.load().then(() => {
      const autocomplete = new google.maps.places.Autocomplete(this.searchElementRef.nativeElement, {
        types: ['address']
      });
      autocomplete.addListener('place_changed', () => {
        this.ngZone.run(() => {
          // get the place result
          const place: google.maps.places.PlaceResult = autocomplete.getPlace();

          // verify result
          if (place.geometry === undefined || place.geometry === null) {
            return;
          }

          // set latitude, longitude and zoom
          this.latitude = place.geometry.location.lat();
          this.longitude = place.geometry.location.lng();
          this.zoom = 12;
        });
      });
    });
  }

  placeMarker($event) {
    this.latitude = $event.coords.lat;
    this.longitude = $event.coords.lng;
    if (this.latitude != null && this.longitude != null) {
      this.ngZone.run(() => {
        this.getGeoLocation(this.latitude, this.longitude);
      });
    }
  }

  private setCurrentPosition() {
    if ('geolocation' in navigator) {
      navigator.geolocation.getCurrentPosition((position) => {
        this.latitude = position.coords.latitude;
        this.longitude = position.coords.longitude;
      });
    }
  }

  private createForm() {
    this.filterForm = this.formBuilder.group({
      address: [this.address, Validators.required],
      distanceM: [this.distanceM, Validators.required]
    });
  }

  private getGeoLocation(latitude: number, longitude: number) {
    this.ngZone.run(() => {
      const geocoder = new google.maps.Geocoder();
      const latlng = new google.maps.LatLng(latitude, longitude);
      const request = { location: latlng };
      geocoder.geocode(request, (results, status) => {
        if (status === google.maps.GeocoderStatus.OK) {
          if (results[0] != null) {
            this.address = results[0].formatted_address;
            this.filterForm.controls['address'].setValue(this.address);
          } else {
            this.error = true;
          }
        }
      });
    });
  }
}
