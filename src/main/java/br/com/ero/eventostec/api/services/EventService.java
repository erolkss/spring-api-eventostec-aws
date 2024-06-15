package br.com.ero.eventostec.api.services;

import br.com.ero.eventostec.api.domain.event.Event;
import br.com.ero.eventostec.api.domain.event.EventRequestDto;
import br.com.ero.eventostec.api.repositories.EventRepository;
import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class EventService {

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3client;

    @Autowired
    private EventRepository eventRepository;

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

        return eventRepository.save(newEvent);
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
}
