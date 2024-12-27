package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserContactsDto;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserFeignServiceTest {
    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private UserFeignService userFeignService;

    private UserContactsDto userContactsDto;

    @BeforeEach
    void setUp() {
        userContactsDto = UserContactsDto.builder()
                .email("doniyor.kurbanov.24@gmail.com")
                .phone("+998900279515")
                .build();
    }

    @Test
    @DisplayName("Positive test - Successfully fetch user contacts")
    void testGetUserContactsSuccess() {
        when(userServiceClient.getUserContacts(1L)).thenReturn(userContactsDto);

        UserContactsDto result = userFeignService.getUserContacts(1L);

        assertNotNull(result);
        assertEquals("doniyor.kurbanov.24@gmail.com", result.getEmail());
        assertEquals("+998900279515", result.getPhone());

        verify(userServiceClient, times(1)).getUserContacts(1L);
    }

    @Test
    @DisplayName("Negative test - FeignException thrown while fetching user contacts")
    void testGetUserContactsFeignException() {

        when(userServiceClient.getUserContacts(1L)).thenThrow(FeignException.class);

        assertThrows(FeignException.class, () -> userFeignService.getUserContacts(1L));

        verify(userServiceClient, times(1)).getUserContacts(1L);
    }
}
