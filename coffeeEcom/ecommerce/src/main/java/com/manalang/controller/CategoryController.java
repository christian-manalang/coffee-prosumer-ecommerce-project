package com.manalang.controller;

import com.manalang.model.Category;
import com.manalang.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/api/categories")
    public ResponseEntity<?> getAllCategories() {
        log.info("Request received for /api/categories");
        ResponseEntity<?> response;
        try {
            List<Category> categories = categoryService.getAllCategories();
            response = ResponseEntity.ok(categories);
        } catch (Exception ex) {
            log.error("Failed to retrieve categories: {}", ex.getMessage(), ex);
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving categories: " + ex.getMessage());
        }
        return response;
    }
}

