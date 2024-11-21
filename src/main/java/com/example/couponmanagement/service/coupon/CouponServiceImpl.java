package com.example.couponmanagement.service.coupon;

import com.alibaba.fastjson.JSONObject;
import com.example.couponmanagement.constants.CouponTypes;
import com.example.couponmanagement.dao.*;
import com.example.couponmanagement.repository.CouponRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponServiceImpl implements CouponService{

    private final CouponRepository couponRepository;
    @Autowired
    public CouponServiceImpl(CouponRepository repository) {
        this.couponRepository = repository;
    }

    @Override
    public String addCoupon(JSONObject couponDetails) {
        if(couponDetails.getString("type").equalsIgnoreCase(CouponTypes.CART_WISE.getCouponName())) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                CartCoupon cartCoupon = objectMapper.readValue(couponDetails.toJSONString(), CartCoupon.class);
                couponRepository.saveCartCoupon(cartCoupon);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Unable to parse input", e);
            }
            return "Coupon saved";
        }  else if(couponDetails.getString("type").equalsIgnoreCase(CouponTypes.PRODUCT_WISE.getCouponName())) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                ProductCoupon productCoupon = objectMapper.readValue(couponDetails.toJSONString(), ProductCoupon.class);
                couponRepository.saveProductCoupon(productCoupon);

            } catch (JsonProcessingException e) {
                throw new RuntimeException("Unable to parse input", e);
            }
            return "Coupon saved";
        } else if(couponDetails.getString("type").equalsIgnoreCase(CouponTypes.BXGY.getCouponName())) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                BXGYCoupon bxgyCoupon = objectMapper.readValue(couponDetails.toJSONString(), BXGYCoupon.class);
                couponRepository.saveBXGYCoupon(bxgyCoupon);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Unable to parse input", e);
            }
            return "Coupon saved";
        }
        return "Coupon type unknown";

    }

    @Override
    public List<CartCoupon> getCartCoupons() {
        return couponRepository.getCartCoupons();
    }

    @Override
    public List<ProductCoupon> getProductCoupons() {
        return couponRepository.getProductCoupons();
    }

    @Override
    public List<BXGYCoupon> getBXGYCoupons() {
        return couponRepository.getBXGYCoupons();
    }

}

