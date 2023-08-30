package faang.school.notificationservice.service.post;

import faang.school.notificationservice.dto.MentorshipOfferedEventDto;

public interface NotificationService {
    boolean isPreferredContact(MentorshipOfferedEventDto mentorshipOfferedEventDto);

    void send(MentorshipOfferedEventDto mentorshipOfferedEventDto, String text);
}