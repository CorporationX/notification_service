package faang.school.notificationservice.service.post;

import faang.school.notificationservice.dto.MentorshipOfferedEventDto;
import faang.school.notificationservice.entity.PreferredContact;

public class TelegramService implements NotificationService {


    @Override
    public boolean isPreferredContact(MentorshipOfferedEventDto mentorshipOfferedEventDto) {
        return mentorshipOfferedEventDto.getPreferredContact().equals(PreferredContact.EMAIL);
    }

    @Override
    public void send(MentorshipOfferedEventDto mentorshipOfferedEventDto, String text) {

    }
}
