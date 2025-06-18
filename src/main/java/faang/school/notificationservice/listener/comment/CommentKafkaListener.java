package faang.school.notificationservice.listener.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.PostServiceClient;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.kafka.KafkaCommentTopicConfigurationProperties;
import faang.school.notificationservice.dto.client.post_service.CommentClientResponseDto;
import faang.school.notificationservice.dto.client.post_service.PostClientResponseDto;
import faang.school.notificationservice.dto.client.user_service.UserClientResponseDto;
import faang.school.notificationservice.event.comment.CommentEventDto;
import faang.school.notificationservice.listener.AbstractKafkaListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.model.comment.CommentNewModel;
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
public class CommentKafkaListener extends AbstractKafkaListener<CommentEventDto, CommentNewModel> {
    private final PostServiceClient postServiceClient;
    private final UserServiceClient userServiceClient;
    private final KafkaCommentTopicConfigurationProperties props;

    public CommentKafkaListener(List<NotificationService> notificationServices,
                                List<MessageBuilder<CommentNewModel>> messageBuilders,
                                UserServiceClient userServiceClient,
                                PostServiceClient postServiceClient,
                                KafkaCommentTopicConfigurationProperties props,
                                ObjectMapper objectMapper) {
        super(notificationServices, messageBuilders, objectMapper,
                CommentEventDto.class, CommentNewModel.class);
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
        List<UserClientResponseDto> users = userServiceClient.getUsersByIds(userIds);
        Map<Long, UserClientResponseDto> userMap = users.stream()
                .collect(Collectors.toMap(UserClientResponseDto::getId, Function.identity()));

        UserClientResponseDto authorPost = userMap.get(commentEventDto.getAuthorPostId());
        log.debug("Got post author: {}", authorPost);

        UserClientResponseDto authorComment = userMap.get(commentEventDto.getAuthorId());
        log.debug("Got comment author: {}", authorComment);

        PostClientResponseDto post = postServiceClient.getPostById(commentEventDto.getPostId());
        log.debug("Got post: {}", post);

        CommentClientResponseDto comment = postServiceClient.getCommentById(commentEventDto.getId());
        log.debug("Got comment: {}", comment);

        CommentNewModel commentNewModel = getCommentNewModel(comment, authorComment, post);
        String text = getMessage(commentNewModel, authorPost.getLocale());

        sendNotification(authorPost, text);
    }

    private CommentNewModel getCommentNewModel(CommentClientResponseDto comment, UserClientResponseDto authorComment, PostClientResponseDto post) {
        return CommentNewModel.builder()
                .commentContent(comment.getContent())
                .usernameAuthorComment(authorComment.getUsername())
                .postTitle(post.getTitle())
                .build();
    }
}
