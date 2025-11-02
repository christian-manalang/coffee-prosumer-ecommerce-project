package com.manalang.serviceimpl;

import com.manalang.entity.CategoryData;
import com.manalang.model.Category;
import com.manalang.repository.CategoryDataRepository;
import com.manalang.service.CategoryService;
import com.manalang.util.Transform; 
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service 
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDataRepository categoryDataRepository;

    private Transform<CategoryData, Category> transform = new Transform<>(Category.class);

    @Override
    public List<Category> getAllCategories() {
        log.info("Fetching all categories from database.");
        List<Category> categoryDTOs = new ArrayList<>();
        
        Iterable<CategoryData> entities = categoryDataRepository.findAll();

        for (CategoryData entity : entities) {
            categoryDTOs.add(transform.transform(entity));
        }

        log.info("Returning {} categories.", categoryDTOs.size());
        return categoryDTOs;
    }
}

