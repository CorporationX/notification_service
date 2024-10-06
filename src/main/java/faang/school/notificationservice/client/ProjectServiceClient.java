package faang.school.notificationservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "project-service", url = "${project-service.url}")
public interface ProjectServiceClient {
}
