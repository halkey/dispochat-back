package au.dispochat.room.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JoinRoomDTO {

    private String uniqueKey;

    private Long roomId;

}
