package faang.school.notificationservice.config.message;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "builder")
public class BuilderProperties {

    private NewCommentEventOpt newCommentEventOpt;

    @Getter
    @Setter
    public static class NewCommentEventOpt {
        private int startMessageIndex;
        private int lastMessageIndex;
    }
}
