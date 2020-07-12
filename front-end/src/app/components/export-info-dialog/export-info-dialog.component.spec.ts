import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExportInfoDialogComponent } from './export-info-dialog.component';

describe('ExportInfoDialogComponent', () => {
  let component: ExportInfoDialogComponent;
  let fixture: ComponentFixture<ExportInfoDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExportInfoDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExportInfoDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
