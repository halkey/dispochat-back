package au.dispochat.chatter.controller.dto;

import lombok.*;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class ChatterDTO {

    @NotEmpty
    private String uniqueKey;

    @NotEmpty(message = "Nickname can not be empty!")
    private String nickName;

    @NotEmpty(message = "City can not be empty!")
    private String city;

    @NotEmpty(message = "Country can not be empty!")
    private String country;
}
