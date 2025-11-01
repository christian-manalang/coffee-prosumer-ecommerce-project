import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CartService } from '../service/cart.service';
import { OrderService } from '../service/order.service';
import { Order } from '../model/order';
import { OrderItem } from '../model/order-item';

@Component({
  selector: 'app-product-order',
  templateUrl: './product-order.component.html',
  styleUrls: ['./product-order.component.css']
})
export class ProductOrderComponent implements OnInit {

  checkoutForm!: FormGroup;
  totalPrice: number = 0;
  totalQuantity: number = 0;
  isOrderPlaced: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private cartService: CartService,
    private orderService: OrderService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.checkoutForm = this.formBuilder.group({
      fullName: ['', [Validators.required, Validators.minLength(2)]]
    });

    this.reviewCartDetails();
  }

  reviewCartDetails() {
    this.cartService.totalPrice.subscribe(data => this.totalPrice = data);
    this.cartService.totalQuantity.subscribe(data => this.totalQuantity = data);
    this.cartService.computeCartTotals();
  }

  get fullName() { return this.checkoutForm.get('fullName'); }

  onSubmit() {
    console.log("Submitting order...");

    if (this.checkoutForm.invalid) {
      this.checkoutForm.markAllAsTouched();
      console.warn("Form is invalid!");
      return;
    }

    const customerNameFromForm = this.checkoutForm.get('fullName')?.value;
    const cartItems = this.cartService.getItems();

    let order = new Order();
    order.totalPrice = this.totalPrice;
    order.totalQuantity = this.totalQuantity;

    order.orderItems = cartItems.map(cartItem => {
      let item = new OrderItem();

      item.productId = cartItem.id;
      item.productName = cartItem.name;
      item.productDescription = cartItem.description; 
      item.productCategoryName = cartItem.categoryName; 
      item.productImageFile = cartItem.imageFile;
      item.productUnitOfMeasure = cartItem.unitOfMeasure; 
      item.quantity = cartItem.quantity;
      item.price = cartItem.unitPrice;

      item.customerName = customerNameFromForm;
      item.customerId = 0; 

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

