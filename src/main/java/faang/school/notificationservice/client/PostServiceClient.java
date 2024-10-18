package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.post.PostDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "post-service", url = "${post-service.host}:${post-service.port}")
public interface PostServiceClient {

    @GetMapping("api/v1/posts/{id}")
    PostDto getPost(@PathVariable long id);
}
