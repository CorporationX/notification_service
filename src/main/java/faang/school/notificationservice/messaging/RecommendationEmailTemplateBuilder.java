package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationEmailTemplateBuilder implements MessageBuilder<UserDto>{

    @Override
    public Class<?> getInstance() {
        return UserDto.class;
    }

    @Override
    public String buildMessage(UserDto event, Locale locale) {
        return String.format("Привет, %s! Тебе оставили рекомендацию.", event.getUsername());
    }

    public String buildSubject(UserDto event){
        return "Вам оставили рекомендацию";
    }
}