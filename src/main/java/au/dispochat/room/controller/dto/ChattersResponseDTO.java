package au.dispochat.room.controller.dto;

import au.dispochat.chatter.entity.Chatter;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChattersResponseDTO {

    private Chatter ownerChatter;
    private Chatter guestChatter;
}
