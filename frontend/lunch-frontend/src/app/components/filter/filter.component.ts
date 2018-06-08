import { Component, OnInit, Output, EventEmitter, NgZone, ElementRef, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Point } from './../data-model';
import { GoogleService } from '../../services/google/google.service';
import { } from '@types/googlemaps';

@Component({
  selector: 'app-filter',
  templateUrl: './filter.component.html',
  styles: []
})
export class FilterComponent implements OnInit {

  @Output() notifyParent: EventEmitter<Point> = new EventEmitter<Point>();

  @ViewChild('search') public searchElementRef: ElementRef;

  @ViewChild('gmap') gmapElement: any;
  map: google.maps.Map;

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
  showMap: false;
  try = 0;

  constructor(
    private formBuilder: FormBuilder,
    private ngZone: NgZone,
    private googleService: GoogleService
  ) { }

  ngOnInit() {
    this.zoom = 12;
    this.address = '';

    if (this.showMap) {
      const mapProp = {
        center: new google.maps.LatLng(18.5793, 73.8143),
        zoom: 12,
        mapTypeId: google.maps.MapTypeId.ROADMAP
      };
      this.map = new google.maps.Map(this.gmapElement.nativeElement, mapProp);
    }


    // if (navigator.geolocation) {
    //   navigator.geolocation.getCurrentPosition(
    //     success => {
    //       this.latitude = success.coords.latitude;
    //       this.longitude = success.coords.longitude;
    //       this.getGeoLocation(this.latitude, this.longitude);
    //     },
    //    error => {
    //     this.latitude = 53.542733;
    //     this.longitude = 9.986065;
    //     this.address = 'Am Sandtorkai 72-73, 20457 Hamburg, Deutschland';
    //    });
    // }
    this.distanceM = '1000';

    this.createForm();
    this.userAgent = navigator.userAgent;
    this.onlineState = navigator.onLine ? 'online' : 'offline';
    this.supportsGeo = navigator.geolocation ? 'true' : 'false';
    this.installAutocomplete();
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
    const input = this.filterForm.controls['address'].value;

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

  private installAutocomplete() {
    if (this.try < 5 && typeof google !== 'undefined' && typeof google.maps !== 'undefined' && typeof google.maps.places !== 'undefined') {
      const autocomplete = new google.maps.places.Autocomplete(this.searchElementRef.nativeElement, {
        types: ['address']
      });
    } else {
      this.try++;
      console.log("Install try " + this.try);
      setTimeout(() => this.installAutocomplete(), 1000);
    }
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
