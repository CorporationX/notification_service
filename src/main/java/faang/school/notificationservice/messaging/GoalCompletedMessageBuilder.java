package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.GoalCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;

    @Component
    @RequiredArgsConstructor
    public class GoalCompletedMessageBuilder implements MessageBuilder<GoalCompletedEvent> {

        private final MessageTemplateLoader templateLoader;

        @Override
        public Class<?> getInstance() {
            return GoalCompletedEvent.class;
        }

        @Override
        public String buildMessage(GoalCompletedEvent event, Locale locale) {
            String template = templateLoader.getTemplate("goal-completed", locale);
            return String.format(template, event.getGoalId());
        }
}
