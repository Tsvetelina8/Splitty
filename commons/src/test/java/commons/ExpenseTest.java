package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {

    Person p;
    Person q;
    Person r;
    Loan l;
    Loan l1;
    private static final Integer amount = 200;
    Expense expenseObjEqual1;
    Expense expenseObjEqual2;
    Expense expenseObjNotEqual;
    Expense expenseObj;
    @BeforeEach
    public void setup() {
        p = new Person("a", "b");
        q = new Person("c", "d");
        r = new Person("e", "f");
        l = new Loan(10, p, q);
        l1 = new Loan(190, p, r);
        expenseObjEqual1 = new Expense("test", "test",
                LocalDateTime.of(1, 1, 1, 1, 1), p, amount);
        expenseObjEqual2 = new Expense("test", "test",
                LocalDateTime.of(1, 1, 1, 1, 1), p, amount);
        expenseObjNotEqual = new Expense("test1", "test",
                LocalDateTime.of(1, 1, 1, 1, 1), p, amount);
        expenseObj = new Expense("test", "test",
                LocalDateTime.of(1, 1, 1, 1, 1), p, amount);
        expenseObj.loans.add(l);
        expenseObj.loans.add(l1);

        p.id = 0;
        q.id = 1;
        r.id = 2;
        l.id = 0;
        l1.id = 1;
        expenseObjNotEqual.id = 1;
        expenseObj.id = 2;
    }

    @Test
    void testConstructor() {
        assertNotNull(expenseObjEqual1);
        assertEquals(amount, expenseObjEqual1.totalAmount, 0.0);
    }
    @Test
    public void testOwedAmountsWithOwning() {
        Map<Person, Double> mapForP = new HashMap<>();
        mapForP.put(q, 10.0);
        mapForP.put(r, 190.0);
        mapForP.put(p, -200.0);
        assertEquals(mapForP, expenseObj.calculateOwedAmounts());

    }
    @Test
    public void testOwedAmountsNoOwning() {
        Map<Person, Double> mapForP = new HashMap<>();
        mapForP.put(p,10.0);
        mapForP.put(p, 190.0);
        assertNotEquals(mapForP, expenseObjEqual1.calculateOwedAmounts());

    }
    @Test
    public void testHashCodeIsSymmetric() {
        assertEquals(expenseObjEqual1, expenseObjEqual2);
        assertEquals(expenseObjEqual1.hashCode(), expenseObjEqual2.hashCode());
    }

    @Test
    public void testHashCodeShouldNotEqual() {
        assertNotEquals(expenseObjEqual1.hashCode(), expenseObjNotEqual.hashCode());
    }

    @Test
    void testEqualsIsSymmetric() {
        assertTrue(expenseObjEqual1.equals(expenseObjEqual2)
                && expenseObjEqual2.equals(expenseObjEqual1));
    }
    @Test
    void testEqualsShouldBeTrue() {
        assertEquals(expenseObjEqual1, expenseObjEqual2);
    }

    @Test
    void testEqualsShouldBeFalse() {
        expenseObjNotEqual.id = 1;
        assertNotEquals(expenseObjEqual1, expenseObjNotEqual);
    }

    @Test
    void testHashCodeShouldBeTrue(){
        assertEquals(expenseObjEqual1.hashCode(), expenseObjEqual2.hashCode());
    }

    @Test
    void testHashCodeShouldBeFalse(){
        assertNotEquals(expenseObjEqual1.hashCode(), expenseObjNotEqual.hashCode());
    }
    @Test
    void testToString() {
        String actual = expenseObjEqual1.toString();
        assertNotNull(actual);
        assertTrue(actual.contains("name"));
        assertTrue(actual.contains("description"));
        assertTrue(actual.contains("date"));
        assertTrue(actual.contains("payer"));
        assertTrue(actual.contains("loans"));
        assertTrue(actual.contains("totalAmount"));
        assertTrue(actual.contains(amount.toString()));
    }
}