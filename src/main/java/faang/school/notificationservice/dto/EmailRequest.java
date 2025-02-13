package faang.school.notificationservice.dto;

import lombok.Builder;

@Builder
public record EmailRequest(UserDto userDto, String message) {
}
