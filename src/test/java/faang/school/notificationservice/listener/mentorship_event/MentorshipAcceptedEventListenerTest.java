package faang.school.notificationservice.listener.mentorship_event;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.MentorshipAcceptedEventDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MentorshipAcceptedEventListenerTest {
    @InjectMocks
    private MentorshipAcceptedEventListener mentorshipAcceptedEventListener;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private MentorshipAcceptedEventDto mentorshipAcceptedEventDto;
    @Mock
    private Message message;
    private byte[] jsonPattern;


    @BeforeEach
    void setUp() throws IOException {
        jsonPattern = new byte[]{};
        when(message.getBody()).thenReturn(jsonPattern);
        when(objectMapper.readValue(jsonPattern, MentorshipAcceptedEventDto.class)).thenReturn(mentorshipAcceptedEventDto);
    }

    @Test
    void testMethodReadValue() throws IOException {
        when(mentorshipAcceptedEventListener.readValue(message.getBody(), MentorshipAcceptedEventDto.class)).thenReturn(mentorshipAcceptedEventDto);
        mentorshipAcceptedEventListener.readValue(jsonPattern, MentorshipAcceptedEventDto.class);
        verify(objectMapper).readValue(message.getBody(), MentorshipAcceptedEventDto.class);
    }

    @Test
    void testToObject_FailedSerialization() throws IOException {
        when(objectMapper.readValue(jsonPattern, MentorshipAcceptedEventDto.class)).thenThrow(new IOException());
        assertThrows(RuntimeException.class, () -> mentorshipAcceptedEventListener.readValue(jsonPattern, MentorshipAcceptedEventDto.class));
    }
}