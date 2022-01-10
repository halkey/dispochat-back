package au.dispochat.common.dto;

import au.dispochat.chatter.entity.Chatter;
import au.dispochat.common.enums.MessageResponseType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MessageResponseFetchRequester {
    private final MessageResponseType messageResponseType;
    private final String Message;
    private final Chatter chatter;
}
