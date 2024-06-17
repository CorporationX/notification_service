package faang.school.notificationservice.exception;

public class MessageSendingException extends RuntimeException{
    public MessageSendingException(String message){
        super(message);
    }
}
