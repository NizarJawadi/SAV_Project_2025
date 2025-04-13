import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VetrineProduitComponent } from './vetrine-produit.component';

describe('VetrineProduitComponent', () => {
  let component: VetrineProduitComponent;
  let fixture: ComponentFixture<VetrineProduitComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VetrineProduitComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(VetrineProduitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
