package com.mandob.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductReq {
    @NotBlank(message = "must-not-be-null")
    private String arName;

    @NotBlank(message = "must-not-be-null")
    private String enName;

    @NotBlank(message = "must-not-be-blank")
    private String category;

    private String subCategory;

    @NotBlank(message = "must-not-be-blank")
    private String description;

    @NotBlank(message = "must-not-be-blank")
    @Pattern(regexp = "(^$|[0-9]+)", message = "accept-numbers-only")
    private String barcode;

    @NotNull(message = "required-field")
    @Pattern(regexp = "(^$|[0-9]+|[0-9]+\\.[0-9]+)", message = "accept-numbers-or-decimals-only")
    private String price;

    private String dimension;

    private String color;

    @NotNull(message = "required-field")
    private LocalDate prodDate;

    @NotNull(message = "required-field")
    private LocalDate expiryDate;

    private String photoUrl;

    @NotNull(message = "required-field")
    private int amount;

    @NotBlank(message = "must-not-be-blank")
    private String currentUser;


    @Pattern(regexp = "(^$|[0-9]+|[0-9]+\\.[0-9]+)", message = "accept-numbers-or-decimals-only")
    private String weight;

    public Long getBarcode() {
        return barcode == null ? 0 : Long.parseLong(barcode);
    }

    public BigDecimal getPrice() {
        return price == null ? BigDecimal.ZERO : BigDecimal.valueOf(Long.parseLong(price));
    }

    public BigDecimal getWeight() {
        return weight == null ? BigDecimal.ZERO : BigDecimal.valueOf(Long.parseLong(weight));
    }
}
