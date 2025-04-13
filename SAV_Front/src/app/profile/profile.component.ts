import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HeaderComponent } from '../header/header.component';


interface User {
  name: string;
  job: string;
  twitter: string;
  facebook: string;
  instagram: string;
  linkedin: string;
  about: string;
  [key: string]: any;
}
@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule , ReactiveFormsModule , FormsModule , HeaderComponent],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {
  user: User = {
    name: 'Kevin Anderson',
    job: 'Web Designer',
    twitter: 'https://twitter.com/#',
    facebook: 'https://facebook.com/#',
    instagram: 'https://instagram.com/#',
    linkedin: 'https://linkedin.com/#',
    about: 'Sunt est soluta temporibus accusantium...',
  };

  profileDetails = [
    { label: 'Full Name', value: 'Kevin Anderson' },
    { label: 'Company', value: 'Lueilwitz, Wisoky and Leuschke' },
    { label: 'Job', value: 'Web Designer' },
    { label: 'Country', value: 'USA' },
    { label: 'Address', value: 'A108 Adam Street, New York, NY 535022' },
    { label: 'Phone', value: '(436) 486-3538 x29071' },
    { label: 'Email', value: 'k.anderson@example.com' },
  ];

  editableFields = [
    { id: 'fullName', label: 'Full Name' },
    { id: 'company', label: 'Company' },
    { id: 'job', label: 'Job' },
    { id: 'country', label: 'Country' },
    { id: 'address', label: 'Address' },
    { id: 'phone', label: 'Phone' },
    { id: 'email', label: 'Email' }
  ];

  notifications = [
    { name: 'changesMade', label: 'Changes made to your account', checked: true },
    { name: 'newProducts', label: 'Information on new products and services', checked: true },
    { name: 'proOffers', label: 'Marketing and promo offers', checked: false },
    { name: 'securityNotify', label: 'Security alerts', checked: true }
  ];

  passwords = {
    current: '',
    new: '',
    reNew: ''
  };

  saveChanges() {
    console.log('Changes Saved:', this.user);
  }

  saveSettings() {
    console.log('Settings Saved:', this.notifications);
  }

  changePassword() {
    console.log('Password Changed:', this.passwords);
  }

  updateUserProperty(key: keyof User, value: string) {
    (this.user as any)[key] = value;  // Type assertion to 'any'
  }
  

}
