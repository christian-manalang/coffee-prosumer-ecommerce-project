import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CartService } from '../service/cart.service';
import { OrderService } from '../service/order.service';
import { Order } from '../model/order';
import { OrderItem } from '../model/order-item';
import { Customer } from '../model/customer';
import { Subscription } from 'rxjs';
import { CartItem } from '../model/cart-item';

@Component({
  selector: 'app-product-order',
  templateUrl: './product-order.component.html',
  styleUrls: ['./product-order.component.css']
})
export class ProductOrderComponent implements OnInit, OnDestroy {

  checkoutForm!: FormGroup;
  totalPrice: number = 0;
  totalQuantity: number = 0;
  isOrderPlaced: boolean = false;

  private cartSub: Subscription = new Subscription();

  constructor(
    private formBuilder: FormBuilder,
    private cartService: CartService,
    private orderService: OrderService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.checkoutForm = this.formBuilder.group({
      firstname: ['', [Validators.required, Validators.minLength(2)]],
      middlename: [''],
      lastname: ['', [Validators.required, Validators.minLength(2)]],
      dateOfBirth: ['', Validators.required],
      gender: ['', Validators.required]
    });

    this.cartSub.add(
      this.cartService.totalPrice$.subscribe((data: number) => this.totalPrice = data)
    );
    this.cartSub.add(
      this.cartService.totalQuantity$.subscribe((data: number) => this.totalQuantity = data)
    );

    if (this.cartService.getCartItems().length === 0) {
      this.router.navigate(['/product']);
    }
  }

  ngOnDestroy(): void {
    this.cartSub.unsubscribe();
  }

  get firstname() { return this.checkoutForm.get('firstname'); }
  get lastname() { return this.checkoutForm.get('lastname'); }
  get dateOfBirth() { return this.checkoutForm.get('dateOfBirth'); }
  get gender() { return this.checkoutForm.get('gender'); }

  onSubmit() {
    console.log("Submitting order...");

    if (this.checkoutForm.invalid) {
      this.checkoutForm.markAllAsTouched();
      console.warn("Form is invalid!");
      return;
    }

    const cartItems = this.cartService.getCartItems();
    
    let order = new Order();
    order.totalPrice = this.totalPrice;
    order.totalQuantity = this.totalQuantity;

    let customer = new Customer();
    customer.firstname = this.firstname?.value;
    customer.middlename = this.checkoutForm.get('middlename')?.value;
    customer.lastname = this.lastname?.value;
    customer.dateOfBirth = this.dateOfBirth?.value;
    customer.gender = this.gender?.value;
    
    order.customer = customer;

    order.items = cartItems.map((cartItem: CartItem) => {
      let item = new OrderItem();
      item.productId = cartItem.id;
      item.productName = cartItem.name;
      item.productDescription = cartItem.description;
      item.productCategoryName = cartItem.categoryName;
      item.productImageFile = cartItem.imageFile;
      item.productUnitOfMeasure = cartItem.unitOfMeasure;
      item.quantity = cartItem.quantity;
      item.price = cartItem.unitPrice;
      return item;
    });

    this.orderService.placeOrder(order).subscribe({
      next: (response: any) => {
        console.log("Order placed successfully!", response);
        this.isOrderPlaced = true;
        this.cartService.clearCart(); 
        
        setTimeout(() => {
          this.router.navigate(['/']);
        }, 3000);
      },
      error: (err: any) => {
        console.error("Error placing order:", err);
        alert("There was an error placing your order. Please try again.");
      }
    });
  }
}

