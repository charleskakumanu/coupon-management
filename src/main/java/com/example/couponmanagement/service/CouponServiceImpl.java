package com.example.couponmanagement.service;

import com.alibaba.fastjson.JSONObject;
import com.example.couponmanagement.constants.CouponTypes;
import com.example.couponmanagement.dao.CartCoupon;
import com.example.couponmanagement.dao.Coupon;
import com.example.couponmanagement.repository.CouponRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponServiceImpl implements CouponService{

    private final CouponRepository repository;

    @Autowired
    public CouponServiceImpl(CouponRepository repository) {
        this.repository = repository;
    }

    @Override
    public String addCoupon(JSONObject couponDetails) {
        if(couponDetails.getString("type").equalsIgnoreCase(CouponTypes.CART_WISE.getCouponName())) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Coupon cartCoupon = objectMapper.readValue(couponDetails.toJSONString(), CartCoupon.class);
                addCouponToRepository(cartCoupon);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Unable to parse input", e);
            }
        }
        return "Added";
    }

    private void addCouponToRepository(Coupon coupon) {
        repository.addCoupon(coupon);
    }
}
