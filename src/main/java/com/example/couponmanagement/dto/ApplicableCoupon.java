package com.example.couponmanagement.dto;

import com.example.couponmanagement.dao.Coupon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicableCoupon extends Coupon {
    private int discount;
}
