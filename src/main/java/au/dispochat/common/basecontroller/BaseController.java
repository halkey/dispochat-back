package au.dispochat.common.basecontroller;

import au.dispochat.common.dto.MessageResponse;
import au.dispochat.common.enums.MessageResponseType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

public class BaseController {

    @ExceptionHandler(EntityNotFoundException.class)
    public MessageResponse entityExceptionHandler(Exception exception) {
        return new MessageResponse(MessageResponseType.ERROR, exception.getMessage(), null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public MessageResponse httpMessageHandler(Exception exception) {
        return new MessageResponse(MessageResponseType.ERROR, exception.getMessage(), null);
    }

}
