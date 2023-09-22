package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.redis.ProjectFollowerEventDto;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messageBuilder.ProjectFollowerMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FollowerProjectListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final ProjectFollowerMessageBuilder projectFollowerMessageBuilder;
    private final List<NotificationService> notificationServices;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ProjectFollowerEventDto event = objectMapper.readValue(message.getBody(), ProjectFollowerEventDto.class);
            UserDto userDto = userServiceClient.getUser(event.getOwnerId());
            String text = projectFollowerMessageBuilder.buildMessage(event, LocaleContextHolder.getLocale());
            notificationServices.stream()
                    .filter(service -> service.getPreferredContact() == userDto.getPreference())
                    .findFirst()
                    .ifPresent(service -> service.send(userDto, text));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
