package server.database;

import commons.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    /**
     * Auto-generated JPA method to find event by invite code
     *
     * @param code the code to be found
     * @return    event entity found by JPA, if any
     */
    Optional<Event> findByInviteCode(String code);

    /**
     * find event by title
     *
     * @param title the code to be found
     * @return    event entity found by JPA, if any
     */
    Optional<Event> findByTitle(String title);

    /**
     * Auto-generated JPA method to check whether
     * an event with the given invite code exists
     *
     * @param code the code to be found
     * @return    whether an event exists
     */
    boolean existsEventByInviteCode(String code);

    /**
     * Auto-generated JPA method to check whether
     * an event with the given title exists
     *
     * @param title the title to be found
     * @return    whether an event exists
     */
    boolean existsEventByTitle(String title);
}
