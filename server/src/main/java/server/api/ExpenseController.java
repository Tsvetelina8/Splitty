package server.api;

import commons.Event;
import commons.Expense;
import commons.Loan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.LoanRepository;

import java.util.List;

import static server.services.EntityService.isNullOrEmpty;

@RestController
@RequestMapping("/api/events/{eid}/expenses")
public class ExpenseController {

    private static final Logger log = LoggerFactory.getLogger(ExpenseController.class);
    private final EventRepository eventRepository;
    private final ExpenseRepository expenseRepository;
    private final LoanRepository loanRepository;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    /**
     * Constructs a ExpenseController with the given repositories.
     *
     * @param expenseRepository the expense repository
     * @param eventRepository the event repository
     * @param loanRepository the loan repository
     */
    public ExpenseController(ExpenseRepository expenseRepository,
                             EventRepository eventRepository,
                             LoanRepository loanRepository) {
        this.expenseRepository = expenseRepository;
        this.eventRepository = eventRepository;
        this.loanRepository = loanRepository;
    }

    /**
     * Get mapping for /api/event/{eid}/expenses to get all expenses in event
     *
     * @param eid the eid specified in the path
     * @return   list of all expenses in event
     */
    @GetMapping(path = { "", "/" })
    public ResponseEntity<List<Expense>> getAll(@PathVariable("eid") long eid) {
        if (!eventRepository.existsById(eid)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(eventRepository.findById(eid).get().getExpenses());
    }

    /**
     * Get mapping for /api/events/{eid}/expenses/{id} to get a expense by id
     *
     * @param eid the eid specified in the path
     * @param id the id specified in the path
     * @return   response expense entity specified by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getById(@PathVariable("eid") long eid,
                                           @PathVariable("id") long id) {
        if (!expenseRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        if (!eventRepository.existsById(eid)) {
            return ResponseEntity.notFound().build();
        }

        Event ev = eventRepository.findById(eid).get();
        Expense ex = expenseRepository.findById(id).get();

        if (!ev.getExpenses().contains(ex)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ex);
    }

    /**
     * Post mapping for /api/events/{eid}/expenses to create a new expense
     *
     * @param eid the eid specified in the path
     * @param expense the expense object provided in the request body
     * @return response expense entity that was created
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Expense> add(@PathVariable("eid") long eid,
                                       @RequestBody Expense expense) {
        if (isNullOrEmpty(expense.name)
                || isNullOrEmpty(expense.date.toString())) {
            return ResponseEntity.badRequest().build();
        }
        if (!eventRepository.existsById(eid)) {
            return ResponseEntity.notFound().build();
        }

        Event ev = eventRepository.findById(eid).get();
        Expense saved = expenseRepository.save(expense);
        ev.addExpense(expenseRepository.findById(saved.id).get());
        ev.setLastActivity(null);
        eventRepository.save(ev);

        try {
            messagingTemplate.convertAndSend("/topic/event", saved);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(saved);
    }

    /**
     * Delete mapping for /api/events/{eid}/expenses/ to delete an expense by its unique id.
     *
     * @param eid the eid specified in the path
     * @param id the id specified in the path
     * @return response indicating success or failure of the performed operation.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("eid") long eid,
                                           @PathVariable("id") long id) {
        if (!expenseRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        if (!eventRepository.existsById(eid)) {
            return ResponseEntity.notFound().build();
        }

        Event ev = eventRepository.findById(eid).get();
        Expense ex = expenseRepository.findById(id).get();
        for (Loan l : ex.loans) {
            ex.loans.remove(l);
            loanRepository.deleteById(l.id);
        }
        ev.getExpenses().remove(ex);
        ev.setLastActivity(null);
        expenseRepository.deleteById(id);
        eventRepository.save(ev);

        try {
            messagingTemplate.convertAndSend("/topic/event", ex);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.noContent().build();
    }

    /**
     * @param eid event id
     * @param id expense id
     * @param expense changed expense
     * @return expense that is added
     */
    @PutMapping("/{id}")
    public ResponseEntity<Expense> putExpense(@PathVariable("eid") long eid,
                                              @PathVariable("id") long id,
                                              @RequestBody Expense expense) {
        deleteById(eid, id);
        Event ev = eventRepository.findById(eid).get();
        ev.setLastActivity(null);
        eventRepository.save(ev);
        return add(eid, expense);
    }
}
