package faang.school.notificationservice.service.post;

import faang.school.notificationservice.dto.MentorshipOfferedEventDto;
import faang.school.notificationservice.entity.PreferredContact;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsService implements NotificationService {

    @Override
    public boolean isPreferredContact(MentorshipOfferedEventDto mentorshipOfferedEventDto) {
        return mentorshipOfferedEventDto.getPreferredContact().equals(PreferredContact.PHONE);
    }

    @Override
    public void send(MentorshipOfferedEventDto mentorshipOfferedEventDto, String text) {

    }
}
