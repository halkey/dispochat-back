package au.dispochat.room.controller.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

@Data
@RequiredArgsConstructor
public class JoinRoomDTO {


    private String uniqueKey;

    @Valid
    private Long roomId;

}
