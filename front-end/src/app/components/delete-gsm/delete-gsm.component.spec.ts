import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteGSMComponent } from './delete-gsm.component';

describe('DeleteGSMComponent', () => {
  let component: DeleteGSMComponent;
  let fixture: ComponentFixture<DeleteGSMComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DeleteGSMComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteGSMComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
