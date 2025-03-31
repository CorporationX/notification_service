package faang.school.notificationservice.service;

import faang.school.notificationservice.entity.Event;
import faang.school.notificationservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventService {
    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        return eventRepository.existsById(id);
    }

    @Transactional
    public Event save(Event event) {
        return eventRepository.save(event);
    }
}
