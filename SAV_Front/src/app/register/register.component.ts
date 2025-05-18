import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth-service.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  // Modèle de l'utilisateur pour l'inscription
  user = {
    username: '',
    email: '',
    login: '',
    password: '',
    confirmPassword: '',
    codeVerification: '',
    ville: '',
    codePostal: '',
    telephone: '',
    termsAccepted: false
  };

  codeSent: boolean = false;
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
      termsAccepted: [false, Validators.requiredTrue],
      codeVerification: [''] // Champ ajouté mais activé plus tard
    });
  }

  ngOnInit(): void {
    // Initialisation si nécessaire
  }

  // Méthode pour gérer la soumission du formulaire d'inscription
  /*onSubmit(): void {
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
  }*/

   async onSubmit(): Promise<void> {
    if (!this.codeSent) {
      await this.sendVerificationCode();
    } else {
      await this.validateAndRegister();
    }
  }

  private async sendVerificationCode(): Promise<void> {
    const email = this.registerForm.get('email')?.value;
    
    this.authService.sendVerificationCode(email).subscribe({
      next: async () => {
        this.codeSent = true;
        await this.showCodeVerificationPopup();
      },
      error: (err) => {
        Swal.fire('Erreur', err.error?.error || 'Échec de l\'envoi du code', 'error');
      }
    });
  }

  private async showCodeVerificationPopup(): Promise<void> {
    const result = await Swal.fire({
      title: 'Vérification du code',
      html: `<p>Un code a été envoyé à <strong>${this.registerForm.value.email}</strong></p>`,
      input: 'text',
      inputLabel: 'Code de vérification (6 chiffres)',
      inputPlaceholder: '123456',
      showCloseButton: true,
      showCancelButton: true,
      confirmButtonText: 'Valider',
      cancelButtonText: 'Annuler',
      allowOutsideClick: false,
      inputValidator: (value) => {
        if (!value) return 'Le code est requis';
        if (!/^\d{6}$/.test(value)) return 'Le code doit contenir 6 chiffres';
        return null;
      }
    });

    if (result.isConfirmed) {
      this.registerForm.patchValue({ codeVerification: result.value });
      console.log(result.value)
    } else {
      this.codeSent = false;
      Swal.fire('Information', 'Vous devez vérifier votre email pour continuer', 'info');
    }
  }

  private async validateAndRegister(): Promise<void> {
    const formValues = this.registerForm.value;
    
    this.authService.verifyCode(formValues.email, formValues.codeVerification).subscribe({
      next: (verificationResponse: any) => {
        if (verificationResponse.valid) {
          this.completeRegistration(formValues);
        } else {
          Swal.fire('Erreur', verificationResponse.message || 'Code incorrect', 'error');
          this.codeSent = false;
        }
      },
      error: () => {
        Swal.fire('Erreur', 'Erreur lors de la vérification', 'error');
      }
    });
  }

  private completeRegistration(userData: any): void {
    this.authService.register(userData).subscribe({
      next: () => {
        Swal.fire('Succès', 'Inscription réussie!', 'success')
          .then(() => this.router.navigate(['/login']));
      },
      error: (err) => {
        Swal.fire('Erreur', err.error?.error || 'Erreur lors de l\'inscription', 'error');
      }
    });
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
