package com.example.couponmanagement.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BXGYCouponDetails {
    @JsonProperty("buy_products")
    private List<ProductDetails> buyProducts;
    @JsonProperty("get_products")
    private List<ProductDetails> getProducts;
    @JsonProperty("repition_limit")
    private int repetitionLimit;
}
