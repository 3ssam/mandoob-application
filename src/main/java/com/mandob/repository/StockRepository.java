package com.mandob.repository;

import com.mandob.domain.Customer;
import com.mandob.domain.Product;
import com.mandob.domain.Stock;
import com.mandob.projection.Stock.StockProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends PagingAndSortingRepository<Stock, String> {
    Page<StockProjection> findByCustomer(Customer customer, Pageable pageable);

    StockProjection findByProduct(Product product);

    StockProjection findAllById(String id);

    Stock findByCustomerAndProduct(Customer customer, Product product);
}
