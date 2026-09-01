import { Component } from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {

  cartItems = 2;

  wishlistItems = 3;

  search = '';

  menu = [

    {
      name:'Inicio',
      route:'/'
    },

    {
      name:'Catálogo',
      route:'/catalog'
    },

    {
      name:'PC Gamer',
      route:'/catalog'
    },

    {
      name:'Teclados',
      route:'/catalog'
    },

    {
      name:'Mouse',
      route:'/catalog'
    },

    {
      name:'Componentes',
      route:'/catalog'
    }

  ];

}