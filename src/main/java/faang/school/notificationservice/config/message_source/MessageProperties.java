package faang.school.notificationservice.config.message_source;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.messages")
public class MessageProperties {

    private String basename;
    private Integer cacheseconds;
    private String propertyFollower;
    private String propertyMentorshipOffered;
    private String propertyMentorshipAccepted;
    private String propertyRecommendation;
    private String propertyUserViewProfile;

}
