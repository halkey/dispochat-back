package au.dispochat.chatter.service;

import au.dispochat.chatter.entity.Chatter;
import au.dispochat.chatter.repository.ChatterRepository;
import au.dispochat.common.dto.MessageResponse;
import au.dispochat.common.enums.MessageResponseType;
import au.dispochat.common.exception.AlreadyRegisteredException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatterService {

    private final ChatterRepository chatterRepository;

    @SneakyThrows
    public MessageResponse createChatter(Chatter chatter) {

        if (chatterRepository.existsByUniqueKey(chatter.getUniqueKey())) {
            throw new AlreadyRegisteredException();
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
