import { Component } from '@angular/core';

@Component({
  selector: 'app-edit-product',
  templateUrl: './edit-product.component.html',
  styleUrls: ['./edit-product.component.scss']
})
export class EditProductComponent {

  product = {

    name: 'Logitech G502 Hero',

    brand: 'Logitech',

    category: 'Mouse',

    price: 219900,

    stock: 18,

    description:
      'Mouse gamer con sensor HERO 25K, 11 botones programables e iluminación RGB.',

    image: 'assets/images/mouse.jpg',

    status: 'Disponible'

  };

  categories = [

    'Teclados',

    'Mouse',

    'Auriculares',

    'Monitores',

    'Procesadores',

    'Tarjetas Gráficas',

    'Tarjetas Madre',

    'SSD',

    'Memorias RAM',

    'Fuentes de Poder',

    'Gabinetes',

    'Portátiles'

  ];

  updateProduct(){

    console.log('Producto actualizado');

    console.log(this.product);

  }

}