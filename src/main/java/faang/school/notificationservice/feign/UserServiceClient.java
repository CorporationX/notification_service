package faang.school.notificationservice.feign;

import faang.school.notificationservice.model.dto.GoalDto;
import faang.school.notificationservice.model.dto.SkillCandidateDto;
import faang.school.notificationservice.model.dto.RecommendationRequestDto;
import faang.school.notificationservice.model.dto.SkillDto;
import faang.school.notificationservice.model.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}")
public interface UserServiceClient {

    @GetMapping("/users/{id}")
    UserDto getUser(@PathVariable long id);

    @GetMapping("/goals/{id}")
    GoalDto getGoal(@PathVariable long id);

    @PutMapping("/users/updateTelegramUserId")
    UserDto updateTelegramUserId(@RequestParam String telegramUserName,
                                 @RequestParam String telegramUserId);

    @GetMapping("/skills/{userId}")
    List<SkillDto> getUserSkills(@PathVariable Long userId);

    @GetMapping("/skills/{userId}/offers")
    List<SkillCandidateDto> getOfferedSkills(@PathVariable long userId);

    @GetMapping("/recommendation-requests/{id}")
    RecommendationRequestDto getRecommendationRequest(@PathVariable Long id);
}
