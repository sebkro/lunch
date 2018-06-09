import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MobileLocationListComponent } from './mobile-location-list.component';

describe('MobileLocationListComponent', () => {
  let component: MobileLocationListComponent;
  let fixture: ComponentFixture<MobileLocationListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MobileLocationListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MobileLocationListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
