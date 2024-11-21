package com.example.couponmanagement.service.coupon;


import com.alibaba.fastjson.JSONObject;
import com.example.couponmanagement.dao.BXGYCoupon;
import com.example.couponmanagement.dao.CartCoupon;
import com.example.couponmanagement.dao.ProductCoupon;

import java.util.List;

public interface CouponService {
    public String addCoupon(JSONObject couponDetails);

    List<CartCoupon> getCartCoupons();

    List<ProductCoupon> getProductCoupons();

    List<BXGYCoupon> getBXGYCoupons();
}
