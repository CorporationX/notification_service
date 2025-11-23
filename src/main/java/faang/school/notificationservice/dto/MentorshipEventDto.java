package faang.school.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MentorshipEventDto(
        Long mentorId,
        Long menteeId
) {
}