package faang.school.notificationservice.notification_sending_strategy;

import org.springframework.stereotype.Component;

@Component
public class ContextSend {
    private SendingNotification sendingNotification;

    public void setSendingNotification(SendingNotification sendingNotification) {
        this.sendingNotification = sendingNotification;
    }

    public void send(String email, String title, String messageText) {
        sendingNotification.sending(email, title, messageText);
    }
}
