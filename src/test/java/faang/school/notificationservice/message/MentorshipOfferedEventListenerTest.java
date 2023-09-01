package faang.school.notificationservice.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.MentorshipOfferedEventDto;
import faang.school.notificationservice.messageBuilder.MentorshipOfferBuilder;
import faang.school.notificationservice.service.post.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class MentorshipOfferedEventListenerTest {
    @InjectMocks
    private MentorshipOfferedEventListener mentorshipOfferedEventListener;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private MentorshipOfferBuilder mentorshipOfferBuilder;
    @Mock
    private List<NotificationService> postServices;

    private MentorshipOfferedEventDto mentorshipOfferedEventDto;
    private Message message;
    private String text;

    @Test
    public void testOnMessage() throws IOException {
        message = new Message() {
            @Override
            public byte[] getBody() {
                return "channel-name".getBytes();
            }

            @Override
            public byte[] getChannel() {
                return "message-content".getBytes();
            }
        };
        Mockito.when(objectMapper.readValue(message.getBody(), MentorshipOfferedEventDto.class))
                .thenReturn(mentorshipOfferedEventDto);
        Mockito.when(mentorshipOfferBuilder.createMessage(mentorshipOfferedEventDto))
                .thenReturn(text);

        mentorshipOfferedEventListener.onMessage(message, new byte[0]);
        Mockito.verify(objectMapper, Mockito.times(1))
                .readValue(message.getBody(), MentorshipOfferedEventDto.class);
        Mockito.verify(mentorshipOfferBuilder, Mockito.times(1))
                .createMessage(mentorshipOfferedEventDto);
        Mockito.verify(postServices, Mockito.times(1))
                .stream();
    }

    @Test
    public void testNotificationService() throws IOException {
        List<NotificationService> postServices = List.of(Mockito.mock(NotificationService.class), Mockito.mock(NotificationService.class));
        MentorshipOfferedEventListener mentorshipOfferedEventListener = new MentorshipOfferedEventListener(objectMapper, mentorshipOfferBuilder, postServices);

        message = new Message() {
            @Override
            public byte[] getBody() {
                return "channel-name".getBytes();
            }

            @Override
            public byte[] getChannel() {
                return "message-content".getBytes();
            }
        };

        Mockito.when(objectMapper.readValue(message.getBody(), MentorshipOfferedEventDto.class))
                .thenReturn(mentorshipOfferedEventDto);
        Mockito.when(mentorshipOfferBuilder.createMessage(mentorshipOfferedEventDto))
                .thenReturn(text);
        Mockito.when(postServices.get(0).isPreferredContact(mentorshipOfferedEventDto))
                        .thenReturn(true);

        mentorshipOfferedEventListener.onMessage(message, new byte[0]);

        Mockito.verify(postServices.get(0), Mockito.times(1))
                .isPreferredContact(mentorshipOfferedEventDto);
        Mockito.verify(postServices.get(1), Mockito.times(1))
                .isPreferredContact(mentorshipOfferedEventDto);
        Mockito.verify(postServices.get(0), Mockito.times(1))
                .send(mentorshipOfferedEventDto, text);
    }
}