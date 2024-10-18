package faang.school.notificationservice.test_data.follower;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.follower.FollowerEventDto;

public class TestDataFollowerEvent {
    public FollowerEventDto getFollowerEventDto() {
        return FollowerEventDto.builder()
                .username("Denis")
                .followerId(1L)
                .followeeId(2L)
                .build();
    }
    public UserDto getFollowee() {
        return UserDto.builder()
                .id(2L)
                .username("User1")
                .email("user1@mail.com")
                .phone("8-800-555-35-35")
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
    }
}
