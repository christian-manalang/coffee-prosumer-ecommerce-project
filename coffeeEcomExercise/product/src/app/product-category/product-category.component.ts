import { Component, OnInit } from '@angular/core';
import { ProductCategory } from '../model/product-category';
import { ProductService } from '../service/product.service';
import { Product } from '../model/product';
import { CartService } from '../service/cart.service';

@Component({
  selector: 'app-product-category',
  templateUrl: './product-category.component.html',
  styleUrls: ['./product-category.component.css']
})
export class ProductCategoryComponent implements OnInit {

  public allProductsCategory: ProductCategory[] = []; 
  public searchTerm: string = '';                   
  public selectedCategory: string = '';             

  public categoriesForFilter: ProductCategory[] = [];

  constructor(
    private productService: ProductService,
    private cartService: CartService 
  ) {}

  ngOnInit(): void {
    console.log("ngOnInit called");
    this.productService.getData().subscribe(data => {
      this.allProductsCategory = data;
      this.categoriesForFilter = [...data];
      console.log("Data Received:", this.allProductsCategory);
    });
  }

  addToCart(product: Product) {
    console.log(`Adding to cart: ${product.name}, ${product.price}`);
    this.cartService.addToCart(product);
  }

  get filteredProductsCategory(): ProductCategory[] {
    if (!this.allProductsCategory) {
      return []; 
    }

    let filteredList = this.allProductsCategory;

    if (this.selectedCategory) {
      filteredList = filteredList.filter(category =>
        category.categoryName === this.selectedCategory
      );
    }

    if (this.searchTerm) {
      const lowerSearchTerm = this.searchTerm.toLowerCase().trim();
      if (lowerSearchTerm === '') {
         return filteredList;
      }
      
      filteredList = filteredList.map(category => {
        const filteredProducts = category.products.filter(product =>
          product.name.toLowerCase().includes(lowerSearchTerm) ||
          product.description.toLowerCase().includes(lowerSearchTerm)
        );

        if (filteredProducts.length > 0) {
           return { categoryName: category.categoryName, products: filteredProducts };
        }
        return null; 

      }).filter(category => category !== null) as ProductCategory[]; 
    }

    return filteredList;
  }
}

