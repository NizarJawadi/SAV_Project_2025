import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileClientComponent } from './profile-client.component';

describe('ProfileClientComponent', () => {
  let component: ProfileClientComponent;
  let fixture: ComponentFixture<ProfileClientComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfileClientComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProfileClientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
