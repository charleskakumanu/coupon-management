package com.example.couponmanagement.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {
    @JsonProperty("type")
    private String type;
    private LocalDateTime expirationTime;
    private int couponId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Coupon coupon = (Coupon) o;
        return Objects.equals(type, coupon.type) && Objects.equals(expirationTime, coupon.expirationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, expirationTime);
    }
}
