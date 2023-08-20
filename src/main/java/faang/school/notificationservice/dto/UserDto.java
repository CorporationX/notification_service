package faang.school.notificationservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Locale;

@Builder
public record UserDto(Long id,
                      String username,
                      String email,
                      String phone,
                      String aboutMe,
                      boolean active,
                      String city,
                      Integer experience,
                      List<Long> followers,
                      List<Long> followees,
                      List<Long> mentors,
                      List<Long> mentees,
                      CountryDto country,
                      List<GoalDto> goals,
                      List<SkillDto> skills,
                      PreferredContact preference) {
    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM
    }
}
