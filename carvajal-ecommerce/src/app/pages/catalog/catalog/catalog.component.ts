import { Component } from '@angular/core';

@Component({
  selector: 'app-catalog',
  templateUrl: './catalog.component.html',
  styleUrls: ['./catalog.component.scss']
})
export class CatalogComponent {

  search = '';

  products = [

  {
    name:'Teclado Mecánico Redragon Kumara',
    price:'$259.900',
    category:'Teclados',
    image:'assets/images/keyboard.jpg'
  },

  {
    name:'Mouse Logitech G502 Hero',
    price:'$219.900',
    category:'Mouse',
    image:'assets/images/mouse.jpg'
  },

  {
    name:'Monitor LG UltraGear 27"',
    price:'$1.399.900',
    category:'Monitores',
    image:'assets/images/monitor.jpg'
  },

  {
    name:'HyperX Cloud II',
    price:'$449.900',
    category:'Auriculares',
    image:'assets/images/headset.jpg'
  },

  {
    name:'SSD Samsung 990 PRO 1TB',
    price:'$499.900',
    category:'Almacenamiento',
    image:'assets/images/ssd.jpg'
  },

  {
    name:'Procesador Intel Core i7 14700K',
    price:'$1.899.900',
    category:'Procesadores',
    image:'assets/images/i7.jpg'
  },

  {
    name:'RTX 4070 Ti ASUS TUF',
    price:'$4.599.900',
    category:'Tarjetas Gráficas',
    image:'assets/images/rtx4070.jpg'
  },

  {
    name:'Motherboard ASUS ROG B760',
    price:'$1.099.900',
    category:'Tarjetas Madre',
    image:'assets/images/motherboard.jpg'
  }

];

}