package com.example.couponmanagement.repository;

import com.example.couponmanagement.constants.CouponTypes;
import com.example.couponmanagement.dao.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CouponRepository {
    private int cartCouponCounter = 0;
    private int productCouponCounter = 0;
    private int bxgyCouponCounter = 0;

    private synchronized int incrementCartCouponCounter() {
        return ++cartCouponCounter;
    }

    private synchronized int incrementProductCouponCounter() {
        return ++productCouponCounter;
    }

    private synchronized int incrementBXGYCouponCounter() {
        return ++bxgyCouponCounter;
    }

    List<CartCoupon> cartCouponList = new ArrayList<>();
    List<ProductCoupon> productCouponList = new ArrayList<>();
    List<BXGYCoupon> bxgyCouponList = new ArrayList<>();

    public void saveCartCoupon(CartCoupon cartCoupon) {
       if (cartCouponList.contains(cartCoupon)) {
           throwRuntimeException();
       } else {
           cartCoupon.setCouponId(incrementCartCouponCounter());
           cartCouponList.add(cartCoupon);
       }
    }

    public void saveProductCoupon(ProductCoupon productCoupon) {
        if (productCouponList.contains(productCoupon)) {
            throwRuntimeException();
        } else {
            productCoupon.setCouponId(incrementProductCouponCounter());
            productCouponList.add(productCoupon);
        }
    }

    public void saveBXGYCoupon(BXGYCoupon bxgyCoupon) {
        if (bxgyCouponList.contains(bxgyCoupon)) {
            throwRuntimeException();
        } else {
            bxgyCoupon.setCouponId(incrementBXGYCouponCounter());
            bxgyCouponList.add(bxgyCoupon);
        }
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
