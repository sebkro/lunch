import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AbstractLocationListComponent } from './abstract-location-list.component';

describe('AbstractLocationListComponent', () => {
  let component: AbstractLocationListComponent;
  let fixture: ComponentFixture<AbstractLocationListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AbstractLocationListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AbstractLocationListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
