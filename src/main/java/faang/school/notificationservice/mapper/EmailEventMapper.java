package faang.school.notificationservice.mapper;

import faang.school.notificationservice.event.EmailEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.mail.SimpleMailMessage;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmailEventMapper {

    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "to", expression = "java(new String[] { event.getTo() })")
    SimpleMailMessage toSimpleMailMessage(EmailEvent event);
}
