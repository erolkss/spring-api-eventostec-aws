package br.com.ero.eventostec.api.domain.event;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

public record EventResponseDto(
        UUID id, String title, String description, Date date, String city, String state, Boolean remote, String eventUrl, String imageUrl

) {
}
