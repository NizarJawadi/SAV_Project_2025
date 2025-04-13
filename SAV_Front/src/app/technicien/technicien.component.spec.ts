import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TechnicienComponent } from './technicien.component';

describe('TechnicienComponent', () => {
  let component: TechnicienComponent;
  let fixture: ComponentFixture<TechnicienComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TechnicienComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TechnicienComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
