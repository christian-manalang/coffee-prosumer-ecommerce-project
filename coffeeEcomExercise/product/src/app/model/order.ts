import { Customer } from "./customer";
import { OrderItem } from "./order-item";

export class Order {
  id: number = 0;
  customer: Customer = new Customer();
  items: OrderItem[] = []; 
  totalPrice: number = 0;
  totalQuantity: number = 0;
}

