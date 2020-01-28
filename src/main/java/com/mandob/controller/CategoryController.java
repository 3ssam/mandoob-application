package com.mandob.controller;

import com.mandob.base.Utils.ApiPageResponse;
import com.mandob.base.Utils.ApiResponse;
import com.mandob.base.Utils.PageRequestVM;
import com.mandob.projection.Category.CategoryProjection;
import com.mandob.request.CategoryReq;
import com.mandob.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ApiPageResponse<CategoryProjection> findAllCategories(PageRequestVM pr) {
        return ApiPageResponse.of(categoryService.findAll(CategoryProjection.class, pr));
    }

    @GetMapping("{categoryId}")
    public ApiResponse<CategoryProjection> findCategoryById(@PathVariable String categoryId) {
        return ApiResponse.ok(categoryService.findById(categoryId, CategoryProjection.class));
    }

    @PostMapping
    public ApiResponse<CategoryProjection> createCategory(@Valid @RequestBody CategoryReq req) {
        return ApiResponse.created(categoryService.create(req));
    }

    @PutMapping("{categoryId}")
    public ApiResponse<CategoryProjection> updateCategory(@PathVariable String categoryId, @Valid @RequestBody CategoryReq req) {
        return ApiResponse.updated(categoryService.update(categoryId, req));
    }
}
