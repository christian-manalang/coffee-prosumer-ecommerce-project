import { Product } from "./product";

export class CartItem {
  id: number;
  name: string;
  imageFile: string;
  unitPrice: number;
  quantity: number;
  description: string;
  categoryName: string;
  unitOfMeasure: string;

  constructor(product: Product) {
    this.id = product.id;
    this.name = product.name;
    this.imageFile = product.imageFile;
    this.unitPrice = parseFloat(product.price); 
    this.quantity = 1;
    this.description = product.description;
    this.categoryName = product.categoryName;
    this.unitOfMeasure = product.unitOfMeasure;
  }
}

