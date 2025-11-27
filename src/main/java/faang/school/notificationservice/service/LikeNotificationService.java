package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.like.LikeEventDto;
import faang.school.notificationservice.dto.like.UnlikeEventDto;
import faang.school.notificationservice.listner.AbstractEventListener;
import faang.school.notificationservice.messaging.LikeMessageBuilder;
import faang.school.notificationservice.messaging.UnlikeMessageBuilder;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class LikeNotificationService {
    private final UserServiceClient userServiceClient;
    private final AbstractEventListener abstractEventListener;
    private final LikeMessageBuilder likeMessageBuilder;
    private final UnlikeMessageBuilder unlikeMessageBuilder;

    public void processLikeEvent(LikeEventDto likeEventDto) {
        log.info("Processing like event: postId={}, likeAuthorId={}, postAuthorId={}",
                likeEventDto.postId(), likeEventDto.likeAuthorId(), likeEventDto.postAuthorId());

        if (likeEventDto == null || likeEventDto.postAuthorId() == null) {
            throw new IllegalArgumentException("Invalid like event data");
        }
        UserDto postAuthor = userServiceClient.getById(likeEventDto.postAuthorId());
        if (postAuthor == null) {
            throw new EntityNotFoundException(
                    String.format("Author of post with ID %d not found", likeEventDto.postAuthorId()));
        }
        postAuthor.setPreference(UserDto.PreferredContact.EMAIL);
        String message = likeMessageBuilder.buildMessage(
                likeEventDto,
                Locale.getDefault()
        );

        abstractEventListener.sendNotification(postAuthor, message);
    }

    public void processUnlikeEvent(UnlikeEventDto unlikeEventDto) {
        log.info("Processing unlike event: postId={}, likeAuthorId={}, postAuthorId={}",
                unlikeEventDto.postId(), unlikeEventDto.likeAuthorId(), unlikeEventDto.postAuthorId());

        if (unlikeEventDto == null || unlikeEventDto.postAuthorId() == null) {
            throw new IllegalArgumentException("Invalid like event data");
        }
        UserDto postAuthor = userServiceClient.getById(unlikeEventDto.postAuthorId());
        if (postAuthor == null) {
            throw new EntityNotFoundException(
                    String.format("Author of post with ID %d not found", unlikeEventDto.postAuthorId()));
        }
        postAuthor.setPreference(UserDto.PreferredContact.EMAIL);
        String message = unlikeMessageBuilder.buildMessage(
                unlikeEventDto,
                Locale.getDefault()
        );
        abstractEventListener.sendNotification(postAuthor, message);
    }
}