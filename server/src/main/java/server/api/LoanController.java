package server.api;

import commons.Event;
import commons.Expense;
import commons.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.LoanRepository;
import server.database.PersonRepository;

@RestController
@RequestMapping("api/events/{evid}/expenses/{exid}/loans")
public class LoanController {
    private final LoanRepository loanRepository;
    private final EventRepository eventRepository;
    private final ExpenseRepository expenseRepository;
    private final PersonRepository personRepository;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    /**
     * Constructs a LoanController with the loan repository.
     *
     * @param loanRepository    the loan repository
     * @param eventRepository   the event repository
     * @param expenseRepository the expense repository
     * @param personRepository  the person repository
     */
    public LoanController(LoanRepository loanRepository,
                          EventRepository eventRepository,
                          ExpenseRepository expenseRepository,
                          PersonRepository personRepository) {
        this.loanRepository = loanRepository;
        this.eventRepository = eventRepository;
        this.expenseRepository = expenseRepository;
        this.personRepository = personRepository;
    }

    /**
     * Get mapping for /api/events/{evid}/expenses/{exid}/loans/{id} to get a loan by id
     *
     * @param evid the evid specified in the path
     * @param exid the exid specified in the path
     * @param id the id specified in the path
     * @return   response loan entity specified by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Loan> getById(@PathVariable("evid") long evid,
                                        @PathVariable("exid") long exid,
                                        @PathVariable("id") long id) {
        if (!loanRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        if (!eventRepository.existsById(evid)) {
            return ResponseEntity.notFound().build();
        }
        if (!expenseRepository.existsById(exid)) {
            return ResponseEntity.notFound().build();
        }

        Event ev = eventRepository.findById(evid).get();
        Expense ex = expenseRepository.findById(exid).get();
        Loan l = loanRepository.findById(id).get();

        if (!ev.getExpenses().contains(ex)) return ResponseEntity.notFound().build();
        if (!ex.loans.contains(l)) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(l);
    }

    /**
     * Post mapping for /api/events/{evid}/expenses/{exid}/loans/ to create a new loan
     *
     * @param evid the evid specified in the path
     * @param exid the exid specified in the path
     * @param loan the loan object provided in the request body
     * @return response loan entity that was created
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Loan> add(@PathVariable("evid") long evid,
                                    @PathVariable("exid") long exid,
                                    @RequestBody Loan loan) {
        if (!eventRepository.existsById(evid) || !expenseRepository.existsById(exid)) {
            return ResponseEntity.notFound().build();
        }

        if (loan.borrower == null || loan.payer == null) {
            return ResponseEntity.badRequest().build();
        }

        if (!personRepository.existsById(loan.payer.id) ||
                !personRepository.existsById(loan.borrower.id)) {
            return ResponseEntity.badRequest().build();
        }
        loan.id = 0;
        Expense ex = expenseRepository.findById(exid).get();
        Loan saved = loanRepository.save(loan);
        ex.loans.add(loanRepository.findById(saved.id).get());
        expenseRepository.save(ex);

        try {
            messagingTemplate.convertAndSend("/topic/person", saved);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(saved);
    }

    /**
     * Delete mapping for /api/events/{evid}/expenses/{exid}/loans/{id}
     * to delete a loan by their unique id.
     *
     * @param evid the evid specified in the path
     * @param exid the exid specified in the path
     * @param id the id specified in the path
     * @return response indicating success or failure of the performed operation.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("evid") long evid,
                                           @PathVariable("exid") long exid,
                                           @PathVariable("id") long id) {
        if (!loanRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        if (!eventRepository.existsById(evid)) {
            return ResponseEntity.notFound().build();
        }
        if (!expenseRepository.existsById(exid)) {
            return ResponseEntity.notFound().build();
        }

        Event ev = eventRepository.findById(evid).get();
        Expense ex = expenseRepository.findById(exid).get();
        Loan l = loanRepository.findById(id).get();

        if (!ev.getExpenses().contains(ex)) return ResponseEntity.notFound().build();
        if (!ex.loans.contains(l)) return ResponseEntity.notFound().build();

        ex.loans.remove(l);
        expenseRepository.save(ex);
        loanRepository.deleteById(id);

        try {
            messagingTemplate.convertAndSend("/topic/person", l);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.noContent().build();
    }
}
