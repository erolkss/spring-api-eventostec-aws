package br.com.ero.eventostec.api.controller;

import br.com.ero.eventostec.api.domain.coupon.Coupon;
import br.com.ero.eventostec.api.domain.coupon.CouponRequestDto;
import br.com.ero.eventostec.api.services.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping("/event/{eventId}")
    public ResponseEntity<Coupon> addCouponsToEvent(@PathVariable UUID eventId, @RequestBody CouponRequestDto data) {
        Coupon coupon = couponService.addCouponEvent(eventId, data);
        return ResponseEntity.ok(coupon);
    }
}
