import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import Swal from 'sweetalert2';
import { AuthService } from '../services/auth-service.service';


@Component({
  selector: 'app-restpassowrd',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule,RouterLink],
  templateUrl: './restpassowrd.component.html',
  styleUrl: './restpassowrd.component.css'
})
export class RestpassowrdComponent {
resetForm: FormGroup;
  codeSent = false;
  emailVerified = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.resetForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      code: [''],
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required]
    });
  }

  requestResetCode() {
    const email = this.resetForm.get('email')?.value;
    this.authService.requestPasswordReset(email).subscribe({
      next: () => {
        this.codeSent = true;
        Swal.fire('Succès', 'Code envoyé à votre email', 'success');
      },
      error: (err: { error: { error: any; }; }) => {
        Swal.fire('Erreur', err.error?.error || 'Échec de l\'envoi du code', 'error');
      }
    });
  }

  verifyCode() {
    const { email, code } = this.resetForm.value;
    this.authService.verifyCode(email, code).subscribe({
      next: (res: any) => {
        if (res.valid) {
          this.emailVerified = true;
          Swal.fire('Succès', 'Code vérifié', 'success');
        } else {
          Swal.fire('Erreur', res.message || 'Code invalide', 'error');
        }
      },
      error: () => {
        Swal.fire('Erreur', 'Erreur lors de la vérification', 'error');
      }
    });
  }

  resetPassword() {
  const { email, code, newPassword, confirmPassword } = this.resetForm.value;
  
  // Debug: Afficher les données avant envoi
  console.log('Données envoyées:', { 
    email, 
    code, 
    newPassword, 
    confirmPassword 
  });

  if (newPassword !== confirmPassword) {
    Swal.fire('Erreur', 'Les mots de passe ne correspondent pas', 'error');
    return;
  }

  this.authService.resetPassword(email, code, newPassword, confirmPassword).subscribe({
    next: () => {
      Swal.fire('Succès', 'Mot de passe réinitialisé avec succès', 'success')
        .then(() => this.router.navigate(['/login']));
    },
    error: (err) => {
      console.error('Détails de l\'erreur:', err);
      const errorMsg = err.error?.error || err.message || 'Échec de la réinitialisation';
      Swal.fire('Erreur', errorMsg, 'error');
      
      if (errorMsg.includes('Code invalide')) {
        this.codeSent = true; // Permet de resaisir le code
      }
    }
  });
}
}