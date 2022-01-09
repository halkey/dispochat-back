package au.dispochat.chatter.service;

import au.dispochat.chatter.entity.Chatter;
import au.dispochat.chatter.repository.ChatterRepository;
import au.dispochat.common.dto.MessageResponse;
import au.dispochat.common.enums.MessageResponseType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatterService {

    private final ChatterRepository chatterRepository;

    public MessageResponse createChatter(Chatter chatter) {

        if (chatterRepository.existsByUniqueKey(chatter.getUniqueKey())) {
            return new MessageResponse(MessageResponseType.ERROR, "You are already registered!", null);
        }

        chatterRepository.save(chatter);
        return new MessageResponse(MessageResponseType.SUCCESS, "Successfully registered!", null);
    }

    public void updateChatter(Chatter chatter) {
        chatterRepository.updateChatter(chatter);
    }

    public Optional<Chatter> findByUniqueKey(String uniqueKey) {
        return chatterRepository.findByUniqueKey(uniqueKey);
    }
}
