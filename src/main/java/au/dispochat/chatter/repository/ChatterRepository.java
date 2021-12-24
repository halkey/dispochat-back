package au.dispochat.chatter.repository;


import au.dispochat.chatter.entity.Chatter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatterRepository extends JpaRepository <Chatter, String> {

    boolean existsByUniqueKey(String uniqueKey);

    Chatter findByUniqueKey(String uniqueKey);
}
