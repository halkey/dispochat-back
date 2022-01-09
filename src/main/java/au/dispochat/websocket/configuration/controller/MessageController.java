package au.dispochat.websocket.configuration.controller;

import au.dispochat.chatter.entity.Chatter;
import au.dispochat.websocket.configuration.model.MessageModel;
import au.dispochat.websocket.configuration.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    @Autowired
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageService messageService;

    @MessageMapping("/chat/{uniqueKey}")
    public void sendMessage(@PathVariable String uniqueKey, MessageModel message) {
        Chatter receiver = messageService.sendMessage(message);

        //TODO convertAndSendToUser
        simpMessagingTemplate.convertAndSend("/topic/messages" + receiver.getUniqueKey(), message);
    }
}
