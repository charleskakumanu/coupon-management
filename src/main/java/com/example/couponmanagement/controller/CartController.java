package com.example.couponmanagement.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.couponmanagement.dao.UpdatedCart;
import com.example.couponmanagement.dto.ApplicableCoupon;
import com.example.couponmanagement.service.cart.CartService;
import org.springframework.web.bind.annotation.PathVariable;
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
    public Map<String, List<ApplicableCoupon>> getApplicableCoupons(@RequestBody JSONObject cartDetails) {
        return cartService.getApplicableCoupons(cartDetails);
    }

    @PostMapping("/apply-coupon/{id}")
    public Map<String, UpdatedCart> applyCouponOnCart(@PathVariable("id") Integer couponId, @RequestBody JSONObject cartDetails) {
        return cartService.applyCouponOnCart(couponId, cartDetails);
    }
}
