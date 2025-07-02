package faang.school.notificationservice.client;

import faang.school.notificationservice.config.client.feign.FeignClientConfig;
import faang.school.notificationservice.dto.client.post_service.CommentDto;
import faang.school.notificationservice.dto.client.post_service.PostDto;
import feign.FeignException;
import feign.RetryableException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "post-service",
        url = "${services.post-service.host}:${services.post-service.port}",
        path = "/api/v1",
        configuration = FeignClientConfig.class)
public interface PostServiceClient {
    @Retryable(
            retryFor = { FeignException.class, RetryableException.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    @GetMapping("/comments/{commentId}")
    CommentDto getCommentById(@PathVariable long commentId);
    @Retryable(
            retryFor = { FeignException.class, RetryableException.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    @GetMapping("/posts/{postId}")
    PostDto getPostById(@PathVariable long postId);
}
