package faang.school.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import faang.school.notificationservice.model.PreferredContact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FollowerEvent {
    //@JsonProperty("followerId")
    private Long followerId;
    //@JsonProperty("followeeId")
    private Long followeeId;
   // @JsonProperty("preferredContact")
    private PreferredContact preferredContact;
}
