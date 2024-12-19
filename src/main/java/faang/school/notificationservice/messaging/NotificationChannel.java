package faang.school.notificationservice.messaging;

import faang.school.notificationservice.model.ContactPreference;

public interface NotificationChannel {
    boolean supports(ContactPreference preference);
    void sendMessage(String recipient, String message);
}
