import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { WishlistItem, WishlistItemRequest, WishlistHistory } from '../guards/models/wishlist';

@Injectable({
  providedIn: 'root'
})
export class WishlistService {
  private apiUrl = `${environment.apiUrl}/api/wishlist`;

  constructor(private http: HttpClient) {}

  getItems(): Observable<WishlistItem[]> {
    return this.http.get<WishlistItem[]>(this.apiUrl);
  }

  addItem(productId: number, desiredQuantity: number): Observable<WishlistItem> {
    return this.http.post<WishlistItem>(`${this.apiUrl}/items`, { productId, desiredQuantity });
  }

  updateItem(productId: number, desiredQuantity: number): Observable<WishlistItem> {
    return this.http.put<WishlistItem>(`${this.apiUrl}/items/${productId}`, { productId, desiredQuantity });
  }

  removeItem(productId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/items/${productId}`);
  }

  getHistory(): Observable<WishlistHistory[]> {
    return this.http.get<WishlistHistory[]>(`${this.apiUrl}/history`);
  }
}
