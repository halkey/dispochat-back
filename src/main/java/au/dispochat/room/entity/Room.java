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

    @ManyToOne
    @JoinColumn(name = "OWNER_UNIQUE_KEY")
    private Chatter owner;

    @ManyToOne
    @JoinColumn(name = "GUEST_UNIQUE_KEY")
    private Chatter guest;

}
