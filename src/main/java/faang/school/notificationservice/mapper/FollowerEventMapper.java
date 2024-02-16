package faang.school.notificationservice.mapper;

import faang.school.notificationservice.dto.FollowerEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface FollowerEventMapper {
//    @Mappings({
//            @Mapping(target = "receiverId", source = "followeeId"),
//            @Mapping(target = "actorId", expression = "java(faang.school.analytics.model.EventType.FOLLOWER.ordinal())"),
//            @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.FOLLOWER)"),
//            @Mapping(target = "receivedAt", source = "timestamp")
//    })
//    FollowerEventDto followerEventFromDto(FollowerEventDto dto);
}
