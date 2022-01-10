package au.dispochat.room.repository;

import au.dispochat.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {

    Optional<Room> findById(Long id);

    @Modifying
    @Query(
            value = """
                    update Room newRoom
                    set newRoom.requester= :#{#room.requester},
                        newRoom.guest= :#{#room.guest}
                    where newRoom.id = :#{#room.id}
                    """
    )
    void updateRoom(
            Room room
    );
}
