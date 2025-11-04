export class OrderItem {
  id: number = 0;

  productId: number = 0;
  productName: string = '';
  productDescription: string = '';
  productCategoryName: string = '';
  productImageFile: string = '';
  productUnitOfMeasure: string = '';
  price: number = 0;
  quantity: number = 0;

  orderId?: number;
  customerId?: number;
  customerName?: string;
  status?: string; 
  created?: Date;
  lastUpdated?: Date;
}

