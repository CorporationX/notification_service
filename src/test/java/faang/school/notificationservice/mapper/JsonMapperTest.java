package faang.school.notificationservice.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JsonMapperTest<T> {
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private JsonMapper<T> jsonMapper;

    @Test
    void toObject() throws IOException {
        byte[] json = new byte[0];
        jsonMapper.toObject(json, null);
        verify(objectMapper).readValue(json, (Class<T>) null);
    }
}