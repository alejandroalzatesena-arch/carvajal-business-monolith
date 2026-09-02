import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { WishlistService } from '../../../core/services/wishlist.service';
import { WishlistItem } from '../../../core/guards/models/wishlist';

@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.scss']
})
export class WishlistComponent implements OnInit {
  items: WishlistItem[] = [];
  loading = true;

  constructor(
    private wishlistService: WishlistService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadItems();
  }

  loadItems(): void {
    this.wishlistService.getItems().subscribe({
      next: (data) => {
        this.items = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.snackBar.open('Error al cargar la lista', 'Cerrar', { duration: 3000 });
      }
    });
  }

  updateQuantity(item: WishlistItem, newQty: number): void {
    if (newQty < 1) return;
    this.wishlistService.updateItem(item.productId, newQty).subscribe({
      next: () => {
        this.snackBar.open('Cantidad actualizada', 'Cerrar', { duration: 2000 });
        this.loadItems();
      },
      error: (err) => {
        this.snackBar.open(err.error?.message || 'Error al actualizar', 'Cerrar', { duration: 3000 });
      }
    });
  }

  removeItem(item: WishlistItem): void {
    this.wishlistService.removeItem(item.productId).subscribe({
      next: () => {
        this.snackBar.open(`${item.productName} eliminado de la lista`, 'Cerrar', { duration: 2000 });
        this.loadItems();
      },
      error: () => {
        this.snackBar.open('Error al eliminar', 'Cerrar', { duration: 3000 });
      }
    });
  }

  isOutOfStock(item: WishlistItem): boolean {
    return item.outOfStock;
  }

  isInsufficientStock(item: WishlistItem): boolean {
    return !item.outOfStock && item.stockStatus === 'Stock insuficiente para la cantidad deseada';
  }
}
