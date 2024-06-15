package br.com.ero.eventostec.api.services;

import br.com.ero.eventostec.api.domain.event.Event;
import br.com.ero.eventostec.api.domain.event.EventRequestDto;
import br.com.ero.eventostec.api.domain.event.EventResponseDto;
import br.com.ero.eventostec.api.repositories.EventRepository;
import com.amazonaws.services.s3.AmazonS3;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class EventService {

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3client;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AddressService addressService;

    public Event createEvent(EventRequestDto data) {
        String imgUrl = null;

        if (data.image() != null) {
            imgUrl = this.uploadImg(data.image());
        }
        Event newEvent = new Event();
        newEvent.setTitle(data.title());
        newEvent.setDescription(data.description());
        newEvent.setEventUrl(data.eventUrl());
        newEvent.setDate(new Date(data.date()));
        newEvent.setImgUrl(imgUrl);
        newEvent.setRemote(data.remote());

        eventRepository.save(newEvent);

        if (!data.remote()) {
            this.addressService.createAddress(data, newEvent);
        }

        return newEvent;
    }

    private String uploadImg(MultipartFile multipartFile) {
        String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        try {
            File file = this.convertMultiparToFile(multipartFile);
            s3client.putObject(bucketName, fileName, file);
            file.delete();
            return s3client.getUrl(bucketName, fileName).toString();
        } catch (Exception e) {
            System.out.println("Failure to update file");
            return "";
        }
    }

    private File convertMultiparToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }

    public List<EventResponseDto> getUpComingEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventsPage = this.eventRepository.findUpComingEvents(new Date(), pageable);
        return eventsPage.map(event -> new EventResponseDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getAddress() != null ? event.getAddress().getCity() : "",
                event.getAddress() != null ? event.getAddress().getUf() : "",
                event.getRemote(),
                event.getEventUrl(),
                event.getImgUrl()
        )).stream().toList();
    }

    public List<EventResponseDto> getFilteredEvents(int page, int size, String title, String city, String uf, Date startDate, Date endDate) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());;

        title = (title != null) ? title : "";
        city = (city != null) ? city : "";
        uf = (uf != null) ? uf : "";
        startDate = (startDate != null) ? startDate : new Date();
        endDate = (endDate != null) ? endDate : new DateTime().plusYears(1).toDate();

        Pageable pageable = PageRequest.of(page, size);

        Page<Event> eventsPage = this.eventRepository.findFilteredEvents(title, city, uf, startDate, endDate, pageable);
        return eventsPage.map(event -> new EventResponseDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getAddress() != null ? event.getAddress().getCity() : "",
                event.getAddress() != null ? event.getAddress().getUf() : "",
                event.getRemote(),
                event.getEventUrl(),
                event.getImgUrl()
        )).stream().toList();
    }
}
