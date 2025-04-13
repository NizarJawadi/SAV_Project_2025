import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth-service.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule , RouterLink, ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  // Modèle de l'utilisateur pour l'inscription
  user = {
    username: '',
    email: '',
    login: '' ,
    password: '',
    confirmPassword: '',
    ville: '',
    codePostal: '',
    telephone: '',
    termsAccepted: false
  };

  registerForm: FormGroup;

  constructor(private router: Router,
              private authService: AuthService,
              private fb: FormBuilder
  ) {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      login: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required],
      ville: ['', Validators.required],
      codePostal: ['', Validators.required],
      telephone: ['', [Validators.required, Validators.pattern("^[0-9]*$")]], // Validation pour numéro de téléphone
      termsAccepted: [false, Validators.requiredTrue]
    });
  }

  ngOnInit(): void {
    // Initialisation si nécessaire
  }

  // Méthode pour gérer la soumission du formulaire d'inscription
  onSubmit(): void {
    if (this.registerForm.valid) {
      const user = this.registerForm.value;
      this.authService.register(user).subscribe(
        (response) => {
          console.log('Utilisateur inscrit avec succès', response);
          this.router.navigate(['/login']);
        },
        (error) => {
          console.error('Erreur lors de l\'inscription', error);
          alert('Erreur lors de l\'inscription');
        }
      );
    } else {
      alert('Veuillez remplir correctement tous les champs');
    }
  }

  // Validation des champs du formulaire
  validateForm(): boolean {
    if (!this.user.username || !this.user.email || !this.user.password || !this.user.confirmPassword) {
      alert('Tous les champs doivent être remplis.');
      return false;
    }

    if (this.user.password !== this.user.confirmPassword) {
      alert('Les mots de passe ne correspondent pas.');
      return false;
    }

    return true;
  }

  togglePassword(event: Event, field: string): void {
    const passwordField = document.querySelector(`input[name=${field}]`) as HTMLInputElement;
    const icon = event.target as HTMLElement;
    
    if (passwordField.type === 'password') {
      passwordField.type = 'text';
      icon.classList.replace('fa-eye', 'fa-eye-slash');
    } else {
      passwordField.type = 'password';
      icon.classList.replace('fa-eye-slash', 'fa-eye');
    }
  }
}
