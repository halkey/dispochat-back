package au.dispochat.room.service;

import au.dispochat.chatter.entity.Chatter;
import au.dispochat.chatter.repository.ChatterRepository;
import au.dispochat.room.entity.Room;
import au.dispochat.room.repository.RoomRepository;
import org.springframework.stereotype.Service;


@Service
public class RoomService {

    private final ChatterRepository chatterRepository;
    private final RoomRepository roomRepository;

    public RoomService(ChatterRepository chatterRepository, RoomRepository roomRepository) {
        this.chatterRepository = chatterRepository;
        this.roomRepository = roomRepository;
    }

    public String createRoom(String uniqueKey) {
        Chatter chatter = chatterRepository
                .findByUniqueKey(uniqueKey);
        System.out.println("Service nickname " + chatter.getNickName());

        Room room = new Room();
        room.setOwner(chatter.getNickName());
        room.setOwnerUniqueKey(chatter.getUniqueKey());
        room.setGuest(null);
        room.setGuestUniqueKey(null);
        room.setOwnerMessage("Chat did not start!");
        room.setGuestMessage("Chat did not start!");

        roomRepository.save(room);

        return "Room has been created";

    }

}
