package au.dispochat.chatter.controller;

import au.dispochat.chatter.entity.Chatter;
import au.dispochat.chatter.service.ChatterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/createChatter")
@RequiredArgsConstructor
public class ChatterController {

    private final ChatterService chatterService;

    @PostMapping
    public String createChatter(@RequestBody Chatter chatter) {

        return chatterService.createChatter(chatter);

    }
}

