package faang.school.notificationservice.model;

import lombok.Data;

@Data
public class ContactPreference {

    private long id;
    private long userId;
    private String preference;
}