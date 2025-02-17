package faang.school.notificationservice.utils;

import faang.school.notificationservice.exception.impl.non_retryable.ListSizeNotOneException;
import faang.school.notificationservice.exception.impl.non_retryable.NotFoundElementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ListUtilTest {

    private final ListUtil listUtil = new ListUtil();

    @Test
    void testRequireSingleElement_Success() {
        List<String> list = List.of("element");

        String result = listUtil.requireSingleElement(list, String.class, Object.class);

        assertEquals("element", result);
    }

    @Test
    void testRequireSingleElement_NotFound() {
        List<String> list = List.of();

        NotFoundElementException exception = assertThrows(NotFoundElementException.class, () ->
                listUtil.requireSingleElement(list, String.class, Object.class));

        assertTrue(exception.getMessage().contains("No String found for Object"));
    }

    @Test
    void testRequireSingleElement_ListSizeNotOne() {
        List<String> list = List.of("element1", "element2");

        ListSizeNotOneException exception = assertThrows(ListSizeNotOneException.class, () ->
                listUtil.requireSingleElement(list, String.class, Object.class)
        );

        assertTrue(exception.getMessage().contains("Expected exactly 1 String for: Object, but got 2"));
    }
}