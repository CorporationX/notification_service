package faang.school.notificationservice.service;

import faang.school.notificationservice.entity.Event;
import faang.school.notificationservice.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {
    @Mock
    private EventRepository eventRepository;
    @InjectMocks
    private EventService eventService;

    private String eventId;
    private Event event;

    @BeforeEach
    void setUp() {
        eventId = "3";
        event = new Event(eventId);
        event.setProcessedAt(LocalDateTime.now());
    }

    @Test
    void existsByIdReturnTrueEventExists() {
        when(eventRepository.existsById(eventId)).thenReturn(true);

        boolean result = eventService.existsById(eventId);

        assertTrue(result);
        verify(eventRepository, times(1)).existsById(eventId);
    }

    @Test
    void existsByIdReturnFalseEventDoesNotExist() {
        when(eventRepository.existsById(eventId)).thenReturn(false);

        boolean result = eventService.existsById(eventId);

        assertFalse(result);
        verify(eventRepository, times(1)).existsById(eventId);
    }

    @Test
    void saveShouldSaveEvent() {
        when(eventRepository.save(event)).thenReturn(event);

        Event result = eventService.save(event);

        assertEquals(event, result);
        verify(eventRepository, times(1)).save(event);
    }
}