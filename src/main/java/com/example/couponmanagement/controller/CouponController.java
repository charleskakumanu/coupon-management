package com.example.couponmanagement.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.couponmanagement.dao.BXGYCoupon;
import com.example.couponmanagement.dao.CartCoupon;
import com.example.couponmanagement.dao.ProductCoupon;
import com.example.couponmanagement.service.coupon.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CouponController {
    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/coupons")
    public ResponseEntity<String> addCoupon(@RequestBody JSONObject couponDetails) {
        return ResponseEntity.ok(this.couponService.addCoupon(couponDetails));
    }

    @GetMapping("/coupons/cart")
    public List<CartCoupon> getCartCoupons() {
        return couponService.getCartCoupons();
    }

    @GetMapping("/coupons/product")
    public List<ProductCoupon> getProductCoupons() {
        return couponService.getProductCoupons();
    }

    @GetMapping("/coupons/bxgy")
    public List<BXGYCoupon> getBXGYCoupons() {
        return couponService.getBXGYCoupons();
    }
}
