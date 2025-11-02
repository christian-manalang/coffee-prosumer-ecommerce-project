package com.manalang.controller;

import com.manalang.model.Product;
import com.manalang.model.ProductCategory;
import com.manalang.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/api/product")
    public ResponseEntity<?> getProductCategories() {
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<?> response;
        try {
            List<ProductCategory> mappedProducts = productService.listProductCategories();
            log.info("Returning {} product categories.", mappedProducts.size());
            response = ResponseEntity.ok(mappedProducts);
        } catch (Exception ex) {
            log.error("Failed to retrieve product categories: {}", ex.getMessage(), ex);
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return response;
    }

    @PostMapping("/api/product") 
    public ResponseEntity<?> add(@RequestBody Product product) {
        log.info("Input to CREATE product >> " + product.toString());
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<?> response;
        try {
            Product newProduct = productService.create(product);
            log.info("Created product >> " + newProduct.toString());
            response = ResponseEntity.ok(newProduct);
        } catch (Exception ex) {
            log.error("Failed to create product: {}", ex.getMessage(), ex);
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return response;
    }

    @PutMapping("/api/product") 
    public ResponseEntity<?> update(@RequestBody Product product) {
        log.info("Input to UPDATE product >> " + product.toString());
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<?> response;
        try {
            Product updatedProduct = productService.update(product);
            log.info("Updated product >> " + updatedProduct.toString());
            response = ResponseEntity.ok(updatedProduct); 
        } catch (Exception ex) {
            log.error("Failed to update product: {}", ex.getMessage(), ex);
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return response;
    }

    @GetMapping("/api/product/{id}") 
    public ResponseEntity<?> get(@PathVariable final Integer id) {
        log.info("Input product id >> " + Integer.toString(id));
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<?> response;
        try {
            Product product = productService.get(id);
            response = ResponseEntity.ok(product);
        } catch (Exception ex) {
            log.error("Failed to get product {}: {}", id, ex.getMessage(), ex);
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return response;
    }

    @DeleteMapping("/api/product/{id}")
    public ResponseEntity<?> delete(@PathVariable final Integer id) {
        log.info("Input to DELETE product id >> " + Integer.toString(id));
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<?> response;
        try {
            productService.delete(id);
            response = ResponseEntity.ok(null); 
        } catch (Exception ex) {
            log.error("Failed to delete product {}: {}", id, ex.getMessage(), ex);
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return response;
    }
}
