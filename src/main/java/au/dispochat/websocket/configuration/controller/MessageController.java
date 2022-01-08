package au.dispochat.websocket.configuration.controller;

import au.dispochat.chatter.entity.Chatter;
import au.dispochat.websocket.configuration.model.MessageModel;
import au.dispochat.websocket.configuration.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    private MessageService messageService;

    @MessageMapping("/chat/sendMessage")
    public void sendMessage(MessageModel message) {
        Chatter receiver = messageService.sendMessage(message);

        //TODO convertAndSendToUser
        simpMessagingTemplate.convertAndSend("/topic/messages" + receiver.getUniqueKey(), message);
    }
}
