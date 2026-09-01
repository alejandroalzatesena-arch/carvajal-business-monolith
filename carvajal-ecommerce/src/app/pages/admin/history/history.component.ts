import { Component } from '@angular/core';

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.scss']
})
export class HistoryComponent {

  history = [

    {
      action:'Producto agregado',
      description:'Se agregó el teclado mecánico Redragon Kumara.',
      user:'Administrador',
      date:'08/08/2026 10:20 AM',
      icon:'add_circle',
      color:'primary'
    },

    {
      action:'Producto actualizado',
      description:'Se actualizó el precio del Mouse Logitech G502.',
      user:'Administrador',
      date:'08/08/2026 11:45 AM',
      icon:'edit',
      color:'accent'
    },

    {
      action:'Producto eliminado',
      description:'Se eliminó un monitor LG UltraGear.',
      user:'Administrador',
      date:'08/08/2026 01:30 PM',
      icon:'delete',
      color:'warn'
    },

    {
      action:'Nuevo usuario',
      description:'Carlos Ramírez creó una cuenta.',
      user:'Sistema',
      date:'08/10/2026 03:10 PM',
      icon:'person_add',
      color:'primary'
    },

    {
      action:'Inicio de sesión',
      description:'El administrador inició sesión.',
      user:'Administrador',
      date:'08/10/2026 05:20 PM',
      icon:'login',
      color:'primary'
    }

  ];

}