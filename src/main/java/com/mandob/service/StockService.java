package com.mandob.service;

import com.mandob.base.Utils.PageRequestVM;
import com.mandob.base.exception.ApiValidationException;
import com.mandob.domain.Customer;
import com.mandob.domain.Product;
import com.mandob.domain.Salesforce;
import com.mandob.domain.Stock;
import com.mandob.projection.Stock.StockProjection;
import com.mandob.repository.CustomerRepository;
import com.mandob.repository.ProductRepository;
import com.mandob.repository.SalesforceRepository;
import com.mandob.repository.StockRepository;
import com.mandob.request.StockReq;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class StockService {
    @Autowired
    private final StockRepository stockRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final SalesforceRepository salesforceRepository;


    @Transactional
    public StockProjection CreateStock(StockReq req) {
        Customer customer = customerRepository.findById(req.getCurrentUser()).orElse(null);
        if (customer == null)
            throw new ApiValidationException("customer Id", "customer-id-is-not-vaild");
        Salesforce salesforce = customer.getSalesforce();
        Product product = productRepository.findById(req.getProduct()).orElse(null);
        if (product == null)
            throw new ApiValidationException("product Id", "product-id-is-not-vaild");
        Stock stock = stockRepository.findByCustomerAndProduct(customer, product);
        if (stock != null)
            return editStock(stock.getId(), req);
        stock = new Stock();
        stock.setCompany(customer.getCompany());
        stock.setCreatedAt(Instant.now());
        stock.setCreatedBy(customer);
        stock.setUpdatedAt(Instant.now());
        stock.setUpdatedBy(customer);
        stock.setCustomer(customer);
        stock.setProduct(product);
        stock.setCustomerName(customer.getEnName());
        stock.setSalesforceName(salesforce.getEnName());
        stock.setSalesforce(salesforce);
        stock.setStockItems(req.getStockItems());
        stock.setSoldItems(req.getSoldItems());
        stockRepository.save(stock);
        return stockRepository.findAllById(stock.getId());
    }

    @Transactional
    public StockProjection editStock(String stockId, StockReq req) {
        Stock stock = stockRepository.findById(stockId).get();
        if (stock == null)
            throw new ApiValidationException("stock Id", "stock-id-is-not-vaild");
        if (stock.getStockItems() - req.getSoldItems() < 0)
            throw new ApiValidationException("number of items is big", "number of items is bigger than items in stock");
        stock.setUpdatedAt(Instant.now());
        stock.setSoldItems(stock.getSoldItems() + req.getSoldItems());
        stock.setStockItems(stock.getStockItems() - req.getSoldItems() + req.getStockItems());
        stockRepository.save(stock);
        return stockRepository.findAllById(stock.getId());
    }

    public StockProjection getStock(String id, String productId) {
        if (id == null)
            return getStockByProduct(productId);
        else
            return getStockById(id);
    }

    private StockProjection getStockById(String id) {
        StockProjection stock = stockRepository.findAllById(id);
        if (stock == null)
            throw new ApiValidationException("stock Id", "stock-id-is-not-vaild");
        return stock;
    }

    private StockProjection getStockByProduct(String productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null)
            throw new ApiValidationException("product Id", "product-id-is-not-vaild");
        StockProjection stock = stockRepository.findByProduct(product);
        if (stock == null)
            throw new ApiValidationException("stock Id", "stock-id-is-not-vaild");
        return stock;
    }

    public Page<StockProjection> getStocks(String customerId, PageRequestVM pageNumber) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new ApiValidationException("customer Id", "customer-id-is-not-vaild");
        Page<StockProjection> stocks = stockRepository.findByCustomer(customer, pageNumber.buildWithPage(pageNumber.getPage()));
        return stocks;
    }

    @Transactional
    public String deleteStock(String id) {
        Stock stock = stockRepository.findById(id).get();
        if (stock == null)
            throw new ApiValidationException("stock Id", "stock-id-is-not-vaild");
        StockProjection deletedStock = stockRepository.findAllById(id);
        stockRepository.delete(stock);
        return "Deleted Successfully";
    }

}
