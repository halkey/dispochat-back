package au.dispochat.common.dto;

import au.dispochat.common.enums.MessageResponseType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MessageResponse {
    private final MessageResponseType messageResponseType;
    private final String Message;
    private final Object object;
}
