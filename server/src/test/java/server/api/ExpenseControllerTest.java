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
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ExpenseControllerTest {
    private MockExpenseRepository expenseRepo;
    private MockEventRepository eventRepo;
    private MockLoanRepository loanRepo;
    private Event event;
    private Expense expense;

    private ExpenseController sut;

    @BeforeEach
    public void setup() {
        expenseRepo = new MockExpenseRepository();
        eventRepo = new MockEventRepository();
        loanRepo = new MockLoanRepository();
        sut = new ExpenseController(expenseRepo, eventRepo, loanRepo);
        event = new Event("abcd");
        expense = new Expense("s", "b", LocalDateTime.now(), new Person(), 200);
        event.addExpense(expense);
        eventRepo.save(event);
        expenseRepo.save(expense);
    }

    @Test
    public void cannotAddNullFirstName() {
        var actual = sut.add(event.id,
                getExpense(null, "b", LocalDateTime.now(), "k", "l", "j", "l", 200));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    private static Expense getExpense(String name, String desc, LocalDateTime time, String n,
                                      String l, String p, String d, int a) {
        Person pers = new Person(n, l);
        Person pers2 = new Person(p, d);
        ArrayList<Loan> loans = new ArrayList<>();
        Loan loan = new Loan(a, pers, pers2);
        loans.add(loan);

        return new Expense(name, desc, time, pers, a);
    }

    @Test
    void getAllTest() {
        assertEquals(sut.getAll(event.id), ResponseEntity.ok(Collections.singletonList(expense)));
    }

    @Test
    void getAllTestFalse() {
        assertEquals(sut.getAll(event.id - 1), ResponseEntity.notFound().build());
    }


    @Test
    void getByIdTest() {
        assertEquals(sut.getById(event.id, expense.id), ResponseEntity.ok(expense));
    }

    @Test
    void getByIdTestFalseExpense() {
        assertEquals(sut.getById(event.id, expense.id - 1), ResponseEntity.notFound().build());
    }

    @Test
    void getByIdTestFalseEvent() {
        assertEquals(sut.getById(event.id - 1, expense.id), ResponseEntity.notFound().build());
    }

    @Test
    void getByIdTestFalseEventAndExpense() {
        Event e = new Event("new");
        eventRepo.save(e);
        assertEquals(sut.getById(e.id, expense.id), ResponseEntity.notFound().build());
    }

    @Test
    void addTest() {
        Expense e = new Expense("a", "b", LocalDateTime.now(), new Person(), 200);
        event.addExpense(e);
        assertEquals(sut.add(event.id, e), ResponseEntity.ok(e));
    }

    @Test
    void addTestEmptyTitle() {
        assertEquals(sut.add(event.id, new Expense()), ResponseEntity.badRequest().build());
    }

    @Test
    void addTestBadEvent() {
        Expense e = new Expense("a", "b", LocalDateTime.now(), new Person(), 200);
        event.addExpense(e);
        assertEquals(sut.add(event.id - 1, e), ResponseEntity.notFound().build());
    }

    @Test
    void deleteByIdTest() {
        assertEquals(sut.deleteById(event.id, expense.id), ResponseEntity.noContent().build());
    }

    @Test
    void deleteByIdTestFalseExpense() {
        assertEquals(sut.deleteById(event.id, expense.id - 1), ResponseEntity.notFound().build());
    }

    @Test
    void deleteByIdTestFalseEvent() {
        assertEquals(sut.deleteById(event.id - 1, expense.id), ResponseEntity.notFound().build());
    }

    @Test
    public void putTest() {
        Expense e = getExpense("tester", "b", LocalDateTime.now(), "k", "l", "j", "l", 200);
        Expense f = getExpense("tester", "c", LocalDateTime.now(), "k", "l", "j", "l", 200);
        sut.add(event.id, e);
        sut.putExpense(event.id, e.id, f);

        ArrayList<Expense> ex = (ArrayList<Expense>) event.getExpenses();
        assertTrue(ex.contains(f));
    }
}
