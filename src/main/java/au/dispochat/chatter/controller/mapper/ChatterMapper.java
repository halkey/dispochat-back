package au.dispochat.chatter.controller.mapper;

import au.dispochat.chatter.controller.dto.ChatterDTO;
import au.dispochat.chatter.entity.Chatter;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;


@Mapper
public interface ChatterMapper {

    Chatter toChatter(ChatterDTO chatterDTO);

}
