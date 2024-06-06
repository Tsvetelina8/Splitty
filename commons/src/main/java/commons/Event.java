package commons;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;
@Entity
@JsonPropertyOrder({ "ID", "Title", "Invite Code",
    "creationDate", "lastActivity", "Participants", "Expenses"})
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("ID")
    public long id = 0;
    private String title;
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Person> participants;
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Expense> expenses;
    private LocalDateTime creationDate;
    private LocalDateTime lastActivity;
    private String inviteCode;

    /**
     * Constructor, initialises title, lists and creation date
     * @param title The title of the event
     */
    public Event(String title) {
        this.title = title;
        participants = new ArrayList<>();
        expenses = new ArrayList<>();
        creationDate = LocalDateTime.now(ZoneId.systemDefault());
        lastActivity = LocalDateTime.now(ZoneId.systemDefault());
        setInviteCode();
    }

    /**
     * Getter for ID
     * @return ID
     */
    public long getId() {
        return id;
    }

    /**
     * Unused, for object mapper
     */
    @SuppressWarnings("unused")
    public Event() {}

    /**
     * Creates an invite code based on the given seed.
     * @param seed number used to determine the invite code, usually ID
     * @return the invite code as a 6 character long string
     */
    public String createInviteCode(long seed) {
        Random random = new Random(seed);
        final String possibleCharacters = "123456789ABCDEFGHIJKLMNPQRSTUVWXYZ";

        StringBuilder inviteCode = new StringBuilder();
        int length = 6;
        for (int i = 0; i < length; i++) {
            int choice = random.nextInt(0, possibleCharacters.length());
            inviteCode.append(possibleCharacters.charAt(choice));
        }

        return inviteCode.toString();
    }

    /**
     * Sets the invite code
     */
    public void setInviteCode() {
        inviteCode = createInviteCode(title.hashCode());
    }

    /**
     * Gets the invite code
     * @return the invite code
     */
    @JsonGetter("Invite Code")
    public String getInviteCode() {
        return inviteCode;
    }

    /**
     * Gets the title
     * @return the title
     */
    @JsonGetter("Title")
    public String getTitle() {
        return title;
    }

    /**
     * Gets the participants
     * @return the participants
     */
    @JsonGetter("Participants")
    public List<Person> getParticipants() {
        return participants;
    }

    /**
     * Gets the expenses
     * @return the expenses
     */
    @JsonGetter("Expenses")
    public List<Expense> getExpenses() {
        return expenses;
    }

    /**
     * Gets the creation date
     * @return the creation date as a formatted string
     */
    public String getCreationDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return creationDate.format(formatter);
    }

    /**
     * Gets the creation date unformatted
     * @return the creation date in LocalDateTime
     */
    @JsonProperty("creationDate")
    public LocalDateTime getCreationDateUnformatted() {
        return creationDate;
    }

    /**
     * Gets the last activity date
     * @return the last activity date
     */
    public String getLastActivity() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return lastActivity.format(formatter);
    }

    /**
     * Gets the last activity date unformatted
     * @return the last activity date in LocalDateTime
     */
    @JsonProperty("lastActivity")
    public LocalDateTime getLastActivityUnformatted() {
        return lastActivity;
    }

    /**
     * Sets the title of the event
     * @param title The new title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Adds a participant to the event
     * @param p The participant to add
     */
    public void addParticipant(Person p) {
        if(!participants.contains(p)) participants.add(p);
    }

    /**
     * Removes a participant from the event
     * @param p The person to remove
     */
    public void removeParticipant(Person p) {
        participants.remove(p);
    }

    /**
     * Removes all participants, used for database importing
     */
    public void removeAllParticipants() {
        participants.clear();
    }

    /**
     * Adds an expense to the event
     * @param e The expense to add
     */
    public void addExpense(Expense e) {
        if(!expenses.contains(e)) expenses.add(e);
    }

    /**
     * Removes all expenses, used for database importing
     */
    public void removeAllExpenses() {
        expenses.clear();
    }

    /**
     * Sets the last activity date to the current time/the time inputted through the parameter
     * @param timeToSetTo Time to set it to, for testing purposes
     */
    public void setLastActivity(LocalDateTime timeToSetTo) {
        lastActivity = Objects.requireNonNullElseGet(timeToSetTo, LocalDateTime::now);
    }

    /**
     * Function that calculates how much a person has paid
     * @param person The person
     * @return How much the person has paid
     */
    public double calculatePaidAmounts(Person person) {
        if (person == null) return 0;
        double paidAmount = 0;
        for (Expense e : expenses) {
            if(e.payer.equals(person)) paidAmount += e.totalAmount;
        }
        return paidAmount;
    }

    /**
     * Calculates how much each person owes for the event.
     * @return A map where each person is mapped to the respective amount
     * of money they own for the event.
     */
    public Map<Person, Double> calculateOwedAmounts() {
        Map<Person, Double> owedAmountsPerPerson = new HashMap<>();

        for(Expense e: expenses) {
            Map<Person, Double> ownedForExpense = e.calculateOwedAmounts();
            for(Map.Entry<Person, Double> me: ownedForExpense.entrySet()) {
                Person p = me.getKey();
                double amount = me.getValue();
                owedAmountsPerPerson.merge(p, amount, Double::sum);
            }
        }
        return owedAmountsPerPerson;
    }

    /**
     * Settling debts for an event with at most N-1 transfers for a group of N people.
     *
     * @return A list of loans containing transfer instructions indicating how debts are settled.
     */
    public List<Loan> settleDebts() {
        List<Loan> openDebts = new ArrayList<>();
        Map<Person, Double> owedAmounts = this.calculateOwedAmounts();

        List<Person> sortedByOwedAmount = new ArrayList<>(owedAmounts.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .toList());
        Collections.reverse(sortedByOwedAmount);

        while(sortedByOwedAmount.size() > 1) {
            Person minPerson = sortedByOwedAmount.get(0);
            Person maxPerson = sortedByOwedAmount.get(sortedByOwedAmount.size()-1);
            double transferAmount = owedAmounts.get(minPerson);

            openDebts.add(new Loan(transferAmount, maxPerson, minPerson));

            owedAmounts.put(minPerson, owedAmounts.get(minPerson) - transferAmount);
            owedAmounts.put(maxPerson, owedAmounts.get(maxPerson) + transferAmount);

            if (Math.abs(owedAmounts.get(maxPerson)) < 1e-5) {
                sortedByOwedAmount.remove(sortedByOwedAmount.size()-1);
            }
            if (Math.abs(owedAmounts.get(minPerson)) < 1e-5) {
                sortedByOwedAmount.remove(0);
            }
        }

        return openDebts;
    }

    /**
     * Removes a person from the debts map if their debt is equal to zero.
     * @param debts The map of debts
     * @param person The Person with their debt value
     */
    public void removeDebtIfZero(Map<Person, Double> debts, Person person) {
        if(Math.abs(debts.get(person)) < 1e-5) {
            debts.remove(person);
        }
    }

    /**
     * Calculates the total amount that a certain person owes for the event overall.
     * @param p The person for whom to calculate the total amount.
     * @return The total amount that the specified person owns for the whole event.
     * A negative number means that he has to be given that amount of money by the others.
     */
    public double calculateOwedAmountPerPerson(Person p) {
        if(p == null) return 0;
        if(expenses.isEmpty()) return 0;
        try {
            return calculateOwedAmounts().get(p);
        }
        catch(Exception e) {
            return 0;
        }
    }

    /**
     * Calculate the total sum of all expenses in this event
     * @return The total sum of all expenses in the event
     */
    public double calculateTotalExpenses() {
        if(expenses.isEmpty()) {
            return 0;
        }
        double sum = 0;
        for(Expense e: expenses) {
            sum += e.totalAmount;
        }
        return sum;
    }

    /**
     * Compares for equality with other object
     * @param obj The object to compare this to
     * @return true if this is equal to obj
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Event)) return false;
        return id == ((Event) obj).id;
    }

    /**
     * Creates a hashcode for this object
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return (int)id;
    }

    /**
     * Formats this object as a string
     * @return the formatted string
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
