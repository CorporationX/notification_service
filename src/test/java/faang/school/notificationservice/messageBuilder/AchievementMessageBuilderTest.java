package faang.school.notificationservice.messageBuilder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.achievement.DtoAchievement;
import faang.school.notificationservice.dto.achievement.DtoUserEventAchievement;
import faang.school.notificationservice.dto.achievement.Rarity;
import faang.school.notificationservice.dto.user.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

        DtoAchievement dtoAchievement = new DtoAchievement();
        dtoAchievement.setDescription("hello");
        dtoAchievement.setTitle("Raceta");
        dtoAchievement.setRarity(Rarity.EPIC);
        DtoUserEventAchievement eventAchievement = new DtoUserEventAchievement();
        eventAchievement.setUserId(1L);
        eventAchievement.setAchievement(dtoAchievement);

        when(userServiceClient.getUser(1L)).thenReturn(userDto);
        String expected = achievementMessageBuilder.buildMessage(eventAchievement, "ru");
        String actual = "Hello, Pavel you got an achievement: Raceta\ndescription: hello\nrarity: EPIC";
        Assertions.assertEquals(expected, actual);
    }
}