import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { CartItem } from '../model/cart-item';
import { Product } from '../model/product';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  // --- Reactive State ---
  // We use BehaviorSubject to hold the current list of items
  // and make it available as an observable to any component.
  private cartItemsSubject = new BehaviorSubject<CartItem[]>([]);
  public items$ = this.cartItemsSubject.asObservable(); // The '$' indicates an observable

  // We use BehaviorSubject for totals as well
  public totalPrice$ = new BehaviorSubject<number>(0);
  public totalQuantity$ = new BehaviorSubject<number>(0);

  constructor() {
    // Load cart from local storage on startup, if it exists
    const storedItems = localStorage.getItem('cartItems');
    if (storedItems) {
      const items: CartItem[] = JSON.parse(storedItems);
      this.cartItemsSubject.next(items);
      this.computeCartTotals(items);
    }
  }

  /**
   * Main logic to add a product to the cart.
   * If the item already exists, it increments the quantity.
   */
  addToCart(product: Product) {
    const currentItems = this.cartItemsSubject.getValue();
    let alreadyExists = false;
    
    // Check if item already in cart
    for (let item of currentItems) {
      if (item.id === product.id) {
        item.quantity++;
        alreadyExists = true;
        break;
      }
    }

    // If not, create a new CartItem and add it to the array
    if (!alreadyExists) {
      currentItems.push(new CartItem(product));
    }

    // Notify all subscribers (like the cart page) of the changes
    this.notifySubscribers(currentItems);
  }

  /**
   * Decrements the quantity of an item.
   */
  decrementQuantity(cartItem: CartItem) {
    const currentItems = this.cartItemsSubject.getValue();
    for (let item of currentItems) {
      if (item.id === cartItem.id && item.quantity > 1) {
        item.quantity--;
        this.notifySubscribers(currentItems);
        return; // Exit loop
      }
    }
  }

  /**
   * Removes an item from the cart entirely.
   */
  removeFromCart(cartItem: CartItem) {
    let currentItems = this.cartItemsSubject.getValue();
    currentItems = currentItems.filter(item => item.id !== cartItem.id);
    this.notifySubscribers(currentItems);
  }

  /**
   * Gets the current raw array of items (for checkout).
   */
  getCartItems(): CartItem[] {
    return this.cartItemsSubject.getValue();
  }

  /**
   * Empties the cart after a successful order.
   */
  clearCart() {
    this.notifySubscribers([]);
  }

  /**
   * Helper function to update all observables and local storage.
   */
  private notifySubscribers(items: CartItem[]) {
    // 1. Calculate new totals
    const { total, quantity } = this.computeCartTotals(items);

    // 2. Notify all subscribers
    this.cartItemsSubject.next(items);
    this.totalPrice$.next(total);
    this.totalQuantity$.next(quantity);
    
    // 3. Persist to local storage
    localStorage.setItem('cartItems', JSON.stringify(items));
  }

  /**
   * Calculates the total price and quantity.
   * @returns An object with total price and total quantity.
   */
  private computeCartTotals(items: CartItem[]): { total: number, quantity: number } {
    let total = 0;
    let quantity = 0;

    for (let item of items) {
      total += item.quantity * item.unitPrice;
      quantity += item.quantity;
    }
    return { total, quantity };
  }
}

