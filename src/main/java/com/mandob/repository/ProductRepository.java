package com.mandob.repository;


import com.mandob.base.repository.MasterRepository;
import com.mandob.domain.Product;
import com.mandob.projection.Product.ProductProjection;

public interface ProductRepository extends MasterRepository<Product> {
    ProductProjection findByBarcode(String barcode);

    ProductProjection findByIdOrBarcode(String id, String barcode);
}
