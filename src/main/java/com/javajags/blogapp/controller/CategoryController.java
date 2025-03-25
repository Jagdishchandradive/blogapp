package com.javajags.blogapp.controller;

import com.javajags.blogapp.payload.ApiResponse;
import com.javajags.blogapp.payload.CategoryDto;
import com.javajags.blogapp.service.CategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    @Autowired
    private CategoryService categoryService;

    //create-category
    @PostMapping("/create-category")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        logger.info("Request received to create category: {}", categoryDto);
        CategoryDto createCategory = this.categoryService.createCategory(categoryDto);
        return new ResponseEntity<CategoryDto>(createCategory, HttpStatus.CREATED);
    }

    // get-allCategories
    @GetMapping("/allCategories")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        logger.info("Request received to fetch all categories");
        List<CategoryDto> categories = this.categoryService.getCategories();
        return ResponseEntity.ok(categories);
    }

    // get-categoryById
    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Integer catId) {
        logger.info("Request received to get single category with ID: {}", catId);
        CategoryDto categoryDto = this.categoryService.getCategory(catId);
        return new ResponseEntity<CategoryDto>(categoryDto, HttpStatus.OK);

    }

    //update-categoryById
    @PutMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable Integer catId) {
        logger.info("Request received to update user with ID: {}, payload: {}", catId, categoryDto);
        CategoryDto updatedCategory = this.categoryService.updateCategory(categoryDto, catId);
        return new ResponseEntity<CategoryDto>(updatedCategory, HttpStatus.OK);
    }

    // delete-categoryById

    @DeleteMapping("/{catId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer catId) {
        logger.info("Request received to delete Category with ID: {}", catId);
        this.categoryService.deleteCategory(catId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("category is deleted successfully !!", true), HttpStatus.OK);
    }


}
