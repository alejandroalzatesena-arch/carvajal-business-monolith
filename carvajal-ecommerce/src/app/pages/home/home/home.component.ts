import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {

  categories = [

    {
      icon: 'computer',
      title: 'PC Gamer'
    },

    {
      icon: 'keyboard',
      title: 'Teclados'
    },

    {
      icon: 'mouse',
      title: 'Mouse'
    },

    {
      icon: 'headphones',
      title: 'Auriculares'
    },

    {
      icon: 'memory',
      title: 'Procesadores'
    },

    {
      icon: 'developer_board',
      title: 'Tarjetas Madre'
    },

    {
      icon: 'storage',
      title: 'SSD'
    },

    {
      icon: 'videogame_asset',
      title: 'Accesorios'

    }

  ];

  featuredProducts = [

    {
      name: 'Teclado Mecánico Redragon Kumara',
      price: '$259.900',
      image: 'assets/images/keyboard.jpg'
    },

    {
      name: 'Mouse Logitech G502 Hero',
      price: '$219.900',
      image: 'assets/images/mouse.jpg'
    },

    {
      name: 'Monitor Gamer LG UltraGear 27"',
      price: '$1.399.900',
      image: 'assets/images/monitor.jpg'
    },

    {
      name: 'Audífonos HyperX Cloud II',
      price: '$449.900',
      image: 'assets/images/headset.jpg'
    }

  ];

}