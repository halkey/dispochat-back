package au.dispochat.chatter.controller;

import au.dispochat.chatter.controller.dto.ChatterDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/createChatter")

public class ChatterController {

    //private final ChatterService chatterService;

    @PostMapping
    public String createChatter(@RequestBody ChatterDTO chatterDTO) {
        //String[] chatterInfo = chatterString.split("x");
//
        //Chatter chatter = new Chatter();
        //chatter.setUniqueKey(chatterInfo[0]);
        //chatter.setNickName(chatterInfo[1]);
        //chatter.setCity(chatterInfo[2]);
        //chatter.setCountry(chatterInfo[3].split("=")[0]);
//
        //return chatterService.createChatter(chatter);
        System.out.println(chatterDTO);
        return "yes";

    }
}

