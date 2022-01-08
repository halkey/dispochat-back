package au.dispochat.websocket.configuration.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MessageModel {

    private String message;
    private String senderUniqueKey;
}
