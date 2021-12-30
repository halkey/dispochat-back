package au.dispochat.chatter.controller.mapper;

import au.dispochat.chatter.controller.dto.ChatterDTO;
import au.dispochat.chatter.entity.Chatter;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ChatterMapper {

    Chatter toChatter(ChatterDTO chatterDTO);

}
