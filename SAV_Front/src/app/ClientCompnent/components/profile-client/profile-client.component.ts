import { Component } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

interface User {
  name: string;
  job: string;
  twitter: string;
  facebook: string;
  instagram: string;
  linkedin: string;
  about: string;
  email: string;
  phone: string;
  [key: string]: any;
}

@Component({
  selector: 'app-profile-client',
  standalone: true,
  imports: [HeaderComponent, CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './profile-client.component.html',
  styleUrls: ['./profile-client.component.css']
})
export class ProfileClientComponent {
  user: User = {
    name: 'Kevin Anderson',
    job: 'Web Designer',
    twitter: 'https://twitter.com/#',
    facebook: 'https://facebook.com/#',
    instagram: 'https://instagram.com/#',
    linkedin: 'https://linkedin.com/#',
    about: 'Sunt est soluta temporibus accusantium...',
    email: 'k.anderson@example.com',
    phone: '(436) 486-3538 x29071',
  };

  editableFields = [
    { id: 'name', label: 'Nom' },
    { id: 'job', label: 'Métier' },
    { id: 'about', label: 'À propos de vous' },
    { id: 'email', label: 'Email' },
    { id: 'phone', label: 'Téléphone' },
  ];

  notifications = [
    { name: 'changesMade', label: 'Modifications de votre compte', checked: true },
    { name: 'newProducts', label: 'Informations sur les nouveaux produits', checked: true },
    { name: 'proOffers', label: 'Offres marketing et promotions', checked: false },
    { name: 'securityNotify', label: 'Alertes de sécurité', checked: true }
  ];

  saveChanges() {
    console.log('Modifications sauvegardées:', this.user);
  }

  saveSettings() {
    console.log('Paramètres sauvegardés:', this.notifications);
  }

  updateUserProperty(key: keyof User, value: string) {
    (this.user as any)[key] = value;  // Mise à jour dynamique de la propriété
  }
}
