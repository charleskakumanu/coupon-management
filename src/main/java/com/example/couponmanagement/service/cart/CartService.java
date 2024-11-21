package com.example.couponmanagement.service.cart;

import com.alibaba.fastjson.JSONObject;
import com.example.couponmanagement.dao.Coupon;

import java.util.List;
import java.util.Map;

public interface CartService {
    public Map<String, List<Coupon>> getApplicableCoupons(JSONObject cartDetails);
}
