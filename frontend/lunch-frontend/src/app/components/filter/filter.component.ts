import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-filter',
  templateUrl: './filter.component.html',
  styleUrls: ['./filter.component.css']
})
export class FilterComponent implements OnInit {

  @Output() notifyParent: EventEmitter<FormGroup> = new EventEmitter<FormGroup>();


  filterForm: FormGroup;
  error = false;

  constructor(private formBuilder: FormBuilder) {
    this.createForm();
  }

  createForm() {
    this.filterForm = this.formBuilder.group({
      address: ['', Validators.required],
      distance: ['', Validators.required]
    });
  }

  filterButtonClicked(event) {
    if (this.filterForm.valid) {
      this.notifyParent.emit(this.filterForm);
    } else {
      this.error = true;
    }
  }

  ngOnInit() {
  }

}
