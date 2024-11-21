package com.example.couponmanagement.service.cart;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.couponmanagement.dao.Cart;
import com.example.couponmanagement.dao.Coupon;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    @Override
    public Map<String, List<Coupon>> getApplicableCoupons(JSONObject cartDetails) {
        ObjectMapper objectMapper = new ObjectMapper();
        Cart cart;
        try {
            cart = objectMapper.readValue(JSON.toJSONString(cartDetails.get("cart")), Cart.class);
        } catch (Exception e) {
            throw new RuntimeException("Unable to parse input", new Exception("Bad data"));
        }
        System.out.println(cart);
        return Map.of();
    }
}
