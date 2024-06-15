package br.com.ero.eventostec.api.services;

import br.com.ero.eventostec.api.domain.coupon.Coupon;
import br.com.ero.eventostec.api.domain.coupon.CouponRequestDto;
import br.com.ero.eventostec.api.domain.event.Event;
import br.com.ero.eventostec.api.repositories.CouponRepository;
import br.com.ero.eventostec.api.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private EventRepository eventRepository;

    public Coupon addCouponEvent(UUID eventId, CouponRequestDto couponDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event Not found"));

        Coupon coupon = new Coupon();
        coupon.setCode(couponDto.code());
        coupon.setDiscount(couponDto.discount());
        coupon.setValid(new Date(couponDto.valid()));
        coupon.setEvent(event);

        return couponRepository.save(coupon);
    }

    public List<Coupon> consultCoupons(UUID eventId, Date currentDate) {
        return couponRepository.findByEventIdAndValidAfter(eventId, currentDate);
    }
}
