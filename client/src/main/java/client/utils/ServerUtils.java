/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import commons.Expense;
import commons.Loan;
import commons.Person;
import commons.Event;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ServerUtils {

    private String server;
    private Client client = ClientBuilder.newClient(new ClientConfig());

    /**
     * Default constructor for server utilities class
     */
    public ServerUtils() {
        AppConfig config = new AppConfig();
        server = config.getServerUrl();
    }
    /**
     * Client injector method used for testing
     * @param client The mocked client to inject
     */
    public void injectMockClient(Client client) {
        this.client = client;
    }

    /**
     * Retrieves a person from the server using JAX-RS Client API.
     * This method is a higher-level approach to access the server's RESTful service.
     *
     * @param event the event to which this person belongs to
     * @param id the ID of the person to be retrieved.
     * @return A {@link Person} object retrieved from the server by its unique id.
     */
    public Person getPersonById(Event event, long id) {
        return client
                .target(server)
                .path("api/events/" + event.id + "/persons/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Person.class);
    }

    /**
     * Adds a new person to the server.
     * This method sends a POST request to the server with the person data.
     *
     * @param event the event to which this person belongs to
     * @param person The {@link Person} object to be added to the server.
     * @return The {@link Person} object as returned by the server after adding it.
     */
    public Person addPerson(Event event, Person person) {
        return client
                .target(server).path("api/events/" + event.id + "/persons/")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(person, APPLICATION_JSON), Person.class);
    }

    /**
     * Deletes a person from the server by their unique id.
     *
     * @param event the event to which this person belongs to
     * @param id The ID of the Person to be removed.
     */
    public void deletePersonById(Event event, long id) {
        client
                .target(server)
                .path("api/events/" + event.id + "/persons/" + id)
                .request()
                .delete();
    }

    /**
     * Updates person
     * @param person new person
     * @param id id of event
     * @param event the event the person is in
     */
    public void updatePerson(Person person, Event event, long id) {
        client.target(server)
                .path("api/events/" + event.id + "/persons/" + id)
                .request(APPLICATION_JSON)
                .put(Entity.entity(person, APPLICATION_JSON));
    }

    /**
     * Finds an event by the event ID
     *
     * @param id The id of the event to be found
     * @return The {@link Event} object as found by the server
     */
    public Event findEventById(long id) {
        return client
                .target(server)
                .path("api/events/" + id)
                .request()
                .get(Event.class);
    }

    /**
     * Finds an event by the event invite code
     *
     * @param code The invite code of the event to be found
     * @return The {@link Event} object as found by the server
     */
    public Event findEventByCode(String code) {
        return client
                .target(server)
                .path("api/events/code/" + code)
                .request()
                .get(Event.class);
    }

    /**
     * Finds an event by the event title
     *
     * @param title The title of the event to be found
     * @return The {@link Event} object as found by the server
     */
    public Event findEventByTitle(String title) {
        return client
                .target(server)
                .path("api/events/title/" + title)
                .request()
                .get(Event.class);
    }

    /**
     * Updates event title
     * @param title new title
     * @param id id of event
     */
    public void updateTitle(String title, long id) {
        client.target(server)
                .path("api/events/" + id)
                .request()
                .put(Entity.entity(title, APPLICATION_JSON));
    }

    /**
     * Creates an event on the server while giving it a title
     *
     * @param title The title of the new event
     * @return The {@link Event} object as created by the server
     */
    public Event createEvent(String title) {
        return client
                .target(server).path("api/events/")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(title, APPLICATION_JSON), Event.class);
    }

    /**
     * Imports an event on the server
     *
     * @param newEvent The event to import
     * @return The {@link Event} object as created by the server
     */
    public Response importEvent(Event newEvent) {
        return client
                .target(server).path("api/events/json")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(newEvent, APPLICATION_JSON), Response.class);
    }

    /**
     * @param event the event to which this expense belongs to
     * @param expense the expense to add
     * @return expense
     */
    public Expense addExpense(Event event, Expense expense) {
        return client
                .target(server).path("api/events/" + event.id + "/expenses/")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(expense, APPLICATION_JSON), Expense.class);
    }

    /**
     * @param event   the event to which this expense belongs to
     * @param expense the expense to delete
     * @return expense
     */
    public Response deleteExpense(Event event, Expense expense) {
        return client
                .target(server).path("api/events/" + event.id + "/expenses/" + expense.id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
    }

    /**
     * @param event   the event to which this expense belongs to
     * @param expense the expense to edit
     * @return the edited expense
     */
    public Expense editExpense(Event event, Expense expense) {
        return client
                .target(server).path("api/events/" + event.id + "/expenses/" + expense.id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(expense, APPLICATION_JSON), Expense.class);
    }

    /**
     * @param event event which participants to get
     * @return list of participants
     */
    public List<Person> getParticipants(Event event) {
        return client
                .target(server)
                .path("api/events/" + event.id + "/persons/")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    /**
     * Verifies the inputted password
     *
     * @param input The inputted password
     * @return A string containing the response from the server
     */
    public String verifyPassword(String input) {
        Response response = client
                .target(server).path("api/password/")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(input, APPLICATION_JSON));
        return response.readEntity(String.class);
    }

    /**
     * Sends a GET request to get all events on the server
     *
     * @return A list containing all events on the server
     */
    public List<Event> getAllEvents() {
        return client
                .target(server)
                .path("api/events/")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    public static final ExecutorService EXEC = Executors.newSingleThreadExecutor();

    /**
     * register for updates of the event title using long polling
     *
     * @param consumer consumer for the data
     *
     */
    public void registerForUpdatesEvent(Consumer<Event> consumer) {

        EXEC.submit(() -> {
            while(!Thread.interrupted()) {
                var res = client
                        .target(server)
                        .path("api/events/updatesTitle")
                        .request(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .get(Response.class);

                if(res.getStatus() == 204) {

                    continue;
                }

                var r = res.readEntity(Event.class);
                consumer.accept(r);
            }

        });

    }

    /**
     * stop method to kill the thread that handles long polling
     */
    public void stop() {
        EXEC.shutdownNow();
    }

    /**
     * Deletes an event from the server by their unique id.
     *
     * @param id The ID of the Event to be removed.
     * @return http status code if successful or not
     */
    public int deleteEventById(long id) {
        Response response = client
                .target(server)
                .path("api/events/" + id)
                .request()
                .delete();
        return response.getStatus();
    }

    /**
     * @param event the event to which this loan belongs to
     * @param expense the expense to which this loan belongs to
     * @param loan loan that has to be added
     * @return loan that has been added
     */
    public Loan addLoan(Event event, Expense expense, Loan loan) {
        return client
                .target(server).path("api/events/" + event.id
                                    + "/expenses/" + expense.id
                                    + "/loans/")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(loan, APPLICATION_JSON), Loan.class);
    }
}