package com.example.couponmanagement.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.couponmanagement.service.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CouponController {
    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping
    public ResponseEntity<String> addCoupon(@RequestBody JSONObject couponDetails) {
        return ResponseEntity.ok(this.couponService.addCoupon(couponDetails));
    }
}
