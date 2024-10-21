package faang.school.notificationservice.dto;

public record MentorshipAcceptedEvent(
        Long requestId,
        Long mentorId,
        Long menteeId) {
}
