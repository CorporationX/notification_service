package faang.school.notificationservice.event.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountEvent {
    private Long ownerId;
    private String ownerType;
    private String accountType;
    private String currency;
}
