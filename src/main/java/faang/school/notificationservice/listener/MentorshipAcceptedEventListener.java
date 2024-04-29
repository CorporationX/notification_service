package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.messagebroker.MentorshipAcceptedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
public class MentorshipAcceptedEventListener extends AbstractEventListener<MentorshipAcceptedEvent> {
    @Value("${spring.data.redis.channels.mentorship_accepted_channel.name}")
    private ChannelTopic mentorshipAcceptedChannel;

    public MentorshipAcceptedEventListener(ObjectMapper objectMapper, Class<MentorshipAcceptedEvent> type, UserServiceClient userServiceClient) {
        super(objectMapper, type, userServiceClient);
    }

    public void workingEvent(MentorshipAcceptedEvent event) {
        UserDto user = userServiceClient.getUser(event.getIdRequestAuthor());
        String messageText = "Your mentorship request: " + event.getIdRequestRecipient() + " have been registered";
    }
}
