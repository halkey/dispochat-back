package au.dispochat.room.service;

import au.dispochat.chatter.entity.Chatter;
import au.dispochat.chatter.repository.ChatterRepository;
import au.dispochat.room.entity.Room;
import au.dispochat.room.repository.RoomRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
public class RoomService {

    private final ChatterRepository chatterRepository;
    private final RoomRepository roomRepository;

    public RoomService(ChatterRepository chatterRepository, RoomRepository roomRepository) {
        this.chatterRepository = chatterRepository;
        this.roomRepository = roomRepository;
    }

    public Long createRoom(String uniqueKey) {
        Chatter chatter = chatterRepository
                .findByUniqueKey(uniqueKey);

        //To Do: Mapper Yapılacak

        Room room = new Room();
        room.setOwner(chatter.getNickName());
        room.setOwnerUniqueKey(chatter.getUniqueKey());
        room.setGuest(null);
        room.setGuestUniqueKey(null);
        room.setOwnerMessage("Chat did not start!");
        room.setGuestMessage("Chat did not start!");

        roomRepository.save(room);

        return room.getId();

    }

    @Transactional
    public Room joinRoom(Long roomId, Chatter guestChatter) {
        Room targetRoom = roomRepository.findById(roomId);
        targetRoom.setGuest(guestChatter.getNickName());
        targetRoom.setGuestUniqueKey(guestChatter.getUniqueKey());
        roomRepository.guncelleRoom(targetRoom);
        return targetRoom;
    }

}
