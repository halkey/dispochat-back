package au.dispochat.chatter.controller;

import au.dispochat.chatter.controller.dto.ChatterDTO;
import au.dispochat.chatter.entity.Chatter;
import au.dispochat.chatter.service.ChatterService;
import au.dispochat.common.basecontroller.BaseController;
import au.dispochat.common.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/createChatter")
@RequiredArgsConstructor
public class ChatterController extends BaseController {

    private final ChatterService chatterService;

    @PostMapping
    public MessageResponse createChatter(@RequestBody ChatterDTO chatterDTO) {

        Chatter chatter = new Chatter();
        chatter.setUniqueKey(chatterDTO.getUniqueKey());
        chatter.setNickName(chatterDTO.getNickName());
        chatter.setCity(chatterDTO.getCity());
        chatter.setCountry(chatterDTO.getCountry());

        return chatterService.createChatter(chatter);

    }
}

