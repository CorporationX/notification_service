package faang.school.notificationservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "project-service", url = "${services.project-service.host}:${services.project-service.port}")
public interface ProjectServiceClient {

}
