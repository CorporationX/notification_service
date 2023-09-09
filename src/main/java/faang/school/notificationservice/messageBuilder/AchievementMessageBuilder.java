package faang.school.notificationservice.messageBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.achievement.DtoUserEventAchievement;
import faang.school.notificationservice.dto.user.UserDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class AchievementMessageBuilder implements MessageBuilder<DtoUserEventAchievement, String> {
    private final UserServiceClient userServiceClient;
    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    private Map<String, Map<String, String>> map;
    @Getter
    private UserDto userDto;

    @Override
    public String buildMessage(DtoUserEventAchievement event, Locale locale, String argument) {
        userDto = userServiceClient.getUser(event.getUserId());
        try {
            map = mapper.readValue(new File("src/main/resources/messages.yaml"), Map.class);
        } catch (IOException ignored) {
        }

        return String.format(map.get("achievement").get("start"), userDto.getUsername(), event.getAchievement().getTitle()
                , event.getAchievement().getDescription(), event.getAchievement().getRarity());
    }

    @Override
    public Class<?> getEventType() {
        return null;
    }
}
