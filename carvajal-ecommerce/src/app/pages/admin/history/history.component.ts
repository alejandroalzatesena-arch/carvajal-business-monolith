import { Component, OnInit } from '@angular/core';
import { WishlistService } from '../../../core/services/wishlist.service';
import { WishlistHistory } from '../../../core/guards/models/wishlist';

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.scss']
})
export class HistoryComponent implements OnInit {
  history: WishlistHistory[] = [];
  loading = true;

  constructor(private wishlistService: WishlistService) {}

  ngOnInit(): void {
    this.wishlistService.getHistory().subscribe({
      next: (data) => {
        this.history = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  getActionIcon(action: string): string {
    switch (action) {
      case 'ADD': return 'add_circle';
      case 'UPDATE': return 'edit';
      case 'REMOVE': return 'delete';
      default: return 'info';
    }
  }

  getActionColor(action: string): string {
    switch (action) {
      case 'ADD': return 'primary';
      case 'UPDATE': return 'accent';
      case 'REMOVE': return 'warn';
      default: return '';
    }
  }

  getActionLabel(action: string): string {
    switch (action) {
      case 'ADD': return 'Producto agregado';
      case 'UPDATE': return 'Producto actualizado';
      case 'REMOVE': return 'Producto eliminado';
      default: return action;
    }
  }
}
