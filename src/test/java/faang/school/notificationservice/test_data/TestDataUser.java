package faang.school.notificationservice.test_data;

import faang.school.notificationservice.dto.UserDto;
import lombok.Getter;

@Getter
public class TestDataUser {
    public UserDto getUserDto() {
        return UserDto.builder()
                .id(1L)
                .username("User1")
                .email("user1@mail.com")
                .phone("8-800-555-35-35")
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
    }
    public UserDto getUserDto2() {
        return UserDto.builder()
                .id(2L)
                .username("User2")
                .email("user2@mail.com")
                .phone("8-800-555-35-35")
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
    }
}
