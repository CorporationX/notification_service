package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.post.CommentDto;
import faang.school.notificationservice.dto.post.LikeDto;
import faang.school.notificationservice.dto.post.PostDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "post-service", url = "${post-service.host}:${post-service.port}")
public interface PostServiceClient {

    @GetMapping("api/v1/posts")
    PostDto getPost(@RequestParam long postId);

    @GetMapping("api/v1/comments")
    CommentDto getComment(@RequestParam long commentId);

    @GetMapping("api/v1/like/{likeId}")
    LikeDto getLike(@PathVariable long likeId);
}