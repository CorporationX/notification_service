package faang.school.notificationservice.service.recommendation;

import faang.school.notificationservice.dto.recommendation.RecommendationRequestEvent;

public interface RecommendationService {

    void sendNotification(RecommendationRequestEvent recommendationRequestEvent);
}