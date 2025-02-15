package faang.school.notificationservice.service;

import faang.school.notificationservice.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DatabaseCleanupJobTest {
    @Mock
    private EventRepository eventRepository;
    @InjectMocks
    private DatabaseCleanupJob databaseCleanupJob;

    @Test
    void cleanupDatabase_ShouldDeleteOldEvents() {
        when(eventRepository.deleteByProcessedAtBefore(any(LocalDateTime.class))).thenReturn(10);

        databaseCleanupJob.cleanupDatabase();

        verify(eventRepository, times(1)).deleteByProcessedAtBefore(any(LocalDateTime.class));
    }
}