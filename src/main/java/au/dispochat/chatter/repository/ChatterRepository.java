package au.dispochat.chatter.repository;


import au.dispochat.chatter.entity.Chatter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatterRepository extends JpaRepository<Chatter, String> {

    boolean existsByUniqueKey(String uniqueKey);

    Optional<Chatter> findByUniqueKey(String uniqueKey);

    @Modifying
    @Query(
            value = """
                    update Chatter newChatter
                    set newChatter.room= :#{#chatter.room},
                        newChatter.chatterType= :#{#chatter.chatterType}
                    where newChatter.uniqueKey = :#{#chatter.uniqueKey}
                    """
    )
    void updateChatter(
            Chatter chatter
    );
}
