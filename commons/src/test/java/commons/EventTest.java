package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    private Event event1;
    private Event event2;
    private Person person1;
    private Person person2;
    private Person person3;
    @BeforeEach
    void setup() {
        event1 = new Event("testEvent");
        event2 = new Event("testEvent2");
        person1 = new Person("Jeffrey", "Bezos");
        person2 = new Person("Among", "Us");
        person3 = new Person("a", "b");
        person1.id = 0;
        person2.id = 1;
        person3.id = 2;
        event1.id = 0;
        event2.id = 1;
    }

    @Test
    void testInit() {
        assertNotNull(event1);
        assertEquals(event1.getTitle(), "testEvent");
        assertNotNull(event1.getParticipants());
        assertNotNull(event1.getExpenses());
        assertNotNull(event1.getCreationDate());
        assertNotNull(event1.getCreationDateUnformatted());
        assertNotNull(event1.getLastActivityUnformatted());
        assertNotNull(event1.getLastActivity());
    }
    @Test
    void testParticipants() {
        event1.addParticipant(person1);
        assertTrue(event1.getParticipants().contains(person1));
        assertFalse(event1.getParticipants().contains(person2));
    }

    @Test
    void testSetTitle() {
        event1.setTitle("testEvent2");
        assertEquals(event1.getTitle(), "testEvent2");
    }

    @Test
    void testAddExpense() {
        Expense exp = new Expense("test", "test",
                LocalDateTime.now(ZoneId.systemDefault()), person1, 200);
        event1.addExpense(exp);
        assertTrue(event1.getExpenses().contains(exp));
    }

    @Test
    void testSetGetLastActivity() {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        event1.setLastActivity(time);
        assertEquals(event1.getLastActivity(), time.format(formatter));
    }

    @Test
    void testInviteIsConsistent() {
        String code1 = event1.createInviteCode(1);
        String code2 = event1.createInviteCode(1);
        assertEquals(code1, code2);
    }

    @Test
    void testInviteIsDifferent() {
        String code1 = event1.createInviteCode(1);
        String code2 = event1.createInviteCode(2);
        assertNotEquals(code1, code2);
    }

    @Test
    void testRemoveParticipants() {
        event1.addParticipant(person1);
        event1.removeParticipant(person1);
        assertFalse(event1.getParticipants().contains(person1));
    }

    @Test
    void testGetInviteCode(){
        assertNotNull(event1.getInviteCode());
    }

    @Test
    void testCalculateTotalExpenses() {
        Loan l1 = new Loan(200, person1, person2);
        Loan l2 = new Loan(300, person2, person1);
        Loan l3 = new Loan(100, person1, person2);
        l1.id = 0;
        l2.id = 1;
        l3.id = 2;
        Expense exp1 = new Expense("test1", "test1",
                LocalDateTime.now(ZoneId.systemDefault()), person1, 200);
        Expense exp2 = new Expense("test2", "test2",
                LocalDateTime.now(ZoneId.systemDefault()), person2, 300);
        Expense exp3 = new Expense("test3", "test3",
                LocalDateTime.now(ZoneId.systemDefault()), person1, 100);
        exp1.id = 0;
        exp2.id = 1;
        exp3.id = 2;

        event1.addExpense(exp1);
        event1.addExpense(exp2);
        event1.addExpense(exp3);

        assertEquals(event1.calculateTotalExpenses(), 600.0);
    }

    @Test
    void testGetTotalExpensesNull() {
        assertEquals(event2.calculateTotalExpenses(), 0);
    }
    @Test
    void testCalculateOwedAmounts() {
        Loan l1 = new Loan(200, person1, person2);
        Loan l2 = new Loan(300, person3, person1);
        Loan l3 = new Loan(100, person3, person2);
        Loan l4 = new Loan(50, person1, person3);
        l1.id = 0;
        l2.id = 1;
        l3.id = 2;
        l4.id = 3;
        Expense exp1 = new Expense("test1", "test1", LocalDateTime.now(ZoneId.systemDefault()), person1, 250);
        exp1.loans.addAll(List.of(new Loan[]{l1, l4}));
        Expense exp2 = new Expense("test2", "test2", LocalDateTime.now(ZoneId.systemDefault()), person3, 300);
        exp2.loans.add(l2);
        Expense exp3 = new Expense("test3", "test3", LocalDateTime.now(ZoneId.systemDefault()), person3, 100);
        exp3.loans.add(l3);
        exp1.id = 0;
        exp2.id = 1;
        exp3.id = 2;

        event1.addExpense(exp1);
        event1.addExpense(exp2);
        event1.addExpense(exp3);
        event1.addParticipant(person1);
        event1.addParticipant(person2);
        event1.addParticipant(person3);

        Map<Person, Double> map = new HashMap<>();
        map.put(person1, 50.0);
        map.put(person2, 300.0);
        map.put(person3, -350.0);

        assertEquals(map, event1.calculateOwedAmounts());
    }

    @Test
    void testSettleDebtsSimple() {
        Loan l1 = new Loan(200, person1, person2);
        Loan l4 = new Loan(50, person1, person3);
        l4.id = 1;
        Expense exp1 = new Expense("test1", "test1", LocalDateTime.now(ZoneId.systemDefault()), person1, 250);
        exp1.loans.addAll(List.of(new Loan[]{l1, l4}));

        event1.addExpense(exp1);
        event1.addParticipant(person1);
        event1.addParticipant(person2);
        event1.addParticipant(person3);

        List<Loan> debtList = event1.settleDebts();
        debtList.get(1).id=1;
        assertTrue(debtList.contains(l1));
        assertTrue(debtList.contains(l4));
    }
    @Test
    void testSettleDebtsComplex() {
        Loan l1 = new Loan(200, person1, person2);
        Loan l2 = new Loan(300, person3, person1);
        Loan l3 = new Loan(100, person3, person2);
        Loan l4 = new Loan(50, person1, person3);
        l1.id = 0;
        l2.id = 1;
        l3.id = 2;
        l4.id = 3;
        Expense exp1 = new Expense("test1", "test1", LocalDateTime.now(ZoneId.systemDefault()), person1, 250);
        exp1.loans.addAll(List.of(new Loan[]{l1, l4}));
        Expense exp2 = new Expense("test2", "test2", LocalDateTime.now(ZoneId.systemDefault()), person3, 300);
        exp2.loans.add(l2);
        Expense exp3 = new Expense("test3", "test3", LocalDateTime.now(ZoneId.systemDefault()), person3, 100);
        exp3.loans.add(l3);
        exp1.id = 0;
        exp2.id = 1;
        exp3.id = 2;

        event1.addExpense(exp1);
        event1.addExpense(exp2);
        event1.addExpense(exp3);
        event1.addParticipant(person1);
        event1.addParticipant(person2);
        event1.addParticipant(person3);

        Loan l5 = new Loan(50.0, person1, person3);
        Loan l6 = new Loan(300.0, person2, person3);
        l5.id = 5;
        l6.id = 6;

        List<Loan> debtList = event1.settleDebts();
        debtList.get(0).id=5;
        debtList.get(1).id=6;

        assertTrue(debtList.contains(l5));
        assertTrue(debtList.contains(l6));
    }
   @Test
   void testOwedAmountPerPerson() {
       Loan l1 = new Loan(200, person1, person2);
       Loan l2 = new Loan(300, person3, person1);
       Loan l3 = new Loan(100, person3, person2);
       l1.id = 0;
       l2.id = 1;
       l3.id = 2;
       Expense exp1 = new Expense("test1", "test1", LocalDateTime.now(ZoneId.systemDefault()), person1, 200);
       exp1.loans.add(l1);
       Expense exp2 = new Expense("test2", "test2", LocalDateTime.now(ZoneId.systemDefault()), person3, 300);
       exp2.loans.add(l2);
       Expense exp3 = new Expense("test3", "test3", LocalDateTime.now(ZoneId.systemDefault()), person3, 100);
       exp3.loans.add(l3);exp1.id = 0;
       exp2.id = 1;
       exp3.id = 2;

       event1.addExpense(exp1);
       event1.addExpense(exp2);
       event1.addExpense(exp3);
       event1.addParticipant(person1);
       event1.addParticipant(person2);
       event1.addParticipant(person3);

       assertEquals(event1.calculateOwedAmountPerPerson(person1), 100.0);
       assertEquals(event1.calculateOwedAmountPerPerson(person2), 300.0);
       assertEquals(event1.calculateOwedAmountPerPerson(person3), -400.0);
   }
    @Test
    void testEquals_False() {
        assertNotEquals(event1, event2);
    }

    @Test
    void testHashCode() {
        assertNotEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void testToString() {
        assertNotEquals(event1.toString(), event2.toString());
    }

    @Test
    void testRemoveAllParticipants() {
        event1.removeAllParticipants();
        assertTrue(event1.getParticipants().isEmpty());
    }

    @Test
    void testRemoveAllExpenses() {
        event1.removeAllExpenses();
        assertTrue(event1.getExpenses().isEmpty());
    }

    @Test
    void calculatePaidAmounts() {
        event1.addParticipant(person1);
        event1.addExpense(new Expense("a" ,"a", LocalDateTime.now(), person1, 100.0));
        assertEquals(event1.calculatePaidAmounts(person1), 100.0);
    }

    @Test
    void calculatePaidAmountsTestNull() {
        assertEquals(event1.calculatePaidAmounts(null), 0);
    }

    @Test
    void calculateOwedAmountPerPersonTestNullPerson() {
        assertEquals(event1.calculateOwedAmountPerPerson(null), 0);
    }

    @Test
    void calculateOwedAmountPerPersonTestNullExpense() {
        assertEquals(event1.calculateOwedAmountPerPerson(person1), 0);
    }
}