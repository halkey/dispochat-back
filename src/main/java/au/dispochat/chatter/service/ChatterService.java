package au.dispochat.chatter.service;

import au.dispochat.chatter.entity.Chatter;
import au.dispochat.chatter.repository.ChatterRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatterService {

    private final ChatterRepository chatterRepository;

    public ChatterService(ChatterRepository chatterRepository) {
        this.chatterRepository = chatterRepository;
    }

    public String createChatter(Chatter chatter) {

        if(chatterRepository.existsByUniqueKey(chatter.getUniqueKey())) {
            return "Already Registered";
        };

        chatterRepository.save(chatter);
        return "Registered Successfully";
    }
}
