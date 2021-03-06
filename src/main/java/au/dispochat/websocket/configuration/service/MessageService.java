package au.dispochat.websocket.configuration.service;

import au.dispochat.chatter.entity.Chatter;
import au.dispochat.chatter.enums.ChatterType;
import au.dispochat.chatter.service.ChatterService;
import au.dispochat.websocket.configuration.model.MessageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final ChatterService chatterService;

    public Chatter sendMessage(MessageModel message) {
        Chatter sender = chatterService.findByUniqueKey(message.getSenderUniqueKey())
                .orElseThrow(() -> new EntityNotFoundException("Your registration could not be found!"));
        if (sender.getChatterType().equals(ChatterType.OWNER)) {
            return sender.getRoom().getGuest();
        } else if (sender.getChatterType().equals(ChatterType.GUEST)) {
            return sender.getRoom().getOwner();
        } else {
            throw new PermissionDeniedDataAccessException("You do not have permission to send a message in this chat room!", null);
        }
    }
}
