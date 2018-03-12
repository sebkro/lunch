import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-spin-loader',
  templateUrl: './spin-loader.component.html',
  styleUrls: ['./spin-loader.component.css']
})
export class SpinLoaderComponent implements OnInit {

  @Input() width: string;
  @Input() height: string;

  constructor() { }

  ngOnInit() {
  }

}
