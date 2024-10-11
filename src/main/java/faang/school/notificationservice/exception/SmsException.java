package faang.school.notificationservice.exception;

import com.vonage.client.sms.MessageStatus;

public class SmsException extends RuntimeException {
    private MessageStatus messageStatus;

    public SmsException(String message, MessageStatus messageStatus) {
        super(message);
        this.messageStatus = messageStatus;
    }
}
