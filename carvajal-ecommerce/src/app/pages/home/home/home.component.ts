import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../../core/services/product.service';
import { Product } from '../../../core/guards/models/product';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  categories = [
    { icon: 'computer', title: 'PC Gamer' },
    { icon: 'keyboard', title: 'Teclados' },
    { icon: 'mouse', title: 'Mouse' },
    { icon: 'headphones', title: 'Auriculares' },
    { icon: 'memory', title: 'Procesadores' },
    { icon: 'developer_board', title: 'Tarjetas Madre' },
    { icon: 'storage', title: 'SSD' },
    { icon: 'videogame_asset', title: 'Accesorios' }
  ];

  featuredProducts: Product[] = [];

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.productService.getCatalog().subscribe({
      next: (products) => {
        this.featuredProducts = products.slice(0, 4);
      }
    });
  }
}
