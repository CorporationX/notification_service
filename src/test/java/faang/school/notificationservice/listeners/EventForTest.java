package faang.school.notificationservice.listeners;

public class EventForTest {
    private final long invitedId;
    private final String name;

    public EventForTest(long invitedId, String name) {
        this.invitedId = invitedId;
        this.name = name;
    }

    public long getInvitedId() {
        return invitedId;
    }

    public String getName() {
        return name;
    }
}
