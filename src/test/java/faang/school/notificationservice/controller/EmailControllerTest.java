package faang.school.notificationservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.EmailRequest;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc
public class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;

    private EmailRequest request;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private UserDto user;
    private final String MESSAGE = "test message";

    @BeforeEach
    void setUp() {
        user = new UserDto();
        user.setId(1L);
        user.setUsername("test name");
        user.setEmail("example@example.com");
        user.setPreference(UserDto.PreferredContact.EMAIL);

        request = EmailRequest.builder()
                .userDto(user)
                .message(MESSAGE)
                .build();
    }

    @Test
    public void testSendMail_Success() throws Exception {
        mockMvc.perform(post("/email/send")
                        .header("x-user-id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Email успешно отправлен пользователю: example@example.com"));

        verify(emailService).send(user, MESSAGE);
    }
}
