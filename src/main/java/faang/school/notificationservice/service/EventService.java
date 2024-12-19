package faang.school.notificationservice.service;

import faang.school.notificationservice.client.ContactPreferenceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MentorshipOfferedMessageBuilder;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.messaging.MessageBuilderManager;
import faang.school.notificationservice.model.ContactPreference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService implements NotificationService {

    private final ContactPreferenceClient contactPreferenceClient;
    private final MessageBuilderManager messageBuilderManager;
  private final MentorshipOfferedMessageBuilder builder = new MentorshipOfferedMessageBuilder();
    MentorshipOfferedEvent event = new MentorshipOfferedEvent("John Doe", "Jane Smith");

    String messageEnglish = builder.buildMessage(event, Locale.ENGLISH);
    String messageFrench = builder.buildMessage(event, Locale.FRENCH);

    @Override
    public void send(UserDto user, String message) {

    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return null;
    }

    public void mentorshipOffered(long idRequest, long idAuthor, long idRequester) {

        String preferredChannel = String.valueOf(contactPreferenceClient.getContactPreferenceByUserId(idRequester));
        String message = messageBuilderManager.buildMessageForEvent(event);

        // Получить нужный канал отправки уведомлений
        NotificationChannel channel = notificationChannelFactory.getChannel(preferredChannel);

        // Отправить сообщение
        channel.send(userId, message);
    }
}