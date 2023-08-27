package faang.school.notificationservice.exception;

import com.vonage.client.sms.MessageStatus;

public class MessageSendingException extends RuntimeException {

    public MessageSendingException(String message, MessageStatus messageStatus) {
        super(message);
    }
}
