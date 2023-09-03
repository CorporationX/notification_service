package faang.school.notificationservice.notification;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.skill.SkillOfferEvent;
import faang.school.notificationservice.service.SkillOfferedMessageBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkillOfferedNotificationSenderTest {
    @Mock
    SkillOfferedMessageBuilder messageBuilder;
    @Mock
    List<NotificationService> notificationServices;
    @InjectMocks
    SkillOfferedNotificationSender sender;

    @ParameterizedTest
    @MethodSource("getPreferredContacts")
    @DisplayName("Successfully send a skill offer notification")
    void successfullySendASkillOfferNotification(UserDto.PreferredContact preferredContact) {
        NotificationService service = mock(NotificationService.class);
        when(service.getPreferredContact())
                .thenReturn(preferredContact);
        SkillOfferEvent event = SkillOfferEvent.builder()
                .skillId(1L)
                .receiverId(2L)
                .senderId(3L)
                .build();
        UserDto receiver = UserDto.builder()
                .id(2L)
                .preference(preferredContact)
                .build();

        when(messageBuilder.build(event, Locale.UK))
                .thenReturn("User {0} offered you skill {1}");
        when(notificationServices.stream())
                .thenReturn(Stream.of(service));

        sender.send(event, receiver);

        verify(service).send(receiver, "User {0} offered you skill {1}");
    }

    private static Stream<Arguments> getPreferredContacts() {
        return Stream.of(
                Arguments.of(UserDto.PreferredContact.EMAIL),
                Arguments.of(UserDto.PreferredContact.SMS),
                Arguments.of(UserDto.PreferredContact.TELEGRAM)
        );
    }
}