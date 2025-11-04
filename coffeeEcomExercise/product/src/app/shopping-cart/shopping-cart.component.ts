import { Component, OnInit, OnDestroy } from '@angular/core';
import { CartService } from '../service/cart.service';
import { CartItem } from '../model/cart-item';
import { Observable, Subscription } from 'rxjs';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css']
})
export class ShoppingCartComponent implements OnInit, OnDestroy {

  cartItems$!: Observable<CartItem[]>;
  totalPrice$!: Observable<number>;
  totalQuantity$!: Observable<number>;

  private cartSub: Subscription = new Subscription();

  constructor(private cartService: CartService) { }

  ngOnInit(): void {
    this.cartItems$ = this.cartService.items$;
    this.totalPrice$ = this.cartService.totalPrice$;
    this.totalQuantity$ = this.cartService.totalQuantity$;
  }

  ngOnDestroy(): void {
    if (this.cartSub) {
      this.cartSub.unsubscribe();
    }
  }

  incrementQuantity(cartItem: CartItem) {
    this.cartService.addToCart(cartItem as any);
  }

  decrementQuantity(cartItem: CartItem) {
    this.cartService.decrementQuantity(cartItem);
  }

  remove(cartItem: CartItem) {
    this.cartService.removeFromCart(cartItem);
  }
}

