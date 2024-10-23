package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.feign.UserServiceClient;
import faang.school.notificationservice.model.dto.SkillCandidateDto;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.SkillOfferedEvent;
import faang.school.notificationservice.service.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class SkillOfferedMessageBuilder implements MessageBuilder<SkillOfferedEvent> {
    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public Class<SkillOfferedEvent> getSupportedClass() {
        return SkillOfferedEvent.class;
    }

    @Override
    public String buildMessage(SkillOfferedEvent event, Locale locale) {
        UserDto receiverDto = userServiceClient.getUser(event.getReceiverId());
        UserDto senderDto = userServiceClient.getUser(event.getSenderId());

        List<SkillCandidateDto> candidates = userServiceClient.getOfferedSkills(event.getReceiverId());
        String offeredSkillTitle = candidates.stream()
                .filter(candidate -> candidate.getSkill().getId().equals(event.getSkillId()))
                .map(candidate -> candidate.getSkill().getTitle())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Skill with id '" + event.getSkillId() + "' not found."));

        return messageSource.getMessage("skill.offered",
                new Object[]{receiverDto.getUsername(), senderDto.getUsername(), offeredSkillTitle}, locale);
    }
}
