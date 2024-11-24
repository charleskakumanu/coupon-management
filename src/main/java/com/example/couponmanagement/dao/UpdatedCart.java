package com.example.couponmanagement.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedCart extends Cart{
    private Integer total_discount;
    private Integer total_price;
    private Integer final_price;
}
