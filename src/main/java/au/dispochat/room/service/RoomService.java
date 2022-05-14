package au.dispochat.room.service;

import au.dispochat.chatter.entity.Chatter;
import au.dispochat.chatter.enums.ChatterType;
import au.dispochat.chatter.repository.ChatterRepository;
import au.dispochat.chatter.service.ChatterService;
import au.dispochat.common.dto.MessageResponse;
import au.dispochat.common.dto.MessageResponseFetchRequester;
import au.dispochat.common.enums.MessageResponseType;
import au.dispochat.common.exception.*;
import au.dispochat.room.controller.dto.ChattersRequestDTO;
import au.dispochat.room.controller.dto.ChattersResponseDTO;
import au.dispochat.room.entity.Room;
import au.dispochat.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;


@Service
@RequiredArgsConstructor
public class RoomService {

    private final ChatterService chatterService;

    private final RoomRepository roomRepository;

    private final ChatterRepository chatterRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Transactional
    public MessageResponse createRoom(String uniqueKey) throws Exception {

        Chatter chatter = chatterService.findByUniqueKey(uniqueKey)
                .orElseThrow(NotRegisteredYetException::new);

        if (chatter.getRoom() != null) {
            if (chatter.getChatterType().equals(ChatterType.OWNER)) {
                throw new AlreadyOwnerException(chatter.getRoom().getId());
            } else if (chatter.getChatterType().equals(ChatterType.GUEST)) {
                throw new AlreadyGuestException(chatter.getRoom().getId());
            } else if (chatter.getChatterType().equals(ChatterType.REQUESTER)) {
                throw new AlreadyRequestedToJoinException(chatter.getRoom().getId());
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
    public MessageResponse joinRoom(Long roomId, String uniqueKey) throws Exception {
        Chatter requester = chatterService.findByUniqueKey(uniqueKey)
                .orElseThrow(NotRegisteredYetException::new);

        if (requester.getRoom() != null) {
            if (requester.getChatterType().equals(ChatterType.OWNER)) {
                throw new AlreadyOwnerException(requester.getRoom().getId());
            } else if (requester.getChatterType().equals(ChatterType.GUEST)) {
                throw new AlreadyGuestException(requester.getRoom().getId());
            } else if (requester.getChatterType().equals(ChatterType.REQUESTER)) {
                throw new AlreadyRequestedToJoinException(requester.getRoom().getId());
            }
        }

        Room targetRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotExistException(roomId));

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
    public MessageResponse acceptGuest(String uniqueKey, Boolean isAccepted) throws Exception {
        Chatter ownerChatter = chatterService.findByUniqueKey(uniqueKey)
                .orElseThrow(NotRegisteredYetException::new);

        if (ownerChatter.getRoom() == null) {
            return new MessageResponse(MessageResponseType.ERROR
                    , "You do not have a chat room yet!", null);
        }

        if (ownerChatter.getChatterType() != ChatterType.OWNER) {
            return new MessageResponse(MessageResponseType.ERROR
                    , "You are not owner of any chat room", null);
        }

        Room targetRoom = roomRepository.findById(ownerChatter.getRoom().getId())
                .orElseThrow(() -> new RoomNotExistException(ownerChatter.getRoom().getId()));


        if (targetRoom.getRequester() == null) {
            return new MessageResponse(MessageResponseType.ERROR
                    , "Sorry but there is no one who wants to join to your room!", null);
        }

        if (targetRoom.getGuest() != null) {
            return new MessageResponse(MessageResponseType.ERROR
                    , "You can not take more than one guest to your room!", null);
        }

        Chatter requesterChatter = targetRoom.getRequester();

        if (!isAccepted) {
            targetRoom.setRequester(null);
            roomRepository.updateRoom(targetRoom);

            requesterChatter.setChatterType(null);
            chatterService.updateChatter(requesterChatter);

            return new MessageResponse(MessageResponseType.SUCCESS
                    , "Your reject request of user %s has been fulfilled."
                    .formatted(requesterChatter.getNickName()), null);
        }


        targetRoom.setGuest(targetRoom.getRequester());
        targetRoom.setRequester(null);
        roomRepository.updateRoom(targetRoom);

        requesterChatter.setChatterType(ChatterType.GUEST);
        chatterService.updateChatter(requesterChatter);

        //TODO notify guest +

        return new MessageResponse(MessageResponseType.SUCCESS
                , "Your accept request of user %s has been fulfilled."
                .formatted(targetRoom.getGuest().getNickName()), null);
    }

    public MessageResponseFetchRequester fetchRequester(String uniqueKey) throws Exception {
        Chatter ownerChatter = chatterService.findByUniqueKey(uniqueKey)
                .orElseThrow(NotRegisteredYetException::new);

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

    public MessageResponse isAccepted(String uniqueKey) {
        Chatter requester = chatterService.findByUniqueKey(uniqueKey)
                .orElseThrow(() -> new EntityNotFoundException("You did not register yet!"));

        if (requester.getRoom() == null)
            return new MessageResponse(MessageResponseType.ERROR, "You did not send a join request to any chat room nor created a new one.", null);

        if (requester.getChatterType() == null) {
            return new MessageResponse(MessageResponseType.ERROR
                    , "Your request is rejected by the owner of the chat room with id %d".formatted(requester.getRoom().getId()), null);
        }

        if (requester.getChatterType().equals(ChatterType.OWNER)) {
            return new MessageResponse(MessageResponseType.ERROR
                    , "You are already have a chat room with id %d ".formatted(requester.getRoom().getId()), null);
        }


        if (requester.getChatterType().equals(ChatterType.GUEST)) {
            Room targetRoom = roomRepository.findById(requester.getRoom().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Room with id %d does not exist!".formatted(requester.getRoom().getId())));

            if (targetRoom.getGuest() == requester) {
                if (targetRoom.getGuest().getUniqueKey().equals(requester.getUniqueKey())) {
                    return new MessageResponse(MessageResponseType.SUCCESS, "%s has accepted your join request. You are being directed to room with id %d".formatted(targetRoom.getOwner().getNickName(), targetRoom.getId()), null);
                }
            }
        } else if (requester.getChatterType().equals(ChatterType.REQUESTER)) {
            return new MessageResponse(MessageResponseType.ERROR, "Sorry but owner has not gave an instruction yet", null);
        }

        return new MessageResponse(MessageResponseType.ERROR, "Opss something went wrong", null);

    }

    @Transactional
    public MessageResponse killSwitch(String uniqueKey) {
        Chatter killer = chatterService.findByUniqueKey(uniqueKey)
                .orElseThrow(() -> new EntityNotFoundException("You did not register yet!"));

        Room targetRoom = roomRepository.findById(killer.getRoom().getId())
                .orElseThrow(() -> new EntityNotFoundException("Room with id %d does not exist!".formatted(killer.getRoom().getId())));

        Chatter ownerChatter;
        Chatter guestChatter;
        Chatter killed;

        if (killer.getChatterType().equals(ChatterType.OWNER)) {
            ownerChatter = killer;
            if (targetRoom.getGuest() == null) {
                targetRoom.setOwner(null);
                targetRoom.setGuest(null);
                roomRepository.updateRoom(targetRoom);

                ownerChatter.setRoom(null);
                chatterService.updateChatter(ownerChatter);

                roomRepository.deleteById(targetRoom.getId());
                chatterRepository.deleteByUniqueKey(ownerChatter.getUniqueKey());

                return new MessageResponse(MessageResponseType.SUCCESS
                        , "ALL OF THE DATA IS DELETED", null);
            }
            guestChatter = chatterService.findByUniqueKey(targetRoom.getGuest().getUniqueKey())
                    .orElseThrow(() -> new EntityNotFoundException("Guest has already gone"));
            killed = guestChatter;
        } else {
            guestChatter = killer;
            ownerChatter = chatterService.findByUniqueKey(targetRoom.getOwner().getUniqueKey())
                    .orElseThrow(() -> new EntityNotFoundException("Owner has already gone"));
            killed = ownerChatter;
        }

        targetRoom.setOwner(null);
        targetRoom.setGuest(null);
        roomRepository.updateRoom(targetRoom);

        ownerChatter.setRoom(null);
        guestChatter.setRoom(null);
        chatterService.updateChatter(ownerChatter);
        chatterService.updateChatter(guestChatter);

        roomRepository.deleteById(targetRoom.getId());
        chatterRepository.deleteByUniqueKey(ownerChatter.getUniqueKey());
        chatterRepository.deleteByUniqueKey(guestChatter.getUniqueKey());

        simpMessagingTemplate.convertAndSend("/topic/messages/" + killed.getUniqueKey(), "!?/kill");
        return new MessageResponse(MessageResponseType.SUCCESS
                , "ALL OF THE DATA IS DELETED", null);


    }

    public ChattersResponseDTO queryChatter(ChattersRequestDTO chattersRequestDTO) {
        Room targetRoom = roomRepository.findById(chattersRequestDTO.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException("Room with id %d does not exist!".formatted(chattersRequestDTO.getRoomId())));

        Chatter roomRequester = chatterService.findByUniqueKey(chattersRequestDTO.getUniqueKey())
                .orElseThrow(() -> new EntityNotFoundException("You did not register yet!"));

        Chatter ownerChatter;
        Chatter guestChatter;

        if (roomRequester.getChatterType().equals(ChatterType.OWNER)) {
            ownerChatter = roomRequester;
            guestChatter = targetRoom.getGuest();
        } else {
            guestChatter = roomRequester;
            ownerChatter = targetRoom.getOwner();
        }

        if (guestChatter == null) {
            ownerChatter.setUniqueKey("Seni hiç alakadar etmez nınınınnn");
            ownerChatter.setRoom(null);
            return new ChattersResponseDTO(ownerChatter, null);
        }

        if(targetRoom.getGuest().getUniqueKey().equals(chattersRequestDTO.getUniqueKey()) || targetRoom.getOwner().getUniqueKey().equals(chattersRequestDTO.getUniqueKey())) {
            guestChatter.setUniqueKey("Seni hiç alakadar etmez nınınınnn");
            guestChatter.setRoom(null);

            ownerChatter.setUniqueKey("Seni hiç alakadar etmez nınınınnn");
            ownerChatter.setRoom(null);
            return new ChattersResponseDTO(ownerChatter, guestChatter);
        }

        throw new EntityNotFoundException("Either room does not exist or you do not have the permission.");
    }
}
