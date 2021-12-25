package au.dispochat.room.controller;

import au.dispochat.chatter.entity.Chatter;
import au.dispochat.chatter.service.ChatterService;
import au.dispochat.room.controller.dto.JoinRoomDTO;
import au.dispochat.room.entity.Room;
import au.dispochat.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final ChatterService chatterService;

    @RequestMapping("/createRoom")
    @PostMapping
    public Long createRoom(@RequestBody String uniqueKey) throws Exception {
        return roomService.createRoom(uniqueKey.split("=")[0]);
    }

    @RequestMapping("/joinRoom")
    @PostMapping
    public Room joinRoom(@RequestBody JoinRoomDTO joinRoomDTO) throws Exception {
        Chatter guestChatter = chatterService.findByUniqueKey(joinRoomDTO.getUniqueKey());
        return roomService.joinRoom(joinRoomDTO.getRoomId(), guestChatter);
    }
}

