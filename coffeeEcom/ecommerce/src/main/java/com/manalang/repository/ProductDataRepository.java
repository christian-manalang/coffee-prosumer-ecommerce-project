package com.manalang.repository;

import com.manalang.entity.CategoryData;
import com.manalang.entity.ProductData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDataRepository extends CrudRepository<ProductData, Integer> {

    List<ProductData> findByCategory(CategoryData category);

    List<ProductData> findByCategoryName(String categoryName);
}
