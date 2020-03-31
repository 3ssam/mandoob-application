package com.mandob.controller;

import com.mandob.base.Projection.LookupProjection;
import com.mandob.base.Utils.ApiPageResponse;
import com.mandob.base.Utils.ApiResponse;
import com.mandob.base.Utils.PageRequestVM;
import com.mandob.projection.Product.ProductListProjection;
import com.mandob.projection.Product.ProductProjection;
import com.mandob.request.ProductReq;
import com.mandob.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    private ApiPageResponse<ProductListProjection> findAllProducts(PageRequestVM pr) {
        return ApiPageResponse.of(productService.findAll(ProductListProjection.class, pr));
    }

    @GetMapping("{productId}")
    public ApiResponse<ProductProjection> findProductById(@PathVariable String productId) {
        return ApiResponse.ok(productService.findById(productId, ProductProjection.class));
    }

    @PostMapping
    public ApiResponse<ProductProjection> createProduct(@Valid @RequestBody ProductReq req) {
        return ApiResponse.created(productService.create(req));
    }

    @PutMapping("{productId}")
    public ApiResponse<ProductProjection> updateProduct(@PathVariable String productId, @Valid @RequestBody ProductReq req) {
        return ApiResponse.updated(productService.update(productId, req));
    }

    @DeleteMapping("{productId}")
    public ApiResponse<String> updateProduct(@PathVariable String productId) {
        return ApiResponse.deleted(productService.delete(productId));
    }

    @GetMapping("lookup")
    public List<LookupProjection> lookup(String productId) {
        return productService.lookup(productId);
    }
}
