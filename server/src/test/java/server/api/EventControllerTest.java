package server.api;

import commons.Event;
import commons.Expense;
import commons.Loan;
import commons.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import server.api.mocks.MockEventRepository;
import server.api.mocks.MockExpenseRepository;
import server.api.mocks.MockLoanRepository;
import server.api.mocks.MockPersonRepository;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

class EventControllerTest {
    private MockEventRepository eventRepo;
    private MockExpenseRepository expenseRepo;
    private MockPersonRepository personRepo;
    private MockLoanRepository loanRepo;


    private EventController sut;

    @BeforeEach
    public void setup() {
        eventRepo = new MockEventRepository();
        expenseRepo = new MockExpenseRepository();
        personRepo = new MockPersonRepository();
        loanRepo = new MockLoanRepository();
        sut = new EventController(expenseRepo, personRepo, eventRepo, loanRepo);
    }

    @Test
    public void cannotAddNullTitle() {
        var actual = sut.add(null);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void cannotAddWhitespaceTitle() {
        var actual = sut.add(" ");
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void cannotAddSameTitle() {
        sut.add("title");
        var response = sut.add("title");
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void cannotUpdateSameTitle() {
        sut.add("title");
        var e = sut.add("title2").getBody();
        var response = sut.updateTitleById(e.id, "title");
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void findByCodeWorks() {
        var event = sut.add("title").getBody();
        var response = sut.getByInviteCode(event.getInviteCode());
        assertEquals(response.getBody(), event);
    }

    @Test
    public void findByCodeInvalid() {
        ResponseEntity<Event> response = sut.getByInviteCode(null);
        assertEquals(response.getStatusCode(), BAD_REQUEST);
    }


    @Test
    public void databaseIsUsed() {
        sut.add("e1");
        assertTrue(eventRepo.calledMethods.contains("save"));
    }

    @Test
    public void titleUpdated() {
        var event = sut.add("e1").getBody();
        assertNotNull(event);

        sut.updateTitleById(event.id, "e2");
        var newTitle = eventRepo.find(event.id).get().getTitle();

        assertEquals("e2", newTitle);
    }

    @Test
    public void updateShouldntCreateNew() {
        var event = sut.add("e1").getBody();
        long count1 = eventRepo.count();

        sut.updateTitleById(event.id, "e2");
        long count2 = eventRepo.count();

        assertEquals(count1, count2);
    }

    @Test
    public void updateInvalid(){
        assertEquals(sut.updateTitleById(1, "").getStatusCode(), BAD_REQUEST);
    }

    @Test
    public void importEventAddTest() {
        Event testEvent = new Event("testEvent");
        Person p = new Person("jeffrey", "bezos");
        testEvent.addParticipant(p);
        testEvent.addExpense(
                new Expense("amongus merch", "susys bakas",
                        LocalDateTime.now(), p, 69420 ));
        ResponseEntity<Event> response = sut.importEventAdd(testEvent);
        assertEquals(response.getStatusCode(), OK);
        assertTrue(eventRepo.existsEventByTitle("testEvent"));
    }

    @Test
    public void importEventAddTestAlreadyExists() {
        Event testEvent = new Event("testEvent");
        sut.importEventAdd(testEvent);
        ResponseEntity<Event> response = sut.importEventAdd(testEvent);
        assertEquals(response.getStatusCode(), BAD_REQUEST);
        assertTrue(eventRepo.existsEventByTitle("testEvent"));
    }

    @Test
    public void deleteByIdTest() {
        Event testEvent = new Event("testEvent");
        Person p = new Person("jeffrey", "bezos");
        testEvent.addParticipant(p);
        Expense e = new Expense("e", "susys bakas",
                LocalDateTime.now(), p, 69420 );
        e.loans.add(new Loan());
        testEvent.addExpense(e);
        testEvent = sut.importEventAdd(testEvent).getBody();
        sut.deleteById(testEvent.id);
        assertFalse(eventRepo.existsById(testEvent.id));
    }

    @Test
    public void deleteByIdTestInvalid() {
        ResponseEntity<Event> response = sut.deleteById(-21093458);
        assertEquals(response.getStatusCode(), BAD_REQUEST);
    }

    @Test
    public void getByIdTest() {
        Event testEvent = sut.add("test").getBody();
        assertEquals(sut.getById(testEvent.id).getBody(), testEvent);
    }

    @Test
    public void getByIdTestInvalid() {
        ResponseEntity<Event> response = sut.getById(-21093458);
        assertEquals(response.getStatusCode(), BAD_REQUEST);
    }

    @Test
    public void getAllTest() {
        Event testEvent = sut.add("test").getBody();
        assertEquals(sut.getAll(), Collections.singletonList(testEvent));
    }
}