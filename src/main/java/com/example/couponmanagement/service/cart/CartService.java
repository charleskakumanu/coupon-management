package com.example.couponmanagement.service.cart;

import com.alibaba.fastjson.JSONObject;
import com.example.couponmanagement.dao.UpdatedCart;
import com.example.couponmanagement.dto.ApplicableCoupon;

import java.util.List;
import java.util.Map;

public interface CartService {
    public Map<String, List<ApplicableCoupon>> getApplicableCoupons(JSONObject cartDetails);

    Map<String, UpdatedCart> applyCouponOnCart(Integer couponId, JSONObject cartDetails);
}
