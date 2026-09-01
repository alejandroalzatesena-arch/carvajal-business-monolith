import { Component } from '@angular/core';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {

  user = {

    name: '',

    lastname: '',

    email: '',

    phone: '',

    address: '',

    password: '',

    confirmPassword: ''

  };

  hidePassword = true;

  hideConfirm = true;

  register() {

    console.log(this.user);

  }

}