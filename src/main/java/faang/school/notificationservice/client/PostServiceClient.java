package faang.school.notificationservice.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "post-service", url = "${post-service.host}:${post-service.port}")
public interface PostServiceClient {

    @GetMapping("${url.version01}/posts/post/{postId}")
    long getUserIdByPostId(@PathVariable long postId);
}
