import { Component } from '@angular/core';

@Component({
  selector: 'app-create-product',
  templateUrl: './create-product.component.html',
  styleUrls: ['./create-product.component.scss']
})
export class CreateProductComponent {

  product = {

    name: '',
    brand: '',
    category: '',
    price: 0,
    stock: 0,
    description: '',
    image: 'assets/images/no-image.png',
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

  saveProduct(){

    console.log(this.product);

  }

}