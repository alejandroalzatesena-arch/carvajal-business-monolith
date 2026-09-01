import { Component } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  email = '';

  password = '';

  hidePassword = true;

  login() {

    console.log('Email:', this.email);
    console.log('Password:', this.password);

    // Aquí luego conectaremos con Spring Boot

  }

}