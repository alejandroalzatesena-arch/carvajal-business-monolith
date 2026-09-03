import { Component } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {

  stats = [

    {
      title: 'Productos',
      value: 250,
      icon: 'inventory_2',
      color: '#1976D2'
    },

    {
      title: 'Usuarios',
      value: 1250,
      icon: 'people',
      color: '#43A047'
    },

    {
      title: 'Ventas',
      value: 985,
      icon: 'shopping_cart',
      color: '#FB8C00'
    },

    {
      title: 'Lista de deseos',
      value: 530,
      icon: 'favorite',
      color: '#E53935'
    }

  ];

}