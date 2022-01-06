package au.dispochat.chatter.repository;


import au.dispochat.chatter.entity.Chatter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatterRepository extends JpaRepository<Chatter, String> {

    boolean existsByUniqueKey(String uniqueKey);

    Optional<Chatter> findByUniqueKey(String uniqueKey);
}
