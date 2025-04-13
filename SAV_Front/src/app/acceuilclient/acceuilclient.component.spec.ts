import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AcceuilclientComponent } from './acceuilclient.component';

describe('AcceuilclientComponent', () => {
  let component: AcceuilclientComponent;
  let fixture: ComponentFixture<AcceuilclientComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AcceuilclientComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AcceuilclientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
