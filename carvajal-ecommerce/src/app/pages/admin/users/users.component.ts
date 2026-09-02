import { Component } from '@angular/core';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent {

  search = '';

  users = [

    {
      id: 1,
      name: 'Juan Ospina',
      email: 'juan@gmail.com',
      phone: '3001234567',
      city: 'Armenia',
      role: 'Administrador',
      status: 'Activo'
    },

    {
      id: 2,
      name: 'Vanessa Triviño',
      email: 'vanessa@gmail.com',
      phone: '3014567890',
      city: 'Calarca',
      role: 'Cliente',
      status: 'Activo'
    },

    {
      id: 3,
      name: 'Alejandro Alzate',
      email: 'alejandro@gmail.com',
      phone: '3109876543',
      city: 'Cali',
      role: 'Cliente',
      status: 'Inactivo'
    },

    {
      id: 4,
      name: 'Esteban Gomez',
      email: 'esteban@gmail.com',
      phone: '3206549871',
      city: 'Barranquilla',
      role: 'Cliente',
      status: 'Activo'
    }

  ];

}