export interface WishlistItem {
  id: number;
  productId: number;
  productName: string;
  productPrice: number;
  desiredQuantity: number;
  availableStock: number;
  outOfStock: boolean;
  stockStatus: string;
  addedAt: string;
}

export interface WishlistItemRequest {
  productId: number;
  desiredQuantity: number;
}

export interface WishlistHistory {
  id: number;
  productId: number;
  productNameSnapshot: string;
  productPriceSnapshot: number;
  action: string;
  actionAt: string;
}
