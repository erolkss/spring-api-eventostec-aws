package br.com.ero.eventostec.api.domain.address;

import br.com.ero.eventostec.api.domain.event.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "address")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue
    private UUID id;
    private String city;
    private String uf;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
