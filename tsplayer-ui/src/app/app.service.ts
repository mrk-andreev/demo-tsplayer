import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, Subject} from 'rxjs';
import {Values, ValuesRange} from './app.models';

@Injectable()
export class AppService {
  private values = new Subject<Values[]>();
  private range = new Subject<ValuesRange>();

  constructor(private http: HttpClient) {
  }

  getValues(): Observable<Values[]> {
    return this.values.asObservable();
  }

  getRange(): Observable<ValuesRange> {
    return this.range.asObservable();
  }
}
