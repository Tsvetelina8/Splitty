package server.api;

import commons.Event;
import commons.Person;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;
import server.database.PersonRepository;

import java.util.List;

import static server.services.EntityService.isNullOrEmpty;

@Controller
@RequestMapping("api/events/{eid}/persons")
public class PersonController {

    private final PersonRepository personRepository;
    private final EventRepository eventRepository;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(PersonController.class);

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    /**
     * Constructs a PersonController with the person repository.
     *
     * @param personRepository the person repository
     * @param eventRepository the event repository
     */
    public PersonController(PersonRepository personRepository, EventRepository eventRepository) {
        this.personRepository = personRepository;
        this.eventRepository = eventRepository;
    }

    /**
     * Get mapping for /api/event/{eid}/persons to get all persons in event
     *
     * @param eid the eid specified in the path
     * @return   ist of all persons in event
     */
    @GetMapping(path = { "", "/" })
    @ResponseBody
    public ResponseEntity<List<Person>> getAll(@PathVariable("eid") long eid) {
        if (!eventRepository.existsById(eid)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(eventRepository.findById(eid).get().getParticipants());
    }

    /**
     * Get mapping for /api/event/{eid}/persons/{id} to get a person by id
     *
     * @param eid the eevent id specified in the path
     * @param id the id specified in the path
     * @return   response person entity specified by id
     */
    @GetMapping({"/{id}", "/{id}/"})
    @ResponseBody
    public ResponseEntity<Person> getById(@PathVariable("eid") long eid,
                                          @PathVariable("id") long id) {
        if (!eventRepository.existsById(eid)) {
            return ResponseEntity.notFound().build();
        }
        if (!personRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        Event ev = eventRepository.findById(eid).get();
        Person p = personRepository.findById(id).get();

        if (!ev.getParticipants().contains(p)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(personRepository.findById(id).get());
    }

    /**
     * Post mapping for api/event/{eid}/persons to create a new person
     *
     * @param eid the eevent id specified in the path
     * @param person the person object provided in the request body
     * @return response person entity that was created
     */
    @PostMapping(path = { "", "/" })
    @ResponseBody
    public ResponseEntity<Person> add(@PathVariable("eid") long eid,
                                      @RequestBody Person person) {
        if (isNullOrEmpty(person.firstName)
                || isNullOrEmpty(person.lastName)) {
            return ResponseEntity.badRequest().build();
        }
        if (!eventRepository.existsById(eid)) {
            return ResponseEntity.notFound().build();
        }

        Event ev = eventRepository.findById(eid).get();
        ev.setLastActivity(null);
        Person saved = personRepository.save(person);
        ev.addParticipant(personRepository.findById(saved.id).get());
        eventRepository.save(ev);

        try {
            messagingTemplate.convertAndSend("/topic/person", saved);
        } catch (Exception e) {
            // Don't send if there's no person (tests)
            e.printStackTrace();
        }

        return ResponseEntity.ok(saved);
    }

    /**
     * Delete mapping for api/event/{eid}/persons/{id} to delete a person by their unique id.
     *
     * @param eid the eid specified in the path
     * @param id the id specified in the path
     * @return response indicating success or failure of the performed operation.
     */
    @DeleteMapping({"/{id}", "/{id}/"})
    @ResponseBody
    public ResponseEntity<Void> deleteById(@PathVariable("eid") long eid,
                                           @PathVariable("id") long id) {
        if (!personRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        if (!eventRepository.existsById(eid)) {
            return ResponseEntity.notFound().build();
        }

        Event ev = eventRepository.findById(eid).get();
        Person p = personRepository.findById(id).get();

        ev.getParticipants().remove(p);
        personRepository.deleteById(id);
        ev.setLastActivity(null);
        eventRepository.save(ev);

        try {
            messagingTemplate.convertAndSend("/topic/person", p);
        } catch (Exception e) {
            // Don't send if there's no person (tests)
            e.printStackTrace();
        }

        return ResponseEntity.noContent().build();
    }

    /**
     * Put mapping for api/event/{eid}/persons/{id} to edit a person by their unique id.
     *
     * @param eid the eid specified in the path
     * @param id the id specified in the path
     * @param person the perosn to edit
     * @return response indicating success or failure of the performed operation.
     */
    @PutMapping({"/{id}", "/{id}/"})
    @ResponseBody
    public ResponseEntity<Person> editById(@PathVariable("eid") long eid,
                                           @PathVariable("id") long id,
                                         @RequestBody Person person) {
        if (!personRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        if (!eventRepository.existsById(eid)) {
            return ResponseEntity.notFound().build();
        }

        Event ev = eventRepository.findById(eid).get();
        Person p = personRepository.findById(id).get();

        ev.getParticipants().remove(p);
        personRepository.deleteById(id);
        Person newPerson = add(eid, person).getBody();
        ev.addParticipant(newPerson);
        ev.setLastActivity(null);
        eventRepository.save(ev);

        try {
            messagingTemplate.convertAndSend("/topic/person", newPerson);
        } catch (Exception e) {
            // Don't send if there's no person (tests)
            e.printStackTrace();
        }

        return ResponseEntity.ok(newPerson);
    }
}
