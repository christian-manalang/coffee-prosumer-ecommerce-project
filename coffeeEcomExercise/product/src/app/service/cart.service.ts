import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { CartItem } from '../model/cart-item';
import { Product } from '../model/product';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  cartItems: CartItem[] = [];
  
  totalPrice: Subject<number> = new BehaviorSubject<number>(0);
  totalQuantity: Subject<number> = new BehaviorSubject<number>(0);

  constructor() { }

  addToCart(product: Product) {
    console.log(`Adding to cart: ${product.name}`);
    
    let alreadyInCart: boolean = false;
    let existingCartItem: CartItem | undefined = undefined;

    if (this.cartItems.length > 0) {
      existingCartItem = this.cartItems.find(item => item.id === product.id); 
      alreadyInCart = (existingCartItem !== undefined);
    }

    if (alreadyInCart) {
      existingCartItem!.quantity++;
    } else {
      this.cartItems.push(new CartItem(product));
    }

    this.computeCartTotals();
  }

  computeCartTotals() {
    let totalPriceValue: number = 0;
    let totalQuantityValue: number = 0;

    for (let currentItem of this.cartItems) {
      totalPriceValue += currentItem.quantity * currentItem.unitPrice;
      totalQuantityValue += currentItem.quantity;
    }

    this.totalPrice.next(totalPriceValue);
    this.totalQuantity.next(totalQuantityValue);
  }

  getItems(): CartItem[] {
    return this.cartItems;
  }

  removeItem(cartItem: CartItem) {
    const itemIndex = this.cartItems.findIndex(item => item.id === cartItem.id);

    if (itemIndex > -1) {
      this.cartItems.splice(itemIndex, 1);
      this.computeCartTotals();
    }
  }

  decrementQuantity(cartItem: CartItem) {
    cartItem.quantity--;

    if (cartItem.quantity === 0) {
      this.removeItem(cartItem);
    } else {
      this.computeCartTotals();
    }
  }

  incrementQuantity(cartItem: CartItem) {
    cartItem.quantity++;
    this.computeCartTotals();
  }

  clearCart() {
    this.cartItems = [];
    this.computeCartTotals();
  }
}

