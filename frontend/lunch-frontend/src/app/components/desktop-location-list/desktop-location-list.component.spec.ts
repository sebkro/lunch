import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DesktopLocationListComponent } from './desktop-location-list.component';

describe('LocationListComponent', () => {
  let component: DesktopLocationListComponent;
  let fixture: ComponentFixture<DesktopLocationListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DesktopLocationListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DesktopLocationListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
