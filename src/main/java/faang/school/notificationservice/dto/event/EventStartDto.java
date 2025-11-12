package faang.school.notificationservice.dto.event;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import faang.school.notificationservice.messaging.EventStart;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.util.List;
import java.util.Locale;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record EventStartDto(
        @NotNull(message = "Event cannot be negative")
        Long eventId,
        List<@NotNull(message = "Participants cannot be null")
        @Positive(message = "Participants cannot be negative")
                Long> attendeesIds,
        EventStart eventStart,
        String title,
        Locale locale
) {
}