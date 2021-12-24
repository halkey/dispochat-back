package au.dispochat.room.entity;

import au.dispochat.chatter.entity.Chatter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "ROOM")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String owner;

    private String ownerUniqueKey;

    private String guest;

    private String guestUniqueKey;

    @Column(name = "OWNER_MESSAGE")
    private String ownerMessage;

    @Column(name = "GUEST_MESSAGE")
    private String guestMessage;

}
