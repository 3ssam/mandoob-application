package com.mandob.domain;

import com.mandob.base.domain.EntityAudit;
import com.mandob.domain.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends EntityAudit {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salesforce_id", referencedColumnName = "id")
    private Salesforce salesforce;


    @NotNull
    private double totalOrder;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    @Transient
    private List<Product> tempProducts = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @OneToOne(mappedBy = "order")
    private Invoice invoice;


    @ElementCollection
    @CollectionTable(
            name = "Product_Amounts",
            joinColumns = @JoinColumn(name = "ORDER_ID")
    )
    @Column(name = "Order_Amount")
    private List<Integer> orderAmount = new ArrayList<>();


    public void calculateTotalPrice() {
        totalOrder = 0;
        for (int i = 0; i < products.size(); i++)
            totalOrder += (products.get(i).getPrice().doubleValue() * orderAmount.get(i));
    }

    public List<Product> getTempProducts() {
        for (int i = 0; i < products.size(); i++) {
            Product product = new Product();
            product.setBarcode(products.get(i).getBarcode());
            product.setColor(products.get(i).getColor());
            product.setDescription(products.get(i).getDescription());
            product.setDimension(products.get(i).getDimension());
            product.setExpiryDate(products.get(i).getExpiryDate().toString());
            product.setProdDate(products.get(i).getProdDate().toString());
            product.setPrice(products.get(i).getPrice());
            product.setAmount(products.get(i).getAmount());
            product.setRemainingAmount(products.get(i).getAmount());
            //String productImage = saveImage(req.getPhotoUrl());
            product.setPhotoUrl(products.get(i).getPhotoUrl());
            product.setEnName(products.get(i).getEnName());
            product.setArName(products.get(i).getArName());
            product.setWeight(products.get(i).getWeight());
            tempProducts.add(product);
        }
        return tempProducts;
    }

}
