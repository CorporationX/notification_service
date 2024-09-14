package faang.school.notificationservice.validator;

import faang.school.notificationservice.exception.DataValidationException;
import org.springframework.stereotype.Component;


@Component
public class UserValidator {

    public void checkEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new DataValidationException("User must have an email");
        }
    }
}
