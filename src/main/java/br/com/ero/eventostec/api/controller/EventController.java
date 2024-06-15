package br.com.ero.eventostec.api.controller;

import br.com.ero.eventostec.api.domain.event.Event;
import br.com.ero.eventostec.api.domain.event.EventRequestDto;
import br.com.ero.eventostec.api.domain.event.EventResponseDto;
import br.com.ero.eventostec.api.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<Event> create(
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("date") Long date,
            @RequestParam("city") String city,
            @RequestParam("state") String state,
            @RequestParam("remote") Boolean remote,
            @RequestParam("eventUrl") String eventUrl,
            @RequestParam(value = "image", required = false) MultipartFile image

    ) {
        EventRequestDto eventRequestDto = new EventRequestDto(title, description, date, city, state, remote, eventUrl, image);
        Event newEvent = this.eventService.createEvent(eventRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newEvent);
    }


    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getEvents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        List<EventResponseDto> allEvents = eventService.getUpComingEvents(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(allEvents);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<EventResponseDto>> filterEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String uf,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate
    ) {
        List<EventResponseDto> events = eventService.getFilteredEvents(page, size, title, city, uf, startDate, endDate);
        return ResponseEntity.status(HttpStatus.OK).body(events);
    }
}
