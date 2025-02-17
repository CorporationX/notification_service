package faang.school.notificationservice.exception.handler;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.exception.impl.non_retryable.DtoValidationFailedException;
import faang.school.notificationservice.exception.impl.non_retryable.KafkaMappingFailedException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaMapperHandler {
    private final ObjectMapper objectMapper;

    public <R> R mapAndValidateKafkaEvent(Object kafkaEventValue, Class<R> clazz) {
        try {
            JsonParser parser = objectMapper.treeAsTokens(objectMapper.valueToTree(kafkaEventValue));
            R mappedDto = objectMapper.readValue(parser, clazz);

            validateDto(mappedDto);

            return mappedDto;
        } catch (IOException e) {
            String error = "Error mapping Kafka event: " + e.getMessage();
            log.error(error, e);
            throw new KafkaMappingFailedException(error);
        } catch (ConstraintViolationException e) {
            String error = "Validation failed: " + e.getMessage();
            log.error(error, e);
            throw new DtoValidationFailedException(error);
        }
    }

    private <T> void validateDto(T dto) {
        Validator validator;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
        Set<ConstraintViolation<T>> violations = validator.validate(dto);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
