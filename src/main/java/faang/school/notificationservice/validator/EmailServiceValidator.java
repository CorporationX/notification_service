package faang.school.notificationservice.validator;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.DataNotificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailServiceValidator {

    public void checkUserDtoIsNull(UserDto userDto) {
        if (userDto == null) {
            log.error("The userDto field cannot be null");
            throw new DataNotificationException("The userDto field cannot be null");
        }
    }

    public void checkMessageIsNull(String message) {
        if (message == null) {
            log.error("The message field cannot be null");
            throw new DataNotificationException("The message field cannot be null");
        }
    }

    public void checkPathIsNull(String path) {
        if (path == null) {
            log.error("The path field cannot be null");
            throw new DataNotificationException("The path field cannot be null");
        }
    }
}