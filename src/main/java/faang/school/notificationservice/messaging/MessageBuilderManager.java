package faang.school.notificationservice.messaging;



import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageBuilderManager {


    private final List<NotificationChannel> notificationChannels;
    private final MentorshipOfferedMessageBuilder messageBuilder;

    public MentorshipOfferedMessageManager(List<NotificationChannel> notificationChannels,
                                           MentorshipOfferedMessageBuilder messageBuilder) {
        this.notificationChannels = notificationChannels;
        this.messageBuilder = messageBuilder;
    }

    public void sendMessage(MentorshipOfferedEvent event, ContactPreference preference, String recipient) {
        // Build the message
        String message = messageBuilder.buildMessage(event, Locale.ENGLISH);

        // Find the appropriate channel
        NotificationChannel channel = notificationChannels.stream()
                .filter(c -> c.supports(preference))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No channel supports the preference: " + preference));

        // Send the message
        channel.sendMessage(recipient, message);
    }
}