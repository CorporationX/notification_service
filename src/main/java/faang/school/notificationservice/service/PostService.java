package faang.school.notificationservice.service;

import faang.school.notificationservice.client.PostServiceClient;
import faang.school.notificationservice.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostServiceClient postServiceClient;

    public PostDto getPostById(Long postId) {
        return postServiceClient.getPostById(postId);
    }
}
