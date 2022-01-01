package au.dispochat.room.repository;

import au.dispochat.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {

    Room findById(Long id);

    @Modifying
    @Query(
            value = """
                    update Room newRoom
                    set newRoom.wantToJoin= :#{#room.wantToJoin}
                    where newRoom.id = :#{#room.id}
                    """
    )
    void guncelleRoom(
            Room room
    );
}
