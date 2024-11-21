package com.example.couponmanagement.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.couponmanagement.dao.Coupon;
import com.example.couponmanagement.service.cart.CartService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/applicable-coupons")
    public Map<String, List<Coupon>> getApplicableCoupons(@RequestBody JSONObject cartDetails) {
        return cartService.getApplicableCoupons(cartDetails);
    }
}
