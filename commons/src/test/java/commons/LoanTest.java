package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoanTest {

    Loan loanNullFields;
    Loan loanEqualObj1;
    Loan loanEqualObj2;
    Loan loanNotEqualAny;

    @BeforeEach
    public void setup() {
        loanNullFields = new Loan(0, null, null);
        loanEqualObj1 = new Loan(42, new Person("John", "Wick"), new Person("Wick", "John"));
        loanEqualObj2 = new Loan(42, new Person("John", "Wick"), new Person("Wick", "John"));
        loanNotEqualAny = new Loan(42, new Person("Wick", "John"), new Person("John", "Wick"));
        loanEqualObj1.id = 1;
        loanEqualObj2.id = 1;
        loanNotEqualAny.id = 2;
    }

    @Test
    void testConstructor() {
        assertNotNull(loanNullFields);
        assertNotNull(loanEqualObj1);
    }
    @Test
    void testEqualsIsSymmetric() {
        assertTrue(loanEqualObj1.equals(loanEqualObj2) && loanEqualObj2.equals(loanEqualObj1));
    }
    @Test
    void testEqualsShouldBeTrue() {
        assertEquals(loanEqualObj1, loanEqualObj2);
    }
    @Test
    void testEqualsShouldBeFalse() {
        loanEqualObj1.id = 1;
        assertNotEquals(loanNullFields, loanEqualObj1);
    }

    @Test
    void testHashCodeIsSymmetric() {
        assertEquals(loanEqualObj1, loanEqualObj2);
        assertEquals(loanEqualObj1.hashCode(), loanEqualObj2.hashCode());
    }
    @Test
    void testHashCodeShouldNotEqual() {
        assertNotEquals(loanEqualObj1.hashCode(), loanNotEqualAny.hashCode());
    }

    @Test
    void testToStringIsNotNull() {
        String actual = loanNotEqualAny.toString();
        assertNotNull(actual);
        assertTrue(actual.contains("amount"));
        assertTrue(actual.contains("payer"));
        assertTrue(actual.contains("borrower"));
    }

    @Test
    void testToStringShouldBeFalse() {
        assertNotEquals(loanEqualObj1.toString(), loanNotEqualAny.toString());
    }
}