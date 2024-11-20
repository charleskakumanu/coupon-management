package com.example.couponmanagement.constants;

public enum CouponTypes {
    CART_WISE("cart-wise");

    private final String couponName;

    CouponTypes(String couponName) {
        this.couponName = couponName;
    }

    public String getCouponName() {
        return this.couponName;
    }
}
