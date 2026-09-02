import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  fullName = '';
  email = '';
  password = '';
  confirmPassword = '';
  hidePassword = true;
  hideConfirm = true;
  loading = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  register(): void {
    if (!this.fullName || !this.email || !this.password) {
      this.snackBar.open('Complete todos los campos', 'Cerrar', { duration: 3000 });
      return;
    }
    if (this.password !== this.confirmPassword) {
      this.snackBar.open('Las contraseñas no coinciden', 'Cerrar', { duration: 3000 });
      return;
    }
    if (this.password.length < 6) {
      this.snackBar.open('La contraseña debe tener al menos 6 caracteres', 'Cerrar', { duration: 3000 });
      return;
    }
    this.loading = true;
    this.authService.register({ fullName: this.fullName, email: this.email, password: this.password }).subscribe({
      next: () => {
        this.snackBar.open('Cuenta creada exitosamente', 'Cerrar', { duration: 2000 });
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.loading = false;
        const msg = err.error?.message || 'Error al registrar';
        this.snackBar.open(msg, 'Cerrar', { duration: 4000 });
      }
    });
  }
}
