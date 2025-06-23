package faang.school.notificationservice.model.kafka.event;

public record EventMessage(
        String title,
        String description,
        String startDate,
        String endDate,
        String location,
        String type,
        String status,
        String initiatorName
) {}
