package faang.school.notificationservice.messageBuilder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.achievement.AchievementDto;
import faang.school.notificationservice.dto.achievement.UserEventAchievementDto;
import faang.school.notificationservice.dto.achievement.Rarity;
import faang.school.notificationservice.dto.user.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementMessageBuilderTest {
    @Mock
    private UserServiceClient userServiceClient;
    @InjectMocks
    private AchievementMessageBuilder achievementMessageBuilder;

    @Test
    public void buildMessageTest() {
        UserDto userDto = new UserDto();
        userDto.setUsername("Pavel");
        userDto.setId(1L);

        AchievementDto achievementDto = new AchievementDto();
        achievementDto.setDescription("hello");
        achievementDto.setTitle("Raceta");
        achievementDto.setRarity(Rarity.EPIC);
        UserEventAchievementDto eventAchievement = new UserEventAchievementDto();
        eventAchievement.setUserId(1L);
        eventAchievement.setAchievement(achievementDto);

        when(userServiceClient.getUser(1L)).thenReturn(userDto);
        String expected = achievementMessageBuilder.buildMessage(eventAchievement,new Locale("ru"), "ru");
        String actual = "Hello, Pavel you got an achievement: Raceta\ndescription: hello\nrarity: EPIC";
        Assertions.assertEquals(expected, actual);
    }
}