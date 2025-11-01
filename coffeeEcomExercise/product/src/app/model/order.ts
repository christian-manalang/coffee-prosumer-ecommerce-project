import { Customer } from "./customer";
import { OrderItem } from "./order-item";

export class Order {
    customer: Customer = new Customer();
    orderItems: OrderItem[] = [];

    totalPrice: number = 0;
    totalQuantity: number = 0;
}
