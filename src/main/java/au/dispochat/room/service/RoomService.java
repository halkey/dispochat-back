package au.dispochat.room.service;

import au.dispochat.chatter.entity.Chatter;
import au.dispochat.chatter.service.ChatterService;
import au.dispochat.common.dto.MessageResponse;
import au.dispochat.common.enums.MessageResponseType;
import au.dispochat.room.entity.Room;
import au.dispochat.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;


@Service
@RequiredArgsConstructor
public class RoomService {

    private final ChatterService chatterService;

    private final RoomRepository roomRepository;

    @Transactional
    public MessageResponse createRoom(String uniqueKey) {

        Chatter chatter = chatterService.findByUniqueKey(uniqueKey)
                .orElseThrow(() -> new EntityNotFoundException("You Did Not Register Yet!"));

        //TODO Mapper Yapılacak

        Room room = new Room();
        room.setOwner(chatter);
        room.setGuest(null);
        room.setWantToJoin(null);
        roomRepository.save(room);

        chatter.setRoom(room);
        chatter.setRoomOwnership(true);
        chatterService.updateChatter(chatter);

        return new MessageResponse(MessageResponseType.SUCCESS
                , "Room with id %d has been created!".formatted(room.getId()), room.getId());

    }

    @Transactional
    public MessageResponse joinRoom(Long roomId, String uniqueKey) {
        Chatter guestChatter = chatterService.findByUniqueKey(uniqueKey)
                .orElseThrow(() -> new EntityNotFoundException("You did not register yet!"));

        Room targetRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room with id %d is not exist!".formatted(roomId)));

        if (targetRoom.getWantToJoin() != null) {
            return new MessageResponse(MessageResponseType.ERROR
                    , "You are too late! Someone else sent a join request to the room with id %d before you.".formatted(roomId), null);
        }

        if (targetRoom.getGuest() != null) {
            return new MessageResponse(MessageResponseType.ERROR
                    , "You are too late! Someone else has joined to the room with id %d before you and the chat has started".formatted(roomId), null);
        }

        guestChatter.setRoom(targetRoom);
        chatterService.updateChatter(guestChatter);
        targetRoom.setWantToJoin(guestChatter);
        roomRepository.updateRoom(targetRoom);

        //TODO notify owner

        return new MessageResponse(MessageResponseType.SUCCESS
                , "Your join request to room %d has been sent to owner of the room".formatted(roomId), null);
    }

    @Transactional
    public MessageResponse acceptGuest(String uniqueKey, Boolean isAccepted) {
        Chatter ownerChatter = chatterService.findByUniqueKey(uniqueKey)
                .orElseThrow(() -> new EntityNotFoundException("You did not register yet!"));

        if (ownerChatter.getRoom() == null) {
            return new MessageResponse(MessageResponseType.ERROR
                    , "You do not have a chat room yet!", null);
        }

        Room targetRoom = ownerChatter.getRoom();

        if (targetRoom.getWantToJoin() == null) {
            return new MessageResponse(MessageResponseType.ERROR
                    , "Sorry but there is no one who wants to join to your room!", null);
        }

        if (!isAccepted) {
            String guestChatterNickname = targetRoom.getWantToJoin().getNickName();
            targetRoom.setWantToJoin(null);
            roomRepository.updateRoom(targetRoom);

            //TODO notify guest -

            return new MessageResponse(MessageResponseType.SUCCESS
                    , "Your reject request of user %s has been fulfilled."
                    .formatted(guestChatterNickname), null);
        }

        if (targetRoom.getGuest() != null) {
            return new MessageResponse(MessageResponseType.ERROR
                    , "You can not take more than one guest to your room!", null);
        }

        targetRoom.setGuest(targetRoom.getWantToJoin());
        targetRoom.setWantToJoin(null);
        roomRepository.updateRoom(targetRoom);

        //TODO notify guest +

        return new MessageResponse(MessageResponseType.SUCCESS
                , "Your accept request of user %s has been fulfilled."
                .formatted(targetRoom.getGuest().getNickName()), null);
    }
}
