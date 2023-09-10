package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.SkillOfferDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class SkillOfferedEventListener extends AbstractEventListener<SkillOfferDto>{

    public SkillOfferedEventListener(ObjectMapper objectMapper,
                                     UserServiceClient userServiceClient,
                                     List<MessageBuilder<SkillOfferDto>> messageBuilder,
                                     List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilder, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        SkillOfferDto skillOfferDto = readValue(message.getBody(), SkillOfferDto.class);
        String notification = getMessage(skillOfferDto, Locale.getDefault());
        UserDto user = userServiceClient.getUser(skillOfferDto.getReceiverId());
        sendNotification(user, notification);
        log.info("Sending skill offer notification: {}", skillOfferDto);
    }
}