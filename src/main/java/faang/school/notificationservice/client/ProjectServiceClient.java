package faang.school.notificationservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "${services.project-service.name}")
public interface ProjectServiceClient {

}
