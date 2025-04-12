package faang.school.notificationservice.messaging.like;

import faang.school.notificationservice.client.UserServiceClient;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class LikeMessageBuilder extends AbstractLikeMessageBuilder {

    public static final String LIKE_EVENT = "like.post.new";

    public LikeMessageBuilder(MessageSource messageSource,
                              UserServiceClient userServiceClient) {
        super(messageSource, userServiceClient);
    }

    @Override
    protected String getMessageCode() {
        return LIKE_EVENT;
    }
}
