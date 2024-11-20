package com.example.couponmanagement.repository;

import com.example.couponmanagement.constants.CouponTypes;
import com.example.couponmanagement.dao.CartCoupon;
import com.example.couponmanagement.dao.Coupon;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CouponRepository {
    List<CartCoupon> cartCouponList = new ArrayList<>();

    public void addCoupon(Coupon coupon) {
        if (coupon.getType().equalsIgnoreCase(CouponTypes.CART_WISE.getCouponName())) {
           if (cartCouponList.contains(coupon)) {
               throw new RuntimeException("Coupon already exists", new Exception("Duplicate coupon"));
           } else {
               cartCouponList.add((CartCoupon) coupon);
           }
        }
    }
}
