import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Point } from '../../components/data-model';
import { Http, Headers } from '@angular/http';
import 'rxjs/add/operator/retry';
import 'rxjs/add/operator/map';


@Injectable()
export class LocationService {

  constructor(private http: Http) { }

  findLocations(point: Point) {
    const query = point.latitude + '/' + point.longitude + '?distance=' + point.distance;
    const headers = new Headers();

    return this.http
      .get('http://localhost:9001/find/' + query, { headers: headers })
      .retry(3)
      .map(response => response.json());
  }
}
