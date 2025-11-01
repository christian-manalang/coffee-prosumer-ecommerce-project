import { CartItem } from './cart-item';
import { Product } from './product';

describe('CartItem', () => {
  it('should create an instance and copy data from Product', () => {
    
    let product = new Product();
    product.id = 1;
    product.name = "Test Coffee";
    product.price = "12.99";
    product.imageFile = "test.jpg";
    product.description = "Test Description"; 
    product.categoryName = "Test Category";
    product.unitOfMeasure = "bag";

    let cartItem = new CartItem(product);

    expect(cartItem).toBeTruthy();
    expect(cartItem.id).toBe(1);
    expect(cartItem.name).toBe("Test Coffee");
    expect(cartItem.unitPrice).toBe(12.99); 
    expect(cartItem.quantity).toBe(1);
    expect(cartItem.imageFile).toBe("test.jpg");
    
    expect(cartItem.description).toBe("Test Description"); 
  });
});

