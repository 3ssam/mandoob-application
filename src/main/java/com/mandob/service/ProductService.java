package com.mandob.service;

import com.mandob.base.repository.BaseRepository;
import com.mandob.base.service.MasterService;
import com.mandob.domain.Category;
import com.mandob.domain.Product;
import com.mandob.projection.Product.ProductProjection;
import com.mandob.repository.ProductRepository;
import com.mandob.request.ProductReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ProductService extends MasterService<Product> {
    //private final ProductMapper productMapper;
    private final CategoryService categoryService;
    private final ProductRepository productRepository;
    private final UserService userService;

    public ProductProjection create(ProductReq req) {
        Product product = new Product();//productMapper.toEntity(req);
        product.setCreatedAt(Instant.now());
        product.setCreatedBy(userService.findById(req.getCurrentUser()));
        product = createNewProduct(req,product);
        setProductCategory(req, product);
        productRepository.save(product);
        return findById(product.getId(), ProductProjection.class);
    }

    public ProductProjection update(String productId, ProductReq req) {
        Product product = findById(productId);
        //productMapper.toEntity(req, product);
        product = createNewProduct(req,product);
        setProductCategory(req, product);
        productRepository.save(product);
        return findById(product.getId(), ProductProjection.class);
    }

    public Product createNewProduct(ProductReq req,Product product){
        product.setBarcode(req.getBarcode());
        product.setColor(req.getColor());
        product.setDescription(req.getDescription());
        product.setDimension(req.getDimension());
        product.setExpiryDate(req.getExpiryDate());
        product.setProdDate(req.getProdDate());
        product.setPrice(req.getPrice());
        product.setPhotoUrl(req.getPhotoUrl());
        product.setEnName(req.getEnName());
        product.setArName(req.getArName());
        product.setWeight(req.getWeight());
        //product.setSubCategory();
        product.setCompany(product.getCreatedBy().getCompany());
        product.setUpdatedAt(Instant.now());
        product.setUpdatedBy(userService.findById(req.getCurrentUser()));
        return product;
    }


    private void setProductCategory(ProductReq req, Product product) {
        Category category = req.getCategory() != null ? categoryService.findById(req.getCategory()) : null;
        Category subCategory = req.getSubCategory() != null ? categoryService.findById(req.getSubCategory()) : null;
        product.setCategory(category);
        product.setSubCategory(subCategory);
    }

    @Override
    protected BaseRepository<Product> getRepository() {
        return productRepository;
    }
}
