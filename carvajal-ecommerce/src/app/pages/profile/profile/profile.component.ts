import { Component } from '@angular/core';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent {

  user = {

    name:'Juan',

    lastname:'Ospina',

    email:'juan@gmail.com',

    phone:'3001234567',

    city:'Armenia',

    address:'Calle 06 #20-30',

    role:'Cliente',

    image:'assets/images/user.png'

  };

  save(){

    console.log('Perfil actualizado');

    console.log(this.user);

  }

}