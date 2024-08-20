package faang.school.notificationservice.validator;

import faang.school.notificationservice.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.control.MappingControl;
import org.springframework.stereotype.Component;

import java.rmi.NoSuchObjectException;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class EmailServiceValidator {
    private static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public void validationParam(UserDto userDto, String message){
        checkUserDto(userDto);
        checkEmail(userDto.getEmail());
        checkMassage(message);
    }

    private void checkUserDto(UserDto userDto) {
        if (userDto == null){
            String msg = "Receiver is null! exception";
            log.error(msg);
            throw new NoSuchElementException(msg);
        }
    }

    private void checkEmail(String email){
        if(email.isBlank()){
            String msg = "email is empty";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            String msg = "Incorrect email address";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
    }

    private void checkMassage(String message){
        if(message.isBlank()){
            String msg = "message is empty";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
    }

}
