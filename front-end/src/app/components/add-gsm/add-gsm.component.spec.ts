import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddGSMComponent } from './add-gsm.component';

describe('AddGSMComponent', () => {
  let component: AddGSMComponent;
  let fixture: ComponentFixture<AddGSMComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddGSMComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddGSMComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
