package au.dispochat.chatter.entity;

import au.dispochat.chatter.enums.ChatterType;
import au.dispochat.room.entity.Room;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "CHATTER")
public class Chatter {

    @Id
    private String uniqueKey;

    @ManyToOne
    @JoinColumn(name = "ROOM")
    private Room room;

    private ChatterType chatterType;
    private String nickName;
    private String city;
    private String country;


}
