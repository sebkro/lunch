import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';


@Injectable()
export class GoogleService {

  apiKey: 'AIzaSyAZuu3uYIFrFyOQOrOpPYxj8InKJodPDjQ';
  geocodeApiUrl = 'https://maps.googleapis.com/maps/api/geocode/json?address=';
  data: any = null;

  constructor(private http: Http) { }

  geocode() {
    return this.http.get(this.geocodeUrlComposite(null, null))
      .map(res => res.json())
      .subscribe(data => {
        this.data = data;
        console.log(this.data);
      });
  }

  private geocodeUrlComposite(streetAndNumber, postalcodeAndCity) {
    return this.geocodeApiUrl + streetAndNumber + ',' + postalcodeAndCity + '&key=' + this.apiKey;
  }

}
