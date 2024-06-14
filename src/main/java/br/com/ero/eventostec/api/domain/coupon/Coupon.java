package br.com.ero.eventostec.api.domain.coupon;

import br.com.ero.eventostec.api.domain.event.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupon")
@Entity
public class Coupon {

    @Id
    @GeneratedValue
    private UUID id;
    private String code;
    private Integer discount;
    private Date valid;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
