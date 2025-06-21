package faang.school.notificationservice.client;

import faang.school.notificationservice.config.client.feign.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "project-service",
        url = "${services.project-service.host}:${services.project-service.port}",
        path = "/api/v1",
        configuration = FeignClientConfig.class)
public interface ProjectServiceClient {

}
