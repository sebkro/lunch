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
  distanceM: number;
  zoom: number;

  constructor(
    private formBuilder: FormBuilder,
    private mapsAPILoader: MapsAPILoader,
    private ngZone: NgZone,
    private googleService: GoogleService
  ) { }

  ngOnInit() {
    this.zoom = 12;
    this.latitude = 53.542733;
    this.longitude = 9.986065;
    this.address = 'Am Sandtorkai 72-73, 20457 Hamburg, Deutschland';
    this.distanceM = 1000;

    this.createForm();
    //this.setCurrentPosition();
  }

  filterButtonClicked($event) {
    if (this.filterForm.valid) {
      this.notifyParent.emit(new Point(this.latitude, this.longitude, this.filterForm.controls['distance'].value));
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
          //get the place result
          const place: google.maps.places.PlaceResult = autocomplete.getPlace();

          //verify result
          if (place.geometry === undefined || place.geometry === null) {
            return;
          }

          //set latitude, longitude and zoom
          this.latitude = place.geometry.location.lat();
          this.longitude = place.geometry.location.lng();
          this.zoom = 12;
        });
      });
    });
  }

  distanceChanged($event) {
    this.distanceM = $event.value * 1000;
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
      address: ['Am Sandtorkai 72-73, 20457 Hamburg, Deutschland', Validators.required],
      distance: ['1', Validators.required]
    });
  }

  private getGeoLocation(latitude: number, longitude: number) {
    if (navigator.geolocation) {
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
}
