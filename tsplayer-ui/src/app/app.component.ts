import {Component} from '@angular/core';
import {DEFAULT_SERIES} from './app.constants';
import {AppService} from './app.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [AppService]
})
export class AppComponent {
  DEFAULT_SERIES = DEFAULT_SERIES;

  constructor(public service: AppService) {
  }
}
