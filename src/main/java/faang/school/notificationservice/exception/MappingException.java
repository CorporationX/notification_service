package faang.school.notificationservice.exception;

import lombok.Getter;
import org.springframework.data.redis.connection.Message;

@Getter
public class MappingException extends RuntimeException {

    public MappingException(String eventTypeName, Message message, Exception e) {
        super(MessageError.UNABLE_TO_PARSE_EVENT.getMessage(eventTypeName, message), e);
    }
}
