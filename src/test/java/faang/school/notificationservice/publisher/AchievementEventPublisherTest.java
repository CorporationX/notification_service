package faang.school.notificationservice.publisher;

import faang.school.notificationservice.dto.AchievementEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AchievementEventPublisherTest {

    private MockMvc mockMvc;
    @InjectMocks
    private AchievementEventPublisher achievementEventPublisher;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private ChannelTopic topic;

    @BeforeEach
    void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(achievementEventPublisher).build();
    }

    @Test
    void givenValidAchievementEventWhenPublishThenReturnSuccessMessage() throws Exception {
        // given - precondition
        AchievementEvent achievementEvent = new AchievementEvent();
        achievementEvent.setUserId(23L);

        when(topic.getTopic())
                .thenReturn("achievementTopic");
        when(redisTemplate.convertAndSend(anyString(), any(AchievementEvent.class)))
                .thenReturn(1L);

        // when - action
        var response = mockMvc.perform(MockMvcRequestBuilders.post("/achievement")
                .content(new ObjectMapper().writeValueAsString(achievementEvent))
                .contentType("application/json"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        verify(redisTemplate, times(1))
                .convertAndSend(eq("achievementTopic"), eq(achievementEvent));
    }
}