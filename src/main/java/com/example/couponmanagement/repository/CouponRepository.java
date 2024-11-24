package com.example.couponmanagement.repository;

import com.example.couponmanagement.constants.CouponTypes;
import com.example.couponmanagement.dao.*;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CouponRepository {
    private int couponIDCounter = 0;

    private synchronized int incrementCouponIDCounter() {
        return ++couponIDCounter;
    }

    List<CartCoupon> cartCouponList = new ArrayList<>();
    List<ProductCoupon> productCouponList = new ArrayList<>();
    List<BXGYCoupon> bxgyCouponList = new ArrayList<>();
    @Getter
    Map<Integer, CouponTypes> idToTypeMap = new HashMap<>();

    public void saveCartCoupon(CartCoupon cartCoupon) {
       if (cartCouponList.contains(cartCoupon)) {
           throwRuntimeException();
       } else {
           cartCoupon.setCouponId(incrementCouponIDCounter());
           cartCouponList.add(cartCoupon);
       }
       idToTypeMap.put(couponIDCounter, CouponTypes.CART_WISE);
    }

    public void saveProductCoupon(ProductCoupon productCoupon) {
        if (productCouponList.contains(productCoupon)) {
            throwRuntimeException();
        } else {
            productCoupon.setCouponId(incrementCouponIDCounter());
            productCouponList.add(productCoupon);
        }
        idToTypeMap.put(couponIDCounter, CouponTypes.PRODUCT_WISE);
    }

    public void saveBXGYCoupon(BXGYCoupon bxgyCoupon) {
        if (bxgyCouponList.contains(bxgyCoupon)) {
            throwRuntimeException();
        } else {
            bxgyCoupon.setCouponId(incrementCouponIDCounter());
            bxgyCouponList.add(bxgyCoupon);
        }
        idToTypeMap.put(couponIDCounter, CouponTypes.BXGY);
    }

    private void throwRuntimeException() {
        throw new RuntimeException("Coupon already exists", new Exception("Duplicate coupon"));
    }

    public List<CartCoupon> getCartCoupons() {
        return this.cartCouponList;
    }

    public List<ProductCoupon> getProductCoupons() {
        return this.productCouponList;
    }

    public List<BXGYCoupon> getBXGYCoupons() {
        return this.bxgyCouponList;
    }
}
