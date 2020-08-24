package com.mandob.controller;

import com.mandob.base.Utils.PageRequestVM;
import com.mandob.projection.Stock.StockProjection;
import com.mandob.request.StockReq;
import com.mandob.service.StockService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("stocks")
public class StockController {


    private final StockService stockService;

    @PostMapping
    public StockProjection createStock(@Valid @RequestBody StockReq req) {
        return stockService.CreateStock(req);
    }

    @PutMapping("{stockId}")
    public StockProjection editStock(@PathVariable String stockId, @RequestBody StockReq req) {
        return stockService.editStock(stockId, req);
    }

    @GetMapping()
    public StockProjection findStock(@RequestParam(required = false) String stockId,
                                     @RequestParam(required = false) String productId) {
        return stockService.getStock(stockId, productId);
    }

    @GetMapping("{customerId}")
    public Page<StockProjection> findStock(@PathVariable String customerId,
                                           @RequestParam(required = true) PageRequestVM pageNumber) {
        return stockService.getStocks(customerId, pageNumber);
    }

    @DeleteMapping("{stockId}")
    public String deleteStock(@PathVariable String stockId) {
        return stockService.deleteStock(stockId);
    }

}
