package faang.school.notificationservice.mapper;

import faang.school.notificationservice.dto.TelegramChatDto;
import faang.school.notificationservice.model.TelegramChat;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TgMapper {

    TelegramChat toEntity(TelegramChatDto dto);
}
