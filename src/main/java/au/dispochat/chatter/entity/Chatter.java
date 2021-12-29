package au.dispochat.chatter.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "CHATTER")
public class Chatter {

    @Id
    private String uniqueKey;
    private String nickName;
    private String city;
    private String country;
}
