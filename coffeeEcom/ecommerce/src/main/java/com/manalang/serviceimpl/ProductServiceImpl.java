package com.manalang.serviceimpl;

import com.manalang.entity.CategoryData;
import com.manalang.entity.ProductData;
import com.manalang.model.Product;
import com.manalang.model.ProductCategory;
import com.manalang.repository.CategoryDataRepository;
import com.manalang.repository.ProductDataRepository;
import com.manalang.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDataRepository productDataRepository;

    @Autowired
    private CategoryDataRepository categoryDataRepository; 

    private Product translateProductEntityToDTO(ProductData entity) {
        if (entity == null) {
            return null;
        }
        Product dto = new Product();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setImageFile(entity.getImageFile());
        dto.setUnitOfMeasure(entity.getUnitOfMeasure());

        if (entity.getPrice() != null) {
            dto.setPrice(entity.getPrice().toString());
        }

        if (entity.getCategory() != null) {
            dto.setCategoryName(entity.getCategory().getName());
        }
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCategory> listProductCategories() {
        log.info("Fetching all products, grouped by category.");
        
        Iterable<CategoryData> categoryEntities = categoryDataRepository.findAll();
        List<ProductCategory> productCategoryDTOs = new ArrayList<>();

        for (CategoryData categoryEntity : categoryEntities) {
            ProductCategory categoryDTO = new ProductCategory();
            categoryDTO.setCategoryName(categoryEntity.getName());

            List<Product> productDTOs = categoryEntity.getProducts().stream()
                    .map(this::translateProductEntityToDTO) 
                    .collect(Collectors.toList());
            
            categoryDTO.setProducts(productDTOs);
            productCategoryDTOs.add(categoryDTO);
        }
        return productCategoryDTOs;
    }

    @Override
    @Transactional(readOnly = true)
    public Product get(Integer id) {
        log.info("Fetching product by id: {}", id);
        return productDataRepository.findById(id)
                .map(this::translateProductEntityToDTO) 
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Override
    @Transactional
    public Product create(Product productDTO) {
        log.info("Creating new product: {}", productDTO.getName());
        
        ProductData entity = new ProductData();
        
        CategoryData category = categoryDataRepository.findByName(productDTO.getCategoryName())
                .orElseThrow(() -> new RuntimeException("Category not found: " + productDTO.getCategoryName()));
        
        entity.setCategory(category);
        entity.setName(productDTO.getName());
        entity.setDescription(productDTO.getDescription());
        entity.setImageFile(productDTO.getImageFile());
        entity.setUnitOfMeasure(productDTO.getUnitOfMeasure());
        
        if (productDTO.getPrice() != null && !productDTO.getPrice().isEmpty()) {
            entity.setPrice(new BigDecimal(productDTO.getPrice()));
        }

        ProductData savedEntity = productDataRepository.save(entity);
        
        return translateProductEntityToDTO(savedEntity);
    }

    @Override
    @Transactional
    public Product update(Product productDTO) {
        log.info("Updating product id: {}", productDTO.getId());
        
        ProductData entity = productDataRepository.findById(productDTO.getId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productDTO.getId()));
        
        CategoryData category = categoryDataRepository.findByName(productDTO.getCategoryName())
                .orElseThrow(() -> new RuntimeException("Category not found: " + productDTO.getCategoryName()));

        entity.setCategory(category);
        entity.setName(productDTO.getName());
        entity.setDescription(productDTO.getDescription());
        entity.setImageFile(productDTO.getImageFile());
        entity.setUnitOfMeasure(productDTO.getUnitOfMeasure());
        if (productDTO.getPrice() != null && !productDTO.getPrice().isEmpty()) {
            entity.setPrice(new BigDecimal(productDTO.getPrice()));
        }
        
        ProductData savedEntity = productDataRepository.save(entity);
        return translateProductEntityToDTO(savedEntity);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Deleting product id: {}", id);
        if (productDataRepository.existsById(id)) {
            productDataRepository.deleteById(id);
        } else {
            log.warn("Could not delete. Product not found with id: {}", id);
        }
    }
}

