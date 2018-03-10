import { Injectable } from '@angular/core';
import { } from 'googlemaps';

@Injectable()
export class GoogleService {

  error = false;
  address: string;

  constructor() { }

  getGeoLocation(latitude: number, longitude: number) {
    if (navigator.geolocation) {
      const geocoder = new google.maps.Geocoder();
      const latlng = new google.maps.LatLng(latitude, longitude);
      const request = { location: latlng };
      geocoder.geocode(request, (results, status) => {
        if (status === google.maps.GeocoderStatus.OK) {
          if (results[0] != null) {
            this.address = results[0].formatted_address;
          } else {
            this.error = true;
          }
        }
      });
    }
  }
}
