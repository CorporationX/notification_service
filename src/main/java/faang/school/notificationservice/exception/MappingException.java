package faang.school.notificationservice.exception;

import lombok.Getter;
import org.springframework.data.redis.connection.Message;

@Getter
public class MappingException extends RuntimeException {

    public MappingException(String eventTypeName, Message message, Exception e) {
        super(String.format("Unable to parse event: %s with message: %s.", eventTypeName, message), e);
    }
}
