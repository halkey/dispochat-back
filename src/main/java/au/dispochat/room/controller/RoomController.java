package au.dispochat.room.controller;

import au.dispochat.common.basecontroller.BaseController;
import au.dispochat.common.dto.MessageResponse;
import au.dispochat.common.dto.MessageResponseFetchRequester;
import au.dispochat.room.controller.dto.JoinRoomDTO;
import au.dispochat.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoomController extends BaseController {

    private final RoomService roomService;

    @RequestMapping("/createRoom")
    @PostMapping
    public MessageResponse createRoom(@RequestBody String uniqueKey) {
        return roomService.createRoom(uniqueKey.split("=")[0]);
    }

    @RequestMapping("/joinRoom")
    @PostMapping
    public MessageResponse joinRoom(@RequestBody final JoinRoomDTO joinRoomDTO) {
        return roomService.joinRoom(joinRoomDTO.getRoomId(), joinRoomDTO.getUniqueKey());
    }

    @RequestMapping("/guestRequest")
    @PostMapping
    public MessageResponse acceptGuest(@RequestBody String uniqueKey, Boolean isAccepted) {
        uniqueKey = uniqueKey.split("=")[0];
        return roomService.acceptGuest(uniqueKey, isAccepted);
    }

    @RequestMapping("/fetchRequester")
    @PostMapping
    public MessageResponseFetchRequester fetchRequester(@RequestBody String uniqueKey) {
        uniqueKey = uniqueKey.split("=")[0];
        return roomService.fetchRequester(uniqueKey);
    }

    @RequestMapping("/isAccepted")
    @PostMapping
    public MessageResponse isAccepted(@RequestBody String uniqueKey) {
        uniqueKey = uniqueKey.split("=")[0];
        return roomService.isAccepted(uniqueKey);
    }
}

