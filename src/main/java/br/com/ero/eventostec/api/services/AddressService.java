package br.com.ero.eventostec.api.services;

import br.com.ero.eventostec.api.domain.address.Address;
import br.com.ero.eventostec.api.domain.event.Event;
import br.com.ero.eventostec.api.domain.event.EventRequestDto;
import br.com.ero.eventostec.api.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public void createAddress(EventRequestDto data, Event event) {
        Address address = new Address();
        address.setCity(data.city());
        address.setUf(data.state());
        address.setEvent(event);

        addressRepository.save(address);
    }
}
