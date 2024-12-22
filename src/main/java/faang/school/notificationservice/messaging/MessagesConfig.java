package faang.school.notificationservice.messaging;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "messages")
public class MessagesConfig {
    private Map<String, Map<String, String>> mentorshipOffered;

    public Map<String, Map<String, String>> getMentorshipOffered() {
        return mentorshipOffered;
    }

    public void setMentorshipOffered(Map<String, Map<String, String>> mentorshipOffered) {
        this.mentorshipOffered = mentorshipOffered;
    }
}