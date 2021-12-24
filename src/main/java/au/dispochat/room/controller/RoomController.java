package au.dispochat.room.controller;

import au.dispochat.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/createRoom")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public String createRoom(@RequestBody String uniqueKey) throws Exception {
        System.out.println("Controller uniqueKey " + uniqueKey);
        return roomService.createRoom(uniqueKey.split("=")[0]);
    }
}

