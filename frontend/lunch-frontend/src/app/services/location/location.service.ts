  import { Injectable, Inject } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Point, Location, LocationFactory } from '../../components/data-model';
import { Http, Headers } from '@angular/http';
import 'rxjs/add/operator/retry';
import 'rxjs/add/operator/map';


@Injectable()
export class LocationService {

  constructor(
    @Inject('LOCATION_SERVICE_URL') private apiUrl: string,
    private http: Http) { }

  findLocations(point: Point): Observable<Array<Location>> {
    const query = point.latitude + '/' + point.longitude + '?distance=' + point.distance;
    const headers = new Headers();
    headers.append('Content-Type', 'application/json');

    return this.http
      .get(this.apiUrl + '/find/' + query, { headers: headers })
      .retry(3)
      .map(response => response.json())
      .map(rawLocations => rawLocations
        .map(rawLocation => LocationFactory.fromObject(rawLocation))
      );
  }
}
