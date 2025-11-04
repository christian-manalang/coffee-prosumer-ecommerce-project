import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Order } from '../model/order';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private orderUrl = 'http://localhost:8080/api/order';

  constructor(private http: HttpClient) { }

  placeOrder(order: Order): Observable<Order> {
    return this.http.post<Order>(this.orderUrl, order);
  }
}

