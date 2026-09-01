import { Component } from '@angular/core';

@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.scss']
})
export class WishlistComponent {

  wishlist = [

    {
      name: 'RTX 4070 Ti ASUS TUF',
      price: '$4.599.900',
      image: 'assets/images/rtx4070.jpg'
    },

    {
      name: 'Mouse Logitech G502 Hero',
      price: '$219.900',
      image: 'assets/images/mouse.jpg'
    },

    {
      name: 'Teclado Mecánico Redragon Kumara',
      price: '$259.900',
      image: 'assets/images/keyboard.jpg'
    }

  ];

  remove(index: number) {

    this.wishlist.splice(index, 1);

  }

}