package faang.school.notificationservice.exception;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


class ErrorResponseTest {

    @Test
    public void testCreateErrorResponseWithDetail() {
        String expectedMessage = new String("error message");
        Map<String, String> detail = Map.of(
            "field1", "value1",
            "field2", "value2"
        );
        ErrorResponse errorResponse = new ErrorResponse(new String("error message"), detail);
        assertEquals(expectedMessage, errorResponse.getErrorMessage());
        assertNotNull(errorResponse.getErrorDate());
        assertEquals(2, errorResponse.getDetail().size());
    }
}