import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LeadingPageComponent } from './leading-page.component';

describe('LeadingPageComponent', () => {
  let component: LeadingPageComponent;
  let fixture: ComponentFixture<LeadingPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LeadingPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(LeadingPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
