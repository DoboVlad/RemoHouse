import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateGSMComponent } from './update-gsm.component';

describe('UpdateGSMComponent', () => {
  let component: UpdateGSMComponent;
  let fixture: ComponentFixture<UpdateGSMComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UpdateGSMComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateGSMComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
