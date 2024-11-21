package com.example.couponmanagement.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetails {
    @JsonProperty("product_id")
    private int productId;
    private int quantity;
    private int price;
    private int discount;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductDetails that = (ProductDetails) o;
        return productId == that.productId && quantity == that.quantity && price == that.price && discount == that.discount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, quantity, price, discount);
    }
}
