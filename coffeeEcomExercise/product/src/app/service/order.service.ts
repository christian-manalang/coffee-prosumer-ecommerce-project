import { Injectable } from '@angular/core';
import { BaseHttpService } from './base-http.service';
import { HttpClient } from '@angular/common/http';
import { Order } from '../model/order';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrderService extends BaseHttpService {

  constructor(protected override http: HttpClient) { 
    super(http, '/api/orderitems'); 
  }

  public placeOrder(order: Order): Observable<any> {

    return this.add(order.orderItems);
  }
}
