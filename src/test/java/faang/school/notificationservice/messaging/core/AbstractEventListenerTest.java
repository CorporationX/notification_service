package faang.school.notificationservice.messaging.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.events.NewFollowerEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AbstractEventListenerTest {

    private static final Class<NewFollowerEvent> EVENT_CLASS = NewFollowerEvent.class;

    private static final long FID_ALICE = 1L;
    private static final long TID_ALICE = 2L;
    private static final String NAME_ALICE = "Alice";

    private static final long FID_BOB = 10L;
    private static final long TID_BOB = 20L;
    private static final String NAME_BOB = "Bob";

    private static final long USER_ID_PREF_PHONE = 99L;
    private static final String USER_PHONE = "+12025550123";
    private static final String MSG_HELLO = "hello";

    private static final long USER_ID_NO_SERVICE = 7L;

    private static final Locale LOCALE_EN = Locale.ENGLISH;
    private static final Locale LOCALE_RU = Locale.forLanguageTag("ru");

    private static final String INVALID_JSON = "{not-json}";

    @Mock
    UserServiceClient userClient;
    @Mock
    NotificationService sms;
    @Mock
    NotificationService email;
    @Mock
    MessageBuilder<NewFollowerEvent> builder;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private AbstractEventListener<NewFollowerEvent> listener;

    private void initListener(List<NotificationService> services, List<MessageBuilder<? extends NewFollowerEvent>> builders) {
        listener = new AbstractEventListener<>(
                MAPPER,
                userClient,
                services,
                builders
        ) {
            @Override
            protected Class<NewFollowerEvent> getEventType() {
                return EVENT_CLASS;
            }
        };
    }

    private void initBuilderDefaults() {
        doReturn(EVENT_CLASS).when(builder).getInstance();
        when(builder.buildMessage(any(NewFollowerEvent.class), any(Locale.class)))
                .thenAnswer(inv -> "MSG:" + ((NewFollowerEvent) inv.getArgument(0)).getFollowerDisplayName());
    }

    private void initDefaultListener() {
        initBuilderDefaults();
        initListener(List.of(sms, email), List.of(builder));
    }

    private static NewFollowerEvent newEvent(long followerId, long targetUserId, String followerName) {
        return new NewFollowerEvent(followerId, targetUserId, followerName);
    }

    @Test
    @DisplayName("MessageBuilder: builds text for NewFollowerEvent")
    void getMessage_usesProperBuilder() {
        initDefaultListener();

        var event = newEvent(FID_ALICE, TID_ALICE, NAME_ALICE);
        var msg = listener.getMessage(event, LOCALE_EN);

        assertEquals("MSG:" + NAME_ALICE, msg);
        verify(builder).buildMessage(eq(event), any(Locale.class));

        verifyNoInteractions(userClient, sms, email);
        verifyNoMoreInteractions(builder);
    }

    @Test
    @DisplayName("MessageBuilder: passes exact Locale to builder")
    void getMessage_passesLocale() {
        initDefaultListener();

        var event = newEvent(FID_BOB, TID_BOB, NAME_BOB);
        listener.getMessage(event, LOCALE_RU);

        verify(builder).buildMessage(eq(event), eq(LOCALE_RU));
        verifyNoInteractions(userClient, sms, email);
        verifyNoMoreInteractions(builder);
    }

    @Test
    @DisplayName("NotificationService: selects SMS based on user preference")
    void sendNotification_picksCorrectServiceByPreference() {
        when(sms.getPreferredContact()).thenReturn(UserDto.PreferredContact.PHONE);

        initListener(List.of(sms, email), List.of(builder));

        var user = new UserDto(USER_ID_PREF_PHONE, USER_PHONE, UserDto.PreferredContact.PHONE);
        when(userClient.getUser(USER_ID_PREF_PHONE)).thenReturn(user);

        listener.sendNotification(USER_ID_PREF_PHONE, MSG_HELLO);

        verify(userClient).getUser(USER_ID_PREF_PHONE);
        verify(sms).send(eq(user), eq(MSG_HELLO));
        verify(email, never()).send(any(), any());
        verifyNoMoreInteractions(userClient, sms, email);
        verifyNoInteractions(builder);
    }

    @Test
    @DisplayName("Event deserialization: JSON → NewFollowerEvent")
    void readEvent_deserializes() throws Exception {
        // билдер/сервисы не требуются, но listener нужен
        initListener(List.of(sms, email), List.of(builder));

        var json = MAPPER.writeValueAsString(newEvent(FID_BOB, TID_BOB, NAME_BOB));
        var e = listener.readEvent(json);

        assertEquals(NAME_BOB, e.getFollowerDisplayName());
        verifyNoInteractions(builder, userClient, sms, email);
    }

    @Test
    @DisplayName("getMessage throws when no MessageBuilder matches event type")
    void getMessage_throwsIfNoBuilder() {
        initListener(List.of(sms, email), List.of()); // без билдеров

        var event = newEvent(FID_ALICE, TID_ALICE, "X");
        assertThrows(IllegalStateException.class, () -> listener.getMessage(event, LOCALE_EN));

        verifyNoInteractions(builder, userClient, sms, email);
    }

    @Test
    @DisplayName("sendNotification throws if no NotificationService matches preference")
    void sendNotification_noServiceForPreference() {
        initListener(List.of(), List.of(builder)); // без сервисов

        var user = new UserDto(USER_ID_NO_SERVICE, "+10000000000", UserDto.PreferredContact.PHONE);
        when(userClient.getUser(USER_ID_NO_SERVICE)).thenReturn(user);

        assertThrows(IllegalStateException.class, () -> listener.sendNotification(USER_ID_NO_SERVICE, "msg"));

        verify(userClient).getUser(USER_ID_NO_SERVICE);
        verifyNoMoreInteractions(userClient);
        verifyNoInteractions(builder, sms, email);
    }

    @Test
    @DisplayName("readEvent throws on invalid JSON")
    void readEvent_invalidJson() {
        initListener(List.of(sms, email), List.of(builder));
        assertThrows(IllegalArgumentException.class, () -> listener.readEvent(INVALID_JSON));
        verifyNoInteractions(builder, userClient, sms, email);
    }
}