import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Product } from '../guards/models/product';

export interface ProductPage {
  content: Product[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}

export interface CatalogParams {
  page?: number;
  size?: number;
  sort?: string;
  category?: string;
  q?: string;
}

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = `${environment.apiUrl}/api/products`;

  constructor(private http: HttpClient) {}

  getCatalog(params?: CatalogParams): Observable<ProductPage> {
    let httpParams = new HttpParams()
      .set('page', String(params?.page ?? 0))
      .set('size', String(params?.size ?? 12));
    if (params?.sort) httpParams = httpParams.set('sort', params.sort);
    if (params?.category) httpParams = httpParams.set('category', params.category);
    if (params?.q) httpParams = httpParams.set('q', params.q);
    return this.http.get<ProductPage>(this.apiUrl, { params: httpParams });
  }

  getById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`);
  }

  uploadImage(id: number, file: File): Observable<Product> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<Product>(`${this.apiUrl}/${id}/image`, formData);
  }
}