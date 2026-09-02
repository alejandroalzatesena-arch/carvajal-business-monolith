import { Component } from '@angular/core';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent {

  search = '';

  displayedColumns: string[] = [
    'image',
    'name',
    'category',
    'price',
    'stock',
    'brand',
    'actions'
  ];

  products = [

    {
      image:'assets/images/keyboard.jpg',
      name:'Redragon Kumara K552',
      category:'Teclados',
      brand:'Redragon',
      stock:18,
      price:'$259.900'
    },

    {
      image:'assets/images/mouse.jpg',
      name:'Logitech G502 Hero',
      category:'Mouse',
      brand:'Logitech',
      stock:22,
      price:'$219.900'
    },

    {
      image:'assets/images/headset.jpg',
      name:'HyperX Cloud II',
      category:'Auriculares',
      brand:'HyperX',
      stock:15,
      price:'$449.900'
    },

    {
      image:'assets/images/monitor.jpg',
      name:'LG UltraGear 27"',
      category:'Monitores',
      brand:'LG',
      stock:9,
      price:'$1.399.900'
    },

    {
      image:'assets/images/ssd.jpg',
      name:'Samsung 990 PRO 1TB',
      category:'SSD',
      brand:'Samsung',
      stock:14,
      price:'$499.900'
    }

  ];

}