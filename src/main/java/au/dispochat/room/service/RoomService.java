package au.dispochat.room.service;

import au.dispochat.chatter.entity.Chatter;
import au.dispochat.chatter.enums.ChatterType;
import au.dispochat.chatter.service.ChatterService;
import au.dispochat.common.dto.MessageResponse;
import au.dispochat.common.dto.MessageResponseFetchRequester;
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

        if (chatter.getRoom() != null) {
            if (chatter.getChatterType().equals(ChatterType.OWNER)) {
                return new MessageResponse(MessageResponseType.ERROR
                        , "You are already have a chat room with id %d ".formatted(chatter.getRoom().getId()), null);
            } else if (chatter.getChatterType().equals(ChatterType.GUEST)) {
                return new MessageResponse(MessageResponseType.ERROR
                        , "You are already in a chat room with id %d as a guest.".formatted(chatter.getRoom().getId()), null);
            } else if (chatter.getChatterType().equals(ChatterType.REQUESTER)) {
                return new MessageResponse(MessageResponseType.ERROR
                        , "You have already requested to join a chat room with id %d as a guest. Wait for the result or you can cancel it".formatted(chatter.getRoom().getId()), null);
            }
        }

        //TODO Mapper Yapılacak
        Room room = new Room();
        room.setOwner(chatter);
        room.setGuest(null);
        room.setRequester(null);
        roomRepository.save(room);

        chatter.setRoom(room);
        chatter.setChatterType(ChatterType.OWNER);
        chatterService.updateChatter(chatter);

        return new MessageResponse(MessageResponseType.SUCCESS
                , "Room with id %d has been created!".formatted(room.getId()), room.getId());
    }

    @Transactional
    public MessageResponse joinRoom(Long roomId, String uniqueKey) {
        Chatter requester = chatterService.findByUniqueKey(uniqueKey)
                .orElseThrow(() -> new EntityNotFoundException("You did not register yet!"));

        if (requester.getRoom() != null) {
            if (requester.getChatterType().equals(ChatterType.OWNER)) {
                return new MessageResponse(MessageResponseType.ERROR
                        , "You are already have a chat room with id %d".formatted(requester.getRoom().getId()), null);
            } else if (requester.getChatterType().equals(ChatterType.GUEST)) {
                return new MessageResponse(MessageResponseType.ERROR
                        , "You are already in a chat room with id %d as a guest.".formatted(requester.getRoom().getId()), null);
            } else if (requester.getChatterType().equals(ChatterType.REQUESTER)) {
                return new MessageResponse(MessageResponseType.ERROR
                        , "You have already requested to join a chat room with id %d as a guest.".formatted(requester.getRoom().getId()), null);
            }
        }

        Room targetRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room with id %d does not exist!".formatted(roomId)));

        if (targetRoom.getRequester() != null) {
            return new MessageResponse(MessageResponseType.ERROR
                    , "You are too late! Someone else sent a join request to the room with id %d before you.".formatted(roomId), null);
        }

        if (targetRoom.getGuest() != null) {
            return new MessageResponse(MessageResponseType.ERROR
                    , "You are too late! Someone else has joined to the room with id %d before you and the chat has started".formatted(roomId), null);
        }

        requester.setRoom(targetRoom);
        requester.setChatterType(ChatterType.REQUESTER);
        chatterService.updateChatter(requester);

        targetRoom.setRequester(requester);
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

        if (ownerChatter.getChatterType() != ChatterType.OWNER) {
            return new MessageResponse(MessageResponseType.ERROR
                    , "You are not owner of any chat room", null);
        }

        Room targetRoom = ownerChatter.getRoom();

        if (targetRoom.getGuest() == null) {
            return new MessageResponse(MessageResponseType.ERROR
                    , "Sorry but there is no one who wants to join to your room!", null);
        }

        if (targetRoom.getRequester() == null) {
            return new MessageResponse(MessageResponseType.ERROR
                    , "Sorry but there is no one who wants to join to your room!", null);
        }

        if (!isAccepted) {
            String guestChatterNickname = targetRoom.getRequester().getNickName();
            targetRoom.setRequester(null);
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

        targetRoom.setGuest(targetRoom.getRequester());
        targetRoom.setRequester(null);
        roomRepository.updateRoom(targetRoom);

        //TODO notify guest +

        return new MessageResponse(MessageResponseType.SUCCESS
                , "Your accept request of user %s has been fulfilled."
                .formatted(targetRoom.getGuest().getNickName()), null);
    }

    public MessageResponseFetchRequester fetchRequester(String uniqueKey) {
        Chatter ownerChatter = chatterService.findByUniqueKey(uniqueKey)
                .orElseThrow(() -> new EntityNotFoundException("You did not register yet!"));

        if (ownerChatter.getRoom() == null) {
            return new MessageResponseFetchRequester(MessageResponseType.ERROR
                    , "You do not have any chat room yet!", null);
        }

        if (ownerChatter.getChatterType() != ChatterType.OWNER) {
            return new MessageResponseFetchRequester(MessageResponseType.ERROR
                    , "You are not owner of any chat room", null);
        }

        Room targetRoom = ownerChatter.getRoom();

        if (targetRoom.getGuest() != null) {
            return new MessageResponseFetchRequester(MessageResponseType.ERROR
                    , "You can not take more than one guest to your room!", null);
        }

        if (targetRoom.getRequester() == null) {
            return new MessageResponseFetchRequester(MessageResponseType.ERROR
                    , "Sorry but there is no one who wants to join to your room!", null);
        }

        Chatter requester = targetRoom.getRequester();

        requester.setUniqueKey("seni hiç alakadar etmez nınınınnnn");
        requester.setChatterType(null);
        requester.setRoom(null);
        return new MessageResponseFetchRequester(MessageResponseType.SUCCESS, "%s wants to join to your room".formatted(requester.getNickName()), requester);
    }

   //public MessageResponse isAccepted(String uniqueKey) {
   //    Chatter requester = chatterService.findByUniqueKey(uniqueKey)
   //            .orElseThrow(() -> new EntityNotFoundException("You did not register yet!"));

   //    if (requester.getRoom() != null) {
   //        if (requester.getChatterType().equals(ChatterType.OWNER)) {
   //            return new MessageResponse(MessageResponseType.ERROR
   //                    , "You are already have a chat room with id %d ".formatted(requester.getRoom().getId()), null);
   //        } else if (requester.getChatterType().equals(ChatterType.GUEST)) {
   //            return new MessageResponse(MessageResponseType.ERROR
   //                    , "You are already in a chat room with id %d as a guest.".formatted(requester.getRoom().getId()), null);
   //        }
   //    }

   //    Room targetRoom = requester.getRoom();

   //    if (targetRoom.getGuest() != null) {
   //        return new MessageResponse(MessageResponseType.ERROR
   //                , "You are too late! Someone else has joined to the room with id %d before you and the chat has started".formatted(roomId), null);
   //    }
   //    if (targetRoom.getRequester() != null) {
   //        return new MessageResponse(MessageResponseType.ERROR
   //                , "You are too late! Someone else sent a join request to the room with id %d before you.".formatted(roomId), null);
   //    }













   //    return new MessageResponse(MessageResponseType.SUCCESS, "%s has accepted your join request. You are being directed to room with id %d".formatted(owner.getNickname, requester.getNickname), null);

   //}
}
