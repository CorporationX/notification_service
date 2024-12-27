package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.event.ProfileViewEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;

import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileViewEventListenerTest {

    private ProfileViewEventListener listener;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MessageBuilder<ProfileViewEvent> messageBuilder;

    @Mock
    private NotificationService notificationService;

    @Mock
    private Message message;

    @BeforeEach
    void setUp() {
        listener = new ProfileViewEventListener(objectMapper, userServiceClient, messageBuilder, List.of(notificationService));
    }

    @Test
    void testOnMessage() {
        ProfileViewEvent event = new ProfileViewEvent();
        event.setViewedId(1L);
        UserContactsDto userContactsDto = new UserContactsDto();
        userContactsDto.setId(1L);

        listener.onMessage(message, null);

        verify(notificationService, never()).send(any(), any());

        assertNotNull(userContactsDto);
        assertEquals(1L, userContactsDto.getId());
    }

    @Test
    void testGetUserContacts_Success() {
        UserContactsDto userContactsDto = new UserContactsDto();
        userContactsDto.setId(1L);
        userContactsDto.setEmail("faang@google.com");

        when(userServiceClient.getUserContacts(1L)).thenReturn(userContactsDto);

        UserContactsDto result = listener.getUserContacts(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("faang@google.com", result.getEmail());
    }

    @Test
    void testGetUserContacts_Failure() {
        long userId = 1L;

        Map<String, Collection<String>> headers = Collections.singletonMap("Authorization", Collections.singletonList("Bearer your_token_here"));

        Request request = Request.create(
                Request.HttpMethod.GET,
                "http://example.com/user/" + userId,
                headers,
                null,
                StandardCharsets.UTF_8,
                null
        );

        FeignException feignException = new FeignException.NotFound("User not found", request, null, null);

        when(userServiceClient.getUserContacts(userId)).thenThrow(feignException);

        assertThrows(FeignException.class, () -> listener.getUserContacts(userId));
    }
}
