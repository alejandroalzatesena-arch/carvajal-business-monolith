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

  page = 0;
  pageSize = 12;
  totalElements = 0;
  totalPages = 0;
  first = true;
  last = false;

  constructor(
    private productService: ProductService,
    private wishlistService: WishlistService,
    private authService: AuthService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(page: number = this.page): void {
    this.loading = true;
    this.productService.getCatalog({
      page,
      size: this.pageSize,
      q: this.search?.trim() || undefined
    }).subscribe({
      next: (data) => {
        this.products = data.content;
        this.page = data.page;
        this.pageSize = data.size;
        this.totalElements = data.totalElements;
        this.totalPages = data.totalPages;
        this.first = data.first;
        this.last = data.last;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.snackBar.open('Error al cargar productos', 'Cerrar', { duration: 3000 });
      }
    });
  }

  onSearch(): void {
    this.loadProducts(0);
  }

  clearSearch(): void {
    this.search = '';
    this.loadProducts(0);
  }

  goToPage(page: number): void {
    if (page < 0 || page >= this.totalPages || page === this.page) return;
    this.loadProducts(page);
  }

  pages(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i);
  }

  get rangeLabel(): string {
    if (this.totalElements === 0) return '0 productos';
    const from = this.page * this.pageSize + 1;
    const to = this.last ? this.totalElements : (this.page + 1) * this.pageSize;
    return `${from}-${to} de ${this.totalElements} productos`;
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

  onImageError(event: Event): void {
    (event.target as HTMLImageElement).src = 'assets/images/no-image.png';
  }
}