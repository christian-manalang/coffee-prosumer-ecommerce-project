package com.manalang.service;

import com.manalang.model.Product;
import com.manalang.model.ProductCategory;
import java.util.List;

public interface ProductService {

    List<ProductCategory> listProductCategories();

    Product get(Integer id);

    Product create(Product product);

    Product update(Product product);

    void delete(Integer id);

}

