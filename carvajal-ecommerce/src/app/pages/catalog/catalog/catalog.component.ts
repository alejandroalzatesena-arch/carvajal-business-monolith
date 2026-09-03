import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ProductService } from '../../../core/services/product.service';
import { WishlistService } from '../../../core/services/wishlist.service';
import { AuthService } from '../../../core/services/auth.service';
import { Product } from '../../../core/guards/models/product';

@Component({
  selector: 'app-catalog',
  templateUrl: './catalog.component.html',
  styleUrls: ['./catalog.component.scss']
})
export class CatalogComponent implements OnInit {
  search = '';
  products: Product[] = [];
  loading = true;

  constructor(
    private productService: ProductService,
    private wishlistService: WishlistService,
    private authService: AuthService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.productService.getCatalog().subscribe({
      next: (data) => {
        this.products = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.snackBar.open('Error al cargar productos', 'Cerrar', { duration: 3000 });
      }
    });
  }

  addToWishlist(product: Product): void {
    if (!this.authService.isAuthenticated()) {
      this.snackBar.open('Inicia sesión para agregar a tu lista', 'Cerrar', { duration: 3000 });
      return;
    }
    this.wishlistService.addItem(product.id, 1).subscribe({
      next: () => {
        this.snackBar.open(`${product.name} agregado a la lista`, 'Cerrar', { duration: 2000 });
      },
      error: (err) => {
        const msg = err.error?.message || 'Error al agregar';
        this.snackBar.open(msg, 'Cerrar', { duration: 3000 });
      }
    });
  }

  get filteredProducts(): Product[] {
    if (!this.search) return this.products;
    const term = this.search.toLowerCase();
    return this.products.filter(p =>
      p.name.toLowerCase().includes(term) ||
      p.category.toLowerCase().includes(term)
    );
  }

  getStockLabel(stock: number): string {
    if (stock === 0) return 'Sin stock';
    if (stock <= 5) return `Últimas ${stock} unidades`;
    return `${stock} disponibles`;
  }

  getStockColor(stock: number): string {
    if (stock === 0) return 'warn';
    if (stock <= 5) return 'accent';
    return 'primary';
  }
}
