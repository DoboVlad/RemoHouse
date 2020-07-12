import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ControllerToggleComponent } from './controller-toggle.component';

describe('ControllerToggleComponent', () => {
  let component: ControllerToggleComponent;
  let fixture: ComponentFixture<ControllerToggleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ControllerToggleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ControllerToggleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
