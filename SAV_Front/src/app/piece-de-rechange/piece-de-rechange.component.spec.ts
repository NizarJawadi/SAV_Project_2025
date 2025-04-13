import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PieceDeRechangeComponent } from './piece-de-rechange.component';

describe('PieceDeRechangeComponent', () => {
  let component: PieceDeRechangeComponent;
  let fixture: ComponentFixture<PieceDeRechangeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PieceDeRechangeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PieceDeRechangeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
