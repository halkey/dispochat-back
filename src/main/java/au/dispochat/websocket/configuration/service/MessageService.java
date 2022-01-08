package au.dispochat.websocket.configuration.service;

import au.dispochat.chatter.entity.Chatter;
import au.dispochat.chatter.service.ChatterService;
import au.dispochat.websocket.configuration.model.MessageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class MessageService {

    private ChatterService chatterService;

    public Chatter sendMessage(MessageModel message) {
        Chatter sender = chatterService.findByUniqueKey(message.getSenderUniqueKey())
                .orElseThrow(() -> new EntityNotFoundException("Your registration could not be found!"));
        if (sender.isRoomOwnership()) {
            return sender.getRoom().getGuest();
        } else {
            return sender.getRoom().getOwner();
        }
    }
}
