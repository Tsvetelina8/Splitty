package server.api;

import commons.Event;
import commons.Expense;
import commons.Loan;
import commons.Person;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.LoanRepository;
import server.database.PersonRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static server.services.EntityService.isNullOrEmpty;


@RestController
@RequestMapping("/api/events/")
public class EventController {
    EventRepository eventRepository;
    PersonRepository personRepository;
    ExpenseRepository expenseRepository;
    LoanRepository loanRepository;


    private static final Logger log = org.slf4j.LoggerFactory.getLogger(EventController.class);

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    /**
     * Constructs an EventController with the given event repository.
     *
     * @param expenseRepository the expense repository
     * @param personRepository the person repository
     * @param eventRepository the event repository
     * @param loanRepository the loan repository
     */
    public EventController(ExpenseRepository expenseRepository,
                           PersonRepository personRepository,
                           EventRepository eventRepository,
                           LoanRepository loanRepository) {
        this.expenseRepository = expenseRepository;
        this.personRepository = personRepository;
        this.eventRepository = eventRepository;
        this.loanRepository = loanRepository;
    }

    /**
     * Get mapping for /api/events to get all events in the current EventRepository
     *
     * @return list of all event
     */
    @GetMapping(path = { "", "/" })
    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    private Map<Object, Consumer<Event>> listeners = new HashMap<>();

    /**
     * Get mapping for /api/events/updatesTitle to get all events in the current EventRepository
     *
     * @return list of all event
     */
    @GetMapping(path = {"/updatesTitle" })
    public DeferredResult<ResponseEntity<Event>> getAllUpdate() {

        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<Event>>(5000L, noContent);

        var key = new Object();

        listeners.put(key, q -> {
            res.setResult(ResponseEntity.ok(q));
        });

        res.onCompletion(() -> {
            listeners.remove(key);
        });


        return res;
    }

    /**
     * Get mapping for /api/event/{id} to get an event by id
     *
     * @param id the id specified in the path
     * @return   response event entity specified by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Event> getById(@PathVariable("id") long id) {
        if (!eventRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(eventRepository.findById(id).get());
    }

    /**
     * Get mapping for /api/event/code/{code} to get an event by invite code
     *
     * @param code the code specified in the path
     * @return   response event entity specified by id
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<Event> getByInviteCode(@PathVariable("code") String code) {
        if (isNullOrEmpty(code) || !eventRepository.existsEventByInviteCode(code)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(eventRepository.findByInviteCode(code).get());
    }

    /**
     * Get mapping for /api/events/title to get an event by title
     *
     * @param title the code specified in the path
     * @return   response event entity specified by id
     */
    @GetMapping("/title/{title}")
    public ResponseEntity<Event> getByTitle(@PathVariable("title") String title) {
        if (title == null || title.isEmpty() || !eventRepository.existsEventByTitle(title)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(eventRepository.findByTitle(title).get());
    }

    /**
     * Delete mapping for /api/event/{id} to delete an event by id
     *
     * @param id the id specified in the path
     * @return   empty return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Event> deleteById(@PathVariable("id") long id) {
        if (!eventRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Event eventToDelete = eventRepository.findById(id).get();
        for(Person p : eventToDelete.getParticipants()) {
            personRepository.deleteById(p.id);
        }
        for(Expense e : eventToDelete.getExpenses()) {
            for (Loan l : e.loans) {
                loanRepository.deleteById(l.id);
            }
            expenseRepository.deleteById(e.id);
        }
        eventRepository.deleteById(id);

        try {
            messagingTemplate.convertAndSend("/topic/events", eventToDelete);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.noContent().build();
    }



    /**
     * Put mapping for /api/event/{id} to update an event's title by id
     *
     * @param id    the id specified in the path
     * @param title the title specifiend in request body
     * @return      response event entity specified by id
     */
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateTitleById(@PathVariable("id") long id,
                                                 @RequestBody String title) {
        if (!eventRepository.existsById(id) || isNullOrEmpty(title) || title.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        if (eventRepository.existsEventByTitle(title)) {
            return ResponseEntity.badRequest().build();
        }

        Event event = eventRepository.findById(id).get();
        event.setTitle(title);
        event.setLastActivity(null);
        listeners.forEach((k, l) -> l.accept(event));
        Event saved = eventRepository.save(event);



        try {
            messagingTemplate.convertAndSend("/topic/events", saved);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(saved);
    }

    /**
     * Post mapping for /api/event/ to add an event by title
     *
     * @param title the title specified in the request body
     * @return   response event entity with the event
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Event> add(@RequestBody String title) {
        if (isNullOrEmpty(title) || title.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        if (eventRepository.existsEventByTitle(title)) {
            return ResponseEntity.badRequest().build();
        }

        Event saved = eventRepository.save(new Event(title));
        try {
            messagingTemplate.convertAndSend("/topic/events", saved);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(saved);
    }

    /**
     * Post mapping for /api/event/json to import an event by Event object
     *
     * @param newEvent the event to import specified in the request body
     * @return   response event entity with the event
     */
    @PostMapping(path = { "/json" })
    public ResponseEntity<Event> importEventAdd(@RequestBody Event newEvent) {
        if (eventRepository.existsEventByTitle(newEvent.getTitle())) {
            return ResponseEntity.badRequest().build();
        }
        newEvent.id = 0;
        Map<Person, Person> participants = new HashMap<>();
        List<Expense> expenses = new ArrayList<>(newEvent.getExpenses());
        List<Person> allParticipants = new ArrayList<>(newEvent.getParticipants());
        newEvent.removeAllParticipants();
        newEvent.removeAllExpenses();

        Event importedEvent = eventRepository.save(newEvent);
        Event savedEvent = eventRepository.findById(importedEvent.id).get();
        for (Person p : allParticipants) {
            Person person = new Person(p);
            savedEvent.addParticipant(person);
            person = personRepository.save(person);
            participants.put(p, person);
        }
        for (Expense e : expenses) {
            e.id = 0;
            e.payer = participants.get(e.payer);
            for (Loan l : e.loans) {
                l.id = 0;
                l.payer = participants.get(l.payer);
                l.borrower = participants.get(l.borrower);
                loanRepository.save(l);
            }
            savedEvent.addExpense(e);
            expenseRepository.save(e);


        }
        savedEvent.setInviteCode();
        savedEvent.setLastActivity(null);

        Event saved = eventRepository.save(savedEvent);

        try {
            messagingTemplate.convertAndSend("/topic/events", saved);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(saved);
    }
}
