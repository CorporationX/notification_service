package faang.school.notificationservice.messages.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.LikeEventDto;
import faang.school.notificationservice.messages.builder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class AbstractEventListenerTest {

    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private List<NotificationService> notificationServices;
    @Mock
    private MessageBuilder<LikeEventDto> messageBuilderMock;
    @Mock
    private List<MessageBuilder<LikeEventDto>> messageBuilders = new ArrayList<>();
    @Mock
    private ObjectMapper objectMapper = Mappers.getMapper(ObjectMapper.class);
    @InjectMocks
    private AbstractEventListener<LikeEventDto> likeEventListener;
    @Captor
    private Stream<LikeEventDto> likeEventDtoStream;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        messageBuilders.add(messageBuilderMock);
    }

    @Test
    void handleEvent_withDeserializationError() {

    }

}
