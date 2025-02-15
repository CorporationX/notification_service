package faang.school.notificationservice.service;

import faang.school.notificationservice.entity.Event;
import faang.school.notificationservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class EventService {
    private final EventRepository eventRepository;

    @Transactional
    public boolean existsById(UUID id) {
        return eventRepository.existsById(id);
    }

    @Transactional
    public Event save(Event event) {
        return eventRepository.save(event);
    }
}
