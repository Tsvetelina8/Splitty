package server.api;

import commons.Event;
import commons.Expense;
import commons.Loan;
import commons.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import server.api.mocks.MockEventRepository;
import server.api.mocks.MockExpenseRepository;
import server.api.mocks.MockLoanRepository;
import server.api.mocks.MockPersonRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoanControllerTest {

    private MockEventRepository eventRepo;
    private MockExpenseRepository expenseRepo;
    private MockPersonRepository personRepo;
    private MockLoanRepository loanRepo;
    private LoanController sut;

    private Event event;
    private Loan loan;
    private Expense expense;

    @BeforeEach
    void setUp() {
        eventRepo = new MockEventRepository();
        expenseRepo = new MockExpenseRepository();
        personRepo = new MockPersonRepository();
        loanRepo = new MockLoanRepository();

        event = new Event("Test Event");
        expense = new Expense("a", "b", LocalDateTime.now(), new Person(), 200);
        loan = new Loan(100, new Person(), new Person());
        expense.loans = new ArrayList<>();
        expense.loans.add(loan);
        event.addExpense(expense);

        expenseRepo.save(expense);
        eventRepo.save(event);
        loanRepo.save(loan);

        sut = new LoanController(loanRepo, eventRepo, expenseRepo, personRepo);
    }

    @Test
    void getByIdTest() {
        assertEquals(sut.getById(event.id, expense.id, loan.id), ResponseEntity.ok(loan));
    }

    @Test
    void getByIdTestFalseEvent() {
        assertEquals(sut.getById(event.id - 1, expense.id, loan.id), ResponseEntity.notFound().build());
    }

    @Test
    void getByIdTestFalseExpense() {
        assertEquals(sut.getById(event.id , expense.id - 1, loan.id), ResponseEntity.notFound().build());
    }

    @Test
    void getByIdTestFalseLoan() {
        assertEquals(sut.getById(event.id, expense.id, loan.id - 1), ResponseEntity.notFound().build());
    }

    @Test
    void getByIdTestFalseEventAndExpense() {
        Event e = new Event("Test Event");
        Expense e1 = new Expense("a", "b", LocalDateTime.now(), new Person(), 200);
        eventRepo.save(e);
        expenseRepo.save(e1);
        assertEquals(sut.getById(e.id, e1.id, loan.id), ResponseEntity.notFound().build());
    }

    @Test
    void getByIdTestFalseExpenseAndLoan() {
        Loan l = new Loan(1100, new Person("ee", "ee"), new Person());
        l.id = 123;
        loanRepo.save(l);
        assertEquals(sut.getById(event.id, expense.id, 123), ResponseEntity.notFound().build());
    }

    @Test
    void addTest() {
        Person p = new Person("ee", "ee");
        Person p2 = new Person("si", "si");
        Loan l = new Loan(1100, p, p2);
        personRepo.save(p);
        personRepo.save(p2);
        assertEquals(sut.add(event.id, expense.id, l), ResponseEntity.ok(l));
    }

    @Test
    void addTestFalseEvent() {
        Person p = new Person("ee", "ee");
        Person p2 = new Person("si", "si");
        Loan l = new Loan(1100, p, p2);
        personRepo.save(p);
        personRepo.save(p2);
        assertEquals(sut.add(event.id - 1, expense.id, l), ResponseEntity.notFound().build());
    }

    @Test
    void addTestFalseExpense() {
        Person p = new Person("ee", "ee");
        Person p2 = new Person("si", "si");
        Loan l = new Loan(1100, p, p2);
        personRepo.save(p);
        personRepo.save(p2);
        assertEquals(sut.add(event.id, expense.id - 1, l), ResponseEntity.notFound().build());
    }

    @Test
    void addTestFalsePerson1() {
        Person p = new Person("ee", "ee");
        Person p2 = new Person("si", "si");
        Loan l = new Loan(1100, null, p2);
        personRepo.save(p);
        personRepo.save(p2);
        assertEquals(sut.add(event.id, expense.id, l), ResponseEntity.badRequest().build());
    }

    @Test
    void addTestFalsePerson2() {
        Person p = new Person("ee", "ee");
        Person p2 = new Person("si", "si");
        Loan l = new Loan(1100, p, null);
        personRepo.save(p);
        personRepo.save(p2);
        assertEquals(sut.add(event.id, expense.id, l), ResponseEntity.badRequest().build());
    }

    @Test
    void addTestFalsePerson1DB() {
        Person p = new Person("ee", "ee");
        Person p2 = new Person("si", "si");
        p.id = 123;
        Loan l = new Loan(1100, p, p2);
        personRepo.save(p2);
        assertEquals(sut.add(event.id, expense.id, l), ResponseEntity.badRequest().build());
    }

    @Test
    void addTestFalsePerson2DB() {
        Person p = new Person("ee", "ee");
        Person p2 = new Person("si", "si");
        p2.id = 123;
        Loan l = new Loan(1100, p, p2);
        personRepo.save(p);
        assertEquals(sut.add(event.id, expense.id, l), ResponseEntity.badRequest().build());
    }

    @Test
    void deleteByIdTest() {
        assertEquals(sut.deleteById(event.id, expense.id, loan.id), ResponseEntity.noContent().build());
    }

    @Test
    void deleteByIdTestFalseEvent() {
        assertEquals(sut.deleteById(event.id - 1, expense.id, loan.id), ResponseEntity.notFound().build());
    }

    @Test
    void deleteByIdTestFalseExpense() {
        assertEquals(sut.deleteById(event.id , expense.id - 1, loan.id), ResponseEntity.notFound().build());
    }

    @Test
    void deleteByIdTestFalseLoan() {
        assertEquals(sut.deleteById(event.id, expense.id, loan.id - 1), ResponseEntity.notFound().build());
    }

    @Test
    void deleteByIdTestFalseEventAndExpense() {
        Event e = new Event("Test Event");
        Expense e1 = new Expense("a", "b", LocalDateTime.now(), new Person(), 200);
        eventRepo.save(e);
        expenseRepo.save(e1);
        assertEquals(sut.deleteById(e.id, e1.id, loan.id), ResponseEntity.notFound().build());
    }

    @Test
    void deleteByIdTestFalseExpenseAndLoan() {
        Loan l = new Loan(1100, new Person("ee", "ee"), new Person());
        l.id = 123;
        loanRepo.save(l);
        assertEquals(sut.deleteById(event.id, expense.id, 123), ResponseEntity.notFound().build());
    }
}
