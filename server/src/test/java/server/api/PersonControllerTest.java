package server.api;

import commons.Event;
import commons.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import server.api.mocks.MockEventRepository;
import server.api.mocks.MockPersonRepository;


import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NO_CONTENT;

public class PersonControllerTest {
    private MockPersonRepository repo;
    private MockEventRepository eventRepo;
    private Event event;
    private PersonController sut;
    private Person person;

    @BeforeEach
    public void setup() {
        repo = new MockPersonRepository();
        eventRepo = new MockEventRepository();
        sut = new PersonController(repo, eventRepo);
        event = new Event("abcd");
        person = new Person("John", "Doe");
        event.addParticipant(person);
        event.id = 123;
        eventRepo.save(event);
        repo.save(person);
    }

    @Test
    void getAllTest() {
        assertEquals(sut.getAll(event.id), ResponseEntity.ok(Collections.singletonList(person)));
    }

    @Test
    void getAllTestFalseEvent() {
        assertEquals(sut.getAll(event.id - 1), ResponseEntity.notFound().build());
    }

    @Test
    public void cannotAddNullFirstName() {
        var actual = sut.add(event.id, getPerson(null, "Doe"));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void cannotAddNullLastName() {
        var actual = sut.add(event.id, getPerson("Jane", null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void addFalseEvent() {
        assertEquals(ResponseEntity.notFound().build(), sut.add(event.id - 1, person));
    }

    @Test
    public void deletePersonById() {
        Person person = getPerson("Jane", "Doe");
        sut.add(event.id, person);

        var actual = sut.deleteById(event.id, person.id);
        assertEquals(NO_CONTENT, actual.getStatusCode());
    }

    @Test
    public void deletePersonByIdFalsePerson() {
        assertEquals(ResponseEntity.notFound().build(), sut.deleteById(event.id, person.id - 1));
    }

    @Test
    public void deletePersonByIdFalseEvent() {
        assertEquals(ResponseEntity.notFound().build(), sut.deleteById(event.id - 1, person.id));
    }

    @Test
    public void getPersonById() {
        Person person = getPerson("Jane", "Doe");
        sut.add(event.id, person);

        Person actual = sut.getById(event.id, person.id).getBody();
        assertEquals(person, actual);
    }

    @Test
    public void getPersonByIdFalseEvent() {
        assertEquals(sut.getById(event.id - 1, person.id), ResponseEntity.notFound().build());
    }

    @Test
    public void getPersonByIdFalsePerson() {
        assertEquals(sut.getById(event.id, person.id - 1), ResponseEntity.notFound().build());
    }

    @Test
    public void getPersonByIdFalsePersonAndEvent() {
        Person p = new Person("Jane", "Doe");
        repo.save(p);
        assertEquals(sut.getById(event.id, p.id), ResponseEntity.notFound().build());
    }


    @Test
    public void databaseIsUsed() {
        sut.add(event.id, getPerson("Jane", "Doe"));
        assertTrue(repo.calledMethods.contains("save"));
    }

    private static Person getPerson(String q1, String q2) {
        return new Person(q1, q2);
    }

    @Test
    void editByIdTest() {
        Person p = sut.editById(event.id, person.id, new Person("Jane", "Doe")).getBody();
        assertEquals(p.firstName, "Jane");
        assertEquals(p.lastName, "Doe");
    }

    @Test
    void editByIdTestFalseEvent() {
        assertEquals(sut.editById(event.id - 1, person.id, new Person("Jane", "Doe")), ResponseEntity.notFound().build());
    }

    @Test
    void editByIdTestFalsePerson() {
        assertEquals(sut.editById(event.id, person.id - 1, new Person("Jane", "Doe")), ResponseEntity.notFound().build());
    }

}
