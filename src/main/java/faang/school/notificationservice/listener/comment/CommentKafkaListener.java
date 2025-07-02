package faang.school.notificationservice.listener.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.PostServiceClient;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.kafka.KafkaCommentTopicProperties;
import faang.school.notificationservice.dto.client.post_service.CommentDto;
import faang.school.notificationservice.dto.client.post_service.PostDto;
import faang.school.notificationservice.dto.client.user_service.UserDto;
import faang.school.notificationservice.model.kafka.comment.CommentEventDto;
import faang.school.notificationservice.listener.AbstractKafkaListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.model.kafka.comment.CommentMessage;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CommentKafkaListener extends AbstractKafkaListener<CommentEventDto, CommentMessage> {
    private final PostServiceClient postServiceClient;
    private final UserServiceClient userServiceClient;
    private final KafkaCommentTopicProperties props;

    public CommentKafkaListener(List<NotificationService> notificationServices,
                                List<MessageBuilder<CommentMessage>> messageBuilders,
                                UserServiceClient userServiceClient,
                                PostServiceClient postServiceClient,
                                KafkaCommentTopicProperties props,
                                ObjectMapper objectMapper) {
        super(notificationServices, messageBuilders, objectMapper,
                CommentEventDto.class, CommentMessage.class);
        this.userServiceClient = userServiceClient;
        this.postServiceClient = postServiceClient;
        this.props = props;
    }

    @KafkaListener(
            topics = "${spring.data.kafka.topic.comment.name}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenCommentTopic(String message) {
        CommentEventDto commentEventDto = getEvent(message);
        log.info("Received a message from {}: {}", props.getName(), commentEventDto);
        List<Long> userIds = List.of(commentEventDto.getAuthorPostId(), commentEventDto.getAuthorId());
        List<UserDto> users = userServiceClient.getUsersByIds(userIds);
        Map<Long, UserDto> userMap = users.stream()
                .collect(Collectors.toMap(UserDto::getId, Function.identity()));

        UserDto authorPost = userMap.get(commentEventDto.getAuthorPostId());
        log.info("Got post author: {}", authorPost);

        UserDto authorComment = userMap.get(commentEventDto.getAuthorId());
        log.info("Got comment author: {}", authorComment);

        PostDto post = postServiceClient.getPostById(commentEventDto.getPostId());
        log.info("Got post: {}", post);

        CommentDto comment = postServiceClient.getCommentById(commentEventDto.getId());
        log.info("Got comment: {}", comment);

        CommentMessage commentMessage = getCommentNewModel(comment, authorComment, post);
        String text = getMessage(commentMessage, authorPost.getLocale());

        sendNotification(authorPost, text);
    }

    private CommentMessage getCommentNewModel(CommentDto comment, UserDto authorComment, PostDto post) {
        return CommentMessage.builder()
                .commentContent(comment.getContent())
                .usernameAuthorComment(authorComment.getUsername())
                .postTitle(post.getTitle())
                .build();
    }
}
