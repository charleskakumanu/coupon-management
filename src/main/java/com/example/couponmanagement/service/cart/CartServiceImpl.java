package com.example.couponmanagement.service.cart;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.couponmanagement.constants.CouponTypes;
import com.example.couponmanagement.dao.*;
import com.example.couponmanagement.dto.ApplicableCoupon;
import com.example.couponmanagement.repository.CouponRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@Log
public class CartServiceImpl implements CartService {
    private final CouponRepository couponRepository;

    @Autowired
    public CartServiceImpl(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public Map<String, List<ApplicableCoupon>> getApplicableCoupons(JSONObject cartDetails) {
        Map<String, List<ApplicableCoupon>> response = new HashMap<>();
        response.put("applicable_coupons",new ArrayList<>());
        ObjectMapper objectMapper = new ObjectMapper();
        Cart cart;
        try {
            cart = objectMapper.readValue(JSON.toJSONString(cartDetails.get("cart")), Cart.class);
            int cartTotal = calculateCartTotal(cart.getItems());
            CompletableFuture<Void> cartCouponFuture = getCartWiseApplicableCoupons(response, cartTotal);
            CompletableFuture<Void> productCouponFuture = getProductWiseApplicableCoupons(cart, response);
            CompletableFuture<Void> bxgyCouponFuture = getBXGYApplicableCoupons(cart, response);
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(cartCouponFuture, productCouponFuture, bxgyCouponFuture);
            try {
                allFutures.get();
            } catch (Exception e) {
                log.severe("Error getting applicable coupons. Error: " + e.getMessage());
            }
            return response;
        } catch (Exception e) {
             throw new RuntimeException("Unable to parse input", new Exception("Bad data"));
        }
    }

    @Override
    public Map<String, UpdatedCart> applyCouponOnCart(Integer couponId, JSONObject cartDetails) {
        ObjectMapper objectMapper = new ObjectMapper();
        Cart cart;
        try {
            cart = objectMapper.readValue(JSON.toJSONString(cartDetails.get("cart")), Cart.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing input. Error: " + e.getMessage(), e);
        }
        if(couponRepository.getIdToTypeMap().get(couponId).equals(CouponTypes.CART_WISE)) {
            Optional<CartCoupon> coupon = couponRepository.getCartCoupons().stream()
                    .filter(c -> c.getCouponId() == couponId).findFirst();
            if(coupon.isPresent()) {
                return applyCartWiseCoupon(cart, coupon.get());
            }
        } else if(couponRepository.getIdToTypeMap().get(couponId).equals(CouponTypes.PRODUCT_WISE)) {
            Optional<ProductCoupon> coupon = couponRepository.getProductCoupons().stream()
                    .filter(c -> c.getCouponId() == couponId).findFirst();
            if(coupon.isPresent()) {
                return applyProductWiseCoupon(cart, coupon.get());
            }
        }  else if(couponRepository.getIdToTypeMap().get(couponId).equals(CouponTypes.BXGY)) {
            Optional<BXGYCoupon> coupon = couponRepository.getBXGYCoupons().stream()
                    .filter(c -> c.getCouponId() == couponId).findFirst();
            if(coupon.isPresent()) {
                return applyBXGYCoupon(cart, coupon.get());
            }
        }
        throw new RuntimeException("Coupon doesn't exist", new Exception("Id doesn't exist"));
    }

    private Map<String, UpdatedCart> applyCartWiseCoupon(Cart cart, CartCoupon cartCoupon) {
        int cartTotal = calculateCartTotal(cart.getItems());
        Map<String, UpdatedCart> response = new HashMap<>();
        float discountPercent = 0;
        int discount = 0;
        if (cartTotal > cartCoupon.getDetails().getThreshold()) {
            discountPercent = (float) cartCoupon.getDetails().getDiscount() / 100;
            discount = (int) (discountPercent * cartTotal);
        }
        UpdatedCart updatedCart = new UpdatedCart();

        updatedCart.setTotal_price(cartTotal);
        updatedCart.setFinal_price(cartTotal - discount);
        updatedCart.setTotal_discount(discount);
        updatedCart.setItems(cart.getItems());
        response.put("updated_cart", updatedCart);
        return response;
    }

    private Map<String, UpdatedCart> applyProductWiseCoupon(Cart cart, ProductCoupon productCoupon) {
        int cartTotal = calculateCartTotal(cart.getItems());
        Map<String, UpdatedCart> response = new HashMap<>();
        Optional<ProductDetails> details = cart.getItems().stream().filter(product -> Objects.equals(product.getProductId(), productCoupon.getDetails().getProductId())).findFirst();
        if(details.isPresent()) {
            int total = details.get().getQuantity() * details.get().getPrice();
            float discountPercentage = (float) productCoupon.getDetails().getDiscount() / 100;
            UpdatedCart updatedCart = new UpdatedCart();
            int totalDiscount = (int) (total * discountPercentage);
            updatedCart.setItems(cart.getItems());
            updatedCart.setTotal_price(cartTotal);
            updatedCart.setFinal_price(cartTotal - totalDiscount);
            updatedCart.setTotal_discount(totalDiscount);
            response.put("updated_cart", updatedCart);
            updatedCart.getItems().stream()
                    .filter(p -> Objects.equals(p.getProductId(), productCoupon.getDetails().getProductId()))
                    .findFirst()
                    .orElseGet(ProductDetails::new).setTotal_discount(totalDiscount);
        }
        return response;
    }

    private Map<String, UpdatedCart> applyBXGYCoupon(Cart cart, BXGYCoupon bxgyCoupon) {
        List<ProductDetails> buyProducts = bxgyCoupon.getDetails().getBuyProducts();
        Map<Integer, Integer> quantityMap = getProductQuantityMap(cart);
        System.out.println(quantityMap);
        Map<String, UpdatedCart> response = new HashMap<>();
        int cartTotal = calculateCartTotal(cart.getItems());
        Map<Integer, Integer> productDiscountMap = new HashMap<>();
        int totalDiscount = 0;
        for (ProductDetails buyProduct : buyProducts) {
            if (quantityMap.getOrDefault(buyProduct.getProductId(), 0) >= buyProduct.getQuantity()) {
                List<ProductDetails> getProducts = new ArrayList<>();
                for (ProductDetails getProductDetails : bxgyCoupon.getDetails().getGetProducts()) {
                    getProducts.addAll(cart.getItems()
                            .stream()
                            .filter(productDetails -> Objects.equals(productDetails.getProductId(), getProductDetails.getProductId()))
                            .toList());
                }
                for (ProductDetails getProduct : getProducts) {
                    int timesDealCanApply = quantityMap.get(buyProduct.getProductId()) / buyProduct.getQuantity();
                    if (timesDealCanApply > 0) {
                        timesDealCanApply = Math.min(timesDealCanApply, bxgyCoupon.getDetails().getRepetitionLimit());
                        int discount = timesDealCanApply * getProduct.getPrice();
                        totalDiscount += discount;
                        productDiscountMap.put(getProduct.getProductId(), productDiscountMap.getOrDefault(getProduct.getProductId(), 0) + discount);
                    }
                }

            }
        }
        UpdatedCart updatedCart = new UpdatedCart();
        updatedCart.setItems(cart.getItems());
        updatedCart.getItems().stream()
                .map( productDetails -> {
                    int discount = productDiscountMap.getOrDefault(productDetails.getProductId(), 0);
                    productDetails.setTotal_discount(discount);
                    return productDetails;
                }).toList();
        updatedCart.setTotal_discount(totalDiscount);
        updatedCart.setFinal_price(cartTotal - totalDiscount);
        updatedCart.setTotal_price(cartTotal);
        response.put("updated_cart", updatedCart);
        return response;
    }

    private CompletableFuture<Void> getCartWiseApplicableCoupons(Map<String, List<ApplicableCoupon>> response, int cartTotal) {
        return CompletableFuture.runAsync(() -> {
            float discount = 0;
            ApplicableCoupon coupon = null;
            List<CartCoupon> cartCouponList = couponRepository.getCartCoupons();
            for (CartCoupon cartCoupon : cartCouponList) {
                int intermediateDiscount = 0;
                if (cartTotal > cartCoupon.getDetails().getThreshold()) {
                    discount = (float) cartCoupon.getDetails().getDiscount() / 100;
                    intermediateDiscount = (int) (discount * cartTotal);
                }
                if (discount < intermediateDiscount) {
                    discount = intermediateDiscount;
                    coupon = createApplicableCoupon(cartCoupon.getCouponId(), cartCoupon.getType(), (int) discount);
                }
            }
            response.get("applicable_coupons").add(coupon);
        });
    }

    private CompletableFuture<Void> getProductWiseApplicableCoupons(Cart cart, Map<String, List<ApplicableCoupon>> response) {
        return CompletableFuture.runAsync(() -> {
            List<ProductCoupon> productCoupons = couponRepository.getProductCoupons();
            for (ProductCoupon productCoupon : productCoupons) {
               Optional<ProductDetails> details = cart.getItems().stream().filter(product -> Objects.equals(product.getProductId(), productCoupon.getDetails().getProductId())).findAny();
               if (details.isPresent()) {
                   int total = details.get().getQuantity() * details.get().getPrice();
                   float discount = (float) productCoupon.getDetails().getDiscount() / 100;
                   ApplicableCoupon coupon = createApplicableCoupon(productCoupon.getCouponId(), productCoupon.getType(), (int) (total * discount));
                   response.get("applicable_coupons").add(coupon);
               }
            }
        });
    }

    private Map<Integer, Integer> getProductQuantityMap(Cart cart) {
        Map<Integer, Integer> response = new HashMap<>();
        for(ProductDetails details: cart.getItems()) {
            response.put(details.getProductId(), response.getOrDefault(details.getProductId(), 0) + details.getQuantity());
        }
        System.out.println(response);
        return response;
    }

    private CompletableFuture<Void> getBXGYApplicableCoupons(Cart cart, Map<String, List<ApplicableCoupon>> response) {
        return CompletableFuture.runAsync(() -> {
            List<BXGYCoupon> bxgyCoupons = couponRepository.getBXGYCoupons();
            ApplicableCoupon coupon = getBXGYDiscountCoupon(cart, bxgyCoupons);
            if(null != coupon) {
                response.get("applicable_coupons").add(coupon);
            }
        });
    }

    private ApplicableCoupon getBXGYDiscountCoupon(Cart cart, List<BXGYCoupon> bxgyCoupons) {
        int discount = 0;
        ApplicableCoupon coupon = null;
        Map<Integer, Integer> quantityMap = getProductQuantityMap(cart);
        for (BXGYCoupon bxgyCoupon : bxgyCoupons) {
            int intermediateDiscount = 0;
            List<ProductDetails> buyProducts = bxgyCoupon.getDetails().getBuyProducts();
            for (ProductDetails buyProduct : buyProducts) {
                if (quantityMap.getOrDefault(buyProduct.getProductId(), 0) >= buyProduct.getQuantity()) {
                    List<ProductDetails> getProducts = new ArrayList<>();
                    for (ProductDetails getProductDetails : bxgyCoupon.getDetails().getGetProducts()) {
                        getProducts.addAll(cart.getItems()
                                .stream()
                                .filter(productDetails -> Objects.equals(productDetails.getProductId(), getProductDetails.getProductId()))
                                .toList());
                    }
                    for (ProductDetails getProduct : getProducts) {
                        int timesDealCanApply = quantityMap.get(buyProduct.getProductId()) / buyProduct.getQuantity();
                        if (timesDealCanApply > 0) {
                            timesDealCanApply = Math.min(timesDealCanApply, bxgyCoupon.getDetails().getRepetitionLimit());
                            intermediateDiscount += timesDealCanApply * getProduct.getPrice();
                        }
                    }

                }
            }
            if (discount < intermediateDiscount) {
                discount = intermediateDiscount;
                 coupon = createApplicableCoupon(bxgyCoupon.getCouponId(), bxgyCoupon.getType(), discount);
            }
        }
        return coupon;
    }

    private ApplicableCoupon createApplicableCoupon(int couponId, String couponType, int discount) {
        ApplicableCoupon coupon = new ApplicableCoupon();
        coupon.setCouponId(couponId);
        coupon.setType(couponType);
        coupon.setDiscount(discount);
        return coupon;
    }

    private int calculateCartTotal(List<ProductDetails> productDetailsList) {
        int total = 0;
        for(ProductDetails detail: productDetailsList) {
            total += detail.getQuantity() * detail.getPrice();
        }
        return total;
    }
}
