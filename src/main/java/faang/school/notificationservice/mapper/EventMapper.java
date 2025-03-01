package faang.school.notificationservice.mapper;

import faang.school.notificationservice.dto.event.AchievementEvent;
import faang.school.notificationservice.redis.event.AchievementRedisEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "source", source = "source")
    @Mapping(target = "achievement", source = "event.achievementName")
    AchievementEvent toEvent(AchievementRedisEvent event, Object source);
}
