package faang.school.notificationservice.exceptions;

import faang.school.notificationservice.dto.user.PreferredContact;

public class PreferredContactNotExistException extends RuntimeException {

    public PreferredContactNotExistException(String message) {
        super(message);
    }

    public PreferredContactNotExistException(PreferredContact preferredContact) {
        super(String.format("Preferred contact %s not exist", preferredContact));
    }
}
