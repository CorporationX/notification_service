package faang.school.notificationservice.messaging.like;

import faang.school.notificationservice.client.UserServiceClient;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class DeleteLikeMessageBuilder extends AbstractLikeMessageBuilder {

    public static final String NOT_LIKE_EVENT = "delete.like.post";

    public DeleteLikeMessageBuilder(MessageSource messageSource,
                                    UserServiceClient userServiceClient) {
        super(messageSource, userServiceClient);
    }

    @Override
    protected String getMessageCode() {
        return NOT_LIKE_EVENT;
    }
}
