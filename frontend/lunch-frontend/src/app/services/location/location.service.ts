import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Point } from '../../components/data-model';


@Injectable()
export class LocationService {

  findLocation(point: Point) {
    console.log(point.latitude, point.longitude);
  }

  constructor() { }

}
