package com.mandob.domain;

import com.mandob.base.domain.MasterFile;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product extends MasterFile {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id", referencedColumnName = "id", nullable = false)
    private Category subCategory;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "barcode", nullable = false)
    private Long barcode;

    @NotNull
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "dimension")
    private String dimension;

    @Column(name = "color")
    private String color;

    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "production_date")
    private LocalDate prodDate;

    @Column(name = "expire_date")
    private LocalDate expiryDate;

    @Column(name = "photo_url")
    private String photoUrl;

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", arName='" + arName + '\'' +
                ", enName='" + enName + '\'' +
                ", description='" + description + '\'' +
                ", barcode=" + barcode +
                ", price=" + price +
                ", dimension='" + dimension + '\'' +
                ", color='" + color + '\'' +
                ", prodDate=" + prodDate +
                ", expiryDate=" + expiryDate +
                ", photoUrl='" + photoUrl + '\'' +
                "}";
    }
}
