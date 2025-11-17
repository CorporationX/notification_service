package faang.school.notificationservice.messaging.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.events.NewFollowerEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import feign.FeignException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    private static final String USER_LOCALE_EN = "en";
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

    private void initListener(List<NotificationService> services,
                              List<MessageBuilder<?>> builders) {
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
                .thenAnswer(inv -> "MSG:" + ((NewFollowerEvent) inv.getArgument(0)).getKey());
    }

    private void initDefaultListener() {
        initBuilderDefaults();
        when(sms.getPreferredContact()).thenReturn(UserDto.PreferredContact.PHONE);
        when(email.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        initListener(List.of(sms, email), List.of(builder));
    }

    private static NewFollowerEvent newEvent(long followerId, long targetUserId, String followerName) {
        return new NewFollowerEvent(followerId, targetUserId, followerName);
    }

    private static UserDto newUser(long id, String phone, String locale, String preference) {
        return new UserDto(
                id,
                null,
                null,
                phone,
                null,
                locale,
                preference
        );
    }

    @Test
    @DisplayName("MessageBuilder: builds text for NewFollowerEvent")
    void getMessage_usesProperBuilder() {
        initDefaultListener();

        var event = newEvent(FID_ALICE, TID_ALICE, NAME_ALICE);
        var msg = listener.getMessage(event, LOCALE_EN);

        assertEquals("MSG:" + event.getKey(), msg);
        verify(builder).buildMessage(eq(event), any(Locale.class));

        verifyNoInteractions(userClient);
        verifyNoMoreInteractions(builder, sms, email);
    }

    @Test
    @DisplayName("MessageBuilder: passes exact Locale to builder")
    void getMessage_passesLocale() {
        initDefaultListener();

        var event = newEvent(FID_BOB, TID_BOB, NAME_BOB);
        listener.getMessage(event, LOCALE_RU);

        verify(builder).buildMessage(eq(event), eq(LOCALE_RU));
        verifyNoInteractions(userClient);
        verifyNoMoreInteractions(builder, sms, email);
    }

    @Test
    @DisplayName("getMessage throws when no MessageBuilder matches event type")
    void getMessage_throwsIfNoBuilder() {

        initListener(List.of(), List.of());

        var event = newEvent(FID_ALICE, TID_ALICE, NAME_ALICE);
        assertThrows(IllegalStateException.class, () -> listener.getMessage(event, LOCALE_EN));

        verifyNoInteractions(builder, userClient, sms, email);
    }

    @Test
    @DisplayName("Event deserialization: JSON → NewFollowerEvent")
    void readEvent_deserializes() throws Exception {

        initListener(List.of(), List.of());

        var json = MAPPER.writeValueAsString(newEvent(FID_BOB, TID_BOB, NAME_BOB));
        var e = listener.readEvent(json);

        assertEquals(NAME_BOB, e.eventType());
        assertEquals(FID_BOB, e.actorId());
        assertEquals(TID_BOB, e.receiverId());

        verifyNoInteractions(builder, userClient, sms, email);
    }

    @Test
    @DisplayName("readEvent throws on invalid JSON")
    void readEvent_invalidJson() {
        initListener(List.of(), List.of());
        assertThrows(IllegalArgumentException.class, () -> listener.readEvent(INVALID_JSON));
        verifyNoInteractions(builder, userClient, sms, email);
    }

    @Test
    @DisplayName("NotificationService: selects SMS based on user preference")
    void sendNotification_picksCorrectServiceByPreference() {

        when(sms.getPreferredContact()).thenReturn(UserDto.PreferredContact.PHONE);
        when(email.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        initListener(List.of(sms, email), List.of());

        var user = newUser(
                USER_ID_PREF_PHONE,
                USER_PHONE,
                USER_LOCALE_EN,
                "phone"
        );

        listener.sendNotification(user, MSG_HELLO);

        verify(sms).send(eq(user), eq(MSG_HELLO));
        verify(email, never()).send(any(), any());
        verifyNoMoreInteractions(sms, email);
        verifyNoInteractions(userClient, builder);
    }

    @Test
    @DisplayName("sendNotification throws if no NotificationService matches preference")
    void sendNotification_noServiceForPreference() {

        initListener(List.of(), List.of());

        var user = newUser(USER_ID_NO_SERVICE, "+10000000000", USER_LOCALE_EN, "phone");

        assertThrows(IllegalStateException.class, () -> listener.sendNotification(user, "msg"));

        verifyNoInteractions(userClient, builder, sms, email);
    }

    @Test
    @DisplayName("sendNotification throws if preference is invalid")
    void sendNotification_invalidPreference() {
        when(sms.getPreferredContact()).thenReturn(UserDto.PreferredContact.PHONE);

        initListener(List.of(sms), List.of());

        var user = newUser(USER_ID_PREF_PHONE, USER_PHONE, USER_LOCALE_EN, "INVALID");

        assertThrows(IllegalStateException.class, () -> listener.sendNotification(user, MSG_HELLO));

        verifyNoInteractions(userClient, builder);
        verify(sms, never()).send(any(), any());
        verify(email, never()).send(any(), any());
    }

    @Test
    @DisplayName("loadUser delegates to userClient and returns DTO")
    void loadUser_success() {

        initListener(List.of(), List.of());

        var user = newUser(USER_ID_PREF_PHONE, USER_PHONE, USER_LOCALE_EN, "phone");
        when(userClient.getUser(USER_ID_PREF_PHONE)).thenReturn(user);

        var loaded = listener.loadUser(USER_ID_PREF_PHONE);

        assertSame(user, loaded);
        verify(userClient).getUser(USER_ID_PREF_PHONE);
        verifyNoMoreInteractions(userClient);
        verifyNoInteractions(builder, sms, email);
    }

    @Test
    @DisplayName("loadUser throws UserNotFoundException on 404")
    void loadUser_notFound() {
        initListener(List.of(), List.of());

        var feign404 = FeignException.errorStatus(
                "GET /users/7",
                feign.Response.builder()
                        .status(404)
                        .reason("Not Found")
                        .request(feign.Request.create(
                                feign.Request.HttpMethod.GET,
                                "/users/7",
                                java.util.Collections.emptyMap(),
                                null,
                                StandardCharsets.UTF_8,
                                null
                        ))
                        .build()
        );

        when(userClient.getUser(USER_ID_NO_SERVICE)).thenThrow(feign404);

        assertThrows(faang.school.notificationservice.error.UserNotFoundException.class,
                () -> listener.loadUser(USER_ID_NO_SERVICE));

        verify(userClient).getUser(USER_ID_NO_SERVICE);
        verifyNoMoreInteractions(userClient);
        verifyNoInteractions(builder, sms, email);
    }

    @Test
    @DisplayName("loadUser throws IllegalStateException on non-404 FeignException")
    void loadUser_otherFeignError() {
        initListener(List.of(), List.of());

        var feign500 = FeignException.errorStatus(
                "GET /users/500",
                feign.Response.builder()
                        .status(500)
                        .reason("Internal Server Error")
                        .request(feign.Request.create(
                                feign.Request.HttpMethod.GET,
                                "/users/500",
                                java.util.Collections.emptyMap(),
                                null,
                                StandardCharsets.UTF_8,
                                null
                        ))
                        .build()
        );

        when(userClient.getUser(USER_ID_PREF_PHONE)).thenThrow(feign500);

        assertThrows(IllegalStateException.class,
                () -> listener.loadUser(USER_ID_PREF_PHONE));

        verify(userClient).getUser(USER_ID_PREF_PHONE);
        verifyNoMoreInteractions(userClient);
        verifyNoInteractions(builder, sms, email);
    }
}
