package au.dispochat.room.service;

import au.dispochat.chatter.entity.Chatter;
import au.dispochat.chatter.repository.ChatterRepository;
import au.dispochat.chatter.service.ChatterService;
import au.dispochat.common.dto.MessageResponse;
import au.dispochat.common.enums.MessageResponseType;
import au.dispochat.room.entity.Room;
import au.dispochat.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@RequiredArgsConstructor
public class RoomService {

    private final ChatterRepository chatterRepository;
    private final RoomRepository roomRepository;

    private final ChatterService chatterService;

    public MessageResponse createRoom(String uniqueKey) {
        Chatter chatter = new Chatter();
        try {
            chatter = chatterRepository.findByUniqueKey(uniqueKey);
        } catch (Exception chatterException) {
            return new MessageResponse(MessageResponseType.ERROR, "You Did Not Register Yet!");
        }

        //TO DO Mapper Yapılacak

        Room room = new Room();
        room.setOwner(chatter);
        room.setGuest(null);
        roomRepository.save(room);

        return new MessageResponse(MessageResponseType.SUCCESS, "Room With Id \"%d\" Has Been Created!".formatted(room.getId()));
    }

    @Transactional
    public MessageResponse joinRoom(Long roomId, String uniqueKey) {
        Chatter guestChatter = new Chatter();
        try {
            guestChatter = chatterService.findByUniqueKey(uniqueKey);
        } catch (Exception chatterException) {
            return new MessageResponse(MessageResponseType.ERROR, "You Did Not Register Yet!");
        }

        Room targetRoom = new Room();
        try {
            roomRepository.findById(roomId);
        } catch (Exception roomException) {
            return new MessageResponse(MessageResponseType.ERROR, "Room With Id \"%d\" Is Not Exist!".formatted(roomId));
        }

        targetRoom.setGuest(guestChatter);
        roomRepository.guncelleRoom(targetRoom);
        return new MessageResponse(MessageResponseType.SUCCESS, "Your Join Request To Room \"%d\" Has Been Sent to Owner of the Room".formatted(roomId));
    }

}
