package faang.school.notificationservice.builder;

import faang.school.notificationservice.LikeEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LikeEventBuilder implements EventBuilder<LikeEvent> {
    @Override
    public LikeEvent build(String message) {
        String[] stringsArray = message.split("}");
        Map<String, String> variables = new HashMap<>();

        Arrays.stream(stringsArray).forEach(str -> {
            int lastIndexBeforeValue = str.lastIndexOf("{");
            int firstIndexAfterKey = str.lastIndexOf("=");
            String value = str.substring(lastIndexBeforeValue + 1);
            String key = str.substring(0, firstIndexAfterKey - 1);
            variables.put(key, value);
        });

        return new LikeEvent(Long.getLong(variables.get("authorId")),
                Long.getLong(variables.get("likerId")),
                Long.getLong(variables.get("postId")));
    }
}
