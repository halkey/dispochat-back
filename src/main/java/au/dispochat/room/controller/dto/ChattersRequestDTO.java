package au.dispochat.room.controller.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ChattersRequestDTO {

    private String uniqueKey;
    private Long roomId;
}
