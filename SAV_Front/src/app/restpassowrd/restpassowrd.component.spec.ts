import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RestpassowrdComponent } from './restpassowrd.component';

describe('RestpassowrdComponent', () => {
  let component: RestpassowrdComponent;
  let fixture: ComponentFixture<RestpassowrdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RestpassowrdComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RestpassowrdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
