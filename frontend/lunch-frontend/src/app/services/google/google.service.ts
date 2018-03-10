import { Injectable, NgZone } from '@angular/core';
import { } from 'googlemaps';

@Injectable()
export class GoogleService {

  error = false;
  address: string;

  constructor(private ngZone: NgZone) { }

  getGeoLocation(latitude: number, longitude: number): string {
    if (navigator.geolocation) {
      this.ngZone.run(() => {
        const geocoder = new google.maps.Geocoder();
        const latlng = new google.maps.LatLng(latitude, longitude);
        const request = { location: latlng };
        geocoder.geocode(request, (results, status) => {
          if (status === google.maps.GeocoderStatus.OK) {
            if (results[0] != null) {
              return results[0].formatted_address;
            } else {
              return 'no address found';
            }
          }
        });
      });
    }
    return 'error';
  }
}
