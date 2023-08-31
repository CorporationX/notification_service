package faang.school.notificationservice.service.email;

import faang.school.notificationservice.exception.DataValidationException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.regex.Pattern;

import static faang.school.notificationservice.commonMessage.ErrorMessageForEmailService.EMAIL_ADDRESS_IS_NULL;
import static faang.school.notificationservice.commonMessage.ErrorMessageForEmailService.EMAIL_NOT_VALID_FORMAT;
import static java.util.Objects.isNull;

@Component
public class EmailValidator {
    public void checkEmail(String email) {
        isExist(email);
        isEmail(email);
    }

    private void isExist(String email) {
        if (isNull(email)) {
            throw new DataValidationException(EMAIL_ADDRESS_IS_NULL);
        }
    }

    private void isEmail(String email) {
        if (!matchingWithEmailPattern(email)) {
            String errorMessage = MessageFormat.format(EMAIL_NOT_VALID_FORMAT, email);
            throw new DataValidationException(errorMessage);
        }
    }

    private boolean matchingWithEmailPattern(String email) {
        String emailPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"; // возможно есть смысл вынести шаблон

        return Pattern.compile(emailPattern)
                .matcher(email)
                .matches();
    }
}
