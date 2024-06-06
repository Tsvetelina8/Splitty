package client.utils;

import commons.Event;
import commons.Expense;
import commons.Loan;
import commons.Person;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServerUtilsTest {

    private ServerUtils sut;
    @Mock
    private Client client;
    @Mock
    private WebTarget target;
    @Mock
    private Invocation.Builder builder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(client.target(anyString())).thenReturn(target);
        when(target.path(anyString())).thenReturn(target);
        when(target.request()).thenReturn(builder);
        when(target.request(anyString())).thenReturn(builder);
        when(builder.accept(anyString())).thenReturn(builder);
        sut = new ServerUtils();
        sut.injectMockClient(client);
    }

    @Test
    void getPersonByIdTest() {
        Person p = new Person();
        when(builder.get(eq(Person.class))).thenReturn(p);
        assertEquals(sut.getPersonById(new Event(), 1), p);
    }

    @Test
    void addPersonTest() {
        Person p = new Person();
        when(builder.post(any(Entity.class), eq(Person.class))).thenReturn(p);
        assertEquals(sut.addPerson(new Event(), p), p);
    }

    @Test
    void deletePersonByIdTest() {
        sut.deletePersonById(new Event(), 1);
        verify(builder, times(1)).delete();
    }

    @Test
    void findEventByIdTest() {
        Event e = new Event();
        when(builder.get(eq(Event.class))).thenReturn(e);
        assertEquals(sut.findEventById(1), e);
    }

    @Test
    void findEventByCodeTest() {
        Event e = new Event();
        when(builder.get(eq(Event.class))).thenReturn(e);
        assertEquals(sut.findEventByCode("AAAAAA"), e);
    }

    @Test
    void createEventTest() {
        Event e = new Event();
        when(builder.post(any(Entity.class), eq(Event.class))).thenReturn(e);
        assertEquals(sut.createEvent("hi"), e);
    }

    @Test
    void importEventTest() {
        when(builder.post(any(Entity.class), eq(Response.class))).thenReturn(null);
        assertNull(sut.importEvent(new Event()));
    }

    @Test
    void addExpenseTest() {
        Expense e = new Expense();
        when(builder.post(any(Entity.class), eq(Expense.class))).thenReturn(e);
        assertEquals(sut.addExpense(new Event(), e), e);
    }

    @Test
    void getParticipantsTest() {
        List<Person> p = Collections.emptyList();
        when(builder.get(any(GenericType.class))).thenReturn(p);
        assertEquals(sut.getParticipants(new Event()), p);
    }

    @Test
    void verifyPasswordTest() {
        Response response = mock(Response.class);
        when(response.readEntity(String.class)).thenReturn(null);
        when(builder.post(any(Entity.class))).thenReturn(response);
        assertNull(sut.verifyPassword("hoi"));
    }

    @Test
    void getAllEventsTest() {
        List<Event> events = Collections.emptyList();
        when(builder.get(any(GenericType.class))).thenReturn(events);
        assertEquals(sut.getAllEvents(), events);
    }

    @Test
    void deleteEventByIdTest() {
        Response response = mock(Response.class);
        when(response.getStatus()).thenReturn(200);
        when(builder.delete()).thenReturn(response);
        assertEquals(sut.deleteEventById(1), 200);
    }

    @Test
    void addLoanTest() {
        Loan l = new Loan();
        when(builder.post(any(Entity.class), eq(Loan.class))).thenReturn(l);
        assertEquals(sut.addLoan(new Event(), new Expense(), l), l);
    }

    @Test
    void updateTitleTest() {
        sut.updateTitle("hyeoi", 1);
        verify(builder, times(1)).put(any(Entity.class));
    }

    @Test
    void deleteExpense() {
        when(builder.delete()).thenReturn(Response.noContent().build());
        sut.deleteExpense(new Event(), new Expense());
        verify(builder, times(1)).delete();
    }

    @Test
    void editExpense() {
        when(builder.put(any(), eq(Expense.class))).thenReturn(new Expense());
        sut.editExpense(new Event(), new Expense());
        verify(builder, times(1)).put(any(Entity.class), eq(Expense.class));
    }

    @Test
    void findEventByTitle() {
        sut.findEventByTitle("hyeoi");
        verify(builder, times(1)).get(eq(Event.class));
    }

    @Test
    void updatePersonTest() {
        sut.updatePerson(new Person(), new Event(), 1);
        verify(builder, times(1)).put(any(Entity.class));
    }

    @Test
    void registerForUpdatesEventTest() {
        final boolean[] done = {false};
        Consumer<Event> c = new Consumer<Event>() {
            @Override
            public void accept(Event event) {
                done[0] = true;
            }
        };
        sut.registerForUpdatesEvent(c);
        assertFalse(sut.EXEC.isTerminated());
    }

    @Test
    void stopThreadTest() {
        sut.registerForUpdatesEvent(new Consumer<Event>() {
            @Override
            public void accept(Event event) {

            }
        });

        sut.stop();
        assertFalse(sut.EXEC.isTerminated());
    }
}