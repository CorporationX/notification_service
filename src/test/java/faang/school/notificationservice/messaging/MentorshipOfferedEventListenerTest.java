package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.MentorshipOfferRequestSentDto;
import faang.school.notificationservice.util.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.data.redis.connection.Message;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MentorshipOfferedEventListenerTest {

    @InjectMocks
    private MentorshipOfferedEventListener mentorshipOfferedEventListener;

    @Mock
    private Message message;

    @Mock
    private AbstractionEventListener<MentorshipOfferRequestSentDto> abstractionEventListener;
    @Mock
    private JsonMapper jsonMapper;

    private MentorshipOfferRequestSentDto mentorshipOffer;

    @BeforeEach
    void setUp() {
        mentorshipOffer = new MentorshipOfferRequestSentDto();
        mentorshipOffer.setId(1L);
        mentorshipOffer.setReceiverId(2L);
        mentorshipOffer.setRequesterId(3L);
    }

    @Test
    void onMessage() {
        Mockito.when(message.toString()).thenReturn("s");

        Mockito.when(jsonMapper.toObject(Mockito.anyString(), Mockito.eq(MentorshipOfferRequestSentDto.class)))
                .thenReturn(Optional.of(mentorshipOffer));

        OngoingStubbing<String> stringOngoingStubbing = Mockito.when(abstractionEventListener.getMessage(mentorshipOffer, Locale.UK))
                .thenReturn(Mockito.anyString());


        mentorshipOfferedEventListener.onMessage(message, "".getBytes());

        Mockito.verify(abstractionEventListener, Mockito.times(1))
                .sendNotification(mentorshipOffer.getId(), stringOngoingStubbing.toString());
    }
}