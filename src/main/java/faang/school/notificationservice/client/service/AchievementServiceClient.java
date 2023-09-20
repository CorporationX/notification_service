package faang.school.notificationservice.client.service;

import faang.school.notificationservice.dto.AchievementDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "achievement-service", url = "${achievement-service.host}:${achievement-service.port}")
public interface AchievementServiceClient {
    @GetMapping("api/v1/achievements/{id}")
    AchievementDto getAchievementById(@PathVariable Long id);
}
