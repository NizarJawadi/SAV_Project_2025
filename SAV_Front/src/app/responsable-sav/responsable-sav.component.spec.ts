import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResponsableSAVComponent } from './responsable-sav.component';

describe('ResponsableSAVComponent', () => {
  let component: ResponsableSAVComponent;
  let fixture: ComponentFixture<ResponsableSAVComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResponsableSAVComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ResponsableSAVComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
