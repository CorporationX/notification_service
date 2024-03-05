package faang.school.notificationservice.dto.user;

import faang.school.notificationservice.dto.skill.SkillDto;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private String aboutMe;
    private Long countryId;
    private String city;
    private Integer experience;
    private List<SkillDto> skills;
    private PreferredContact preference;

    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM
    }
}
