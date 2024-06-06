package commons;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
@JsonPropertyOrder({ "ID", "Amount", "Payer", "Borrower"})
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("ID")
    public long id = 0;

    @JsonProperty("Amount")
    public double amount;
    @ManyToOne
    @JsonProperty("Payer")
    public Person payer; //the one that provided the money
    @ManyToOne
    @JsonProperty("Borrower")
    public Person borrower; //the one that borrowed the money

    /**
     * Constructs a Loan object with given the amount and payer/borrower
     *
     * @param amount   amount which has been loaned
     * @param payer   person who lent the money
     * @param borrower   person who was lent the money
     */
    public Loan(double amount, Person payer, Person borrower) {
        this.amount = amount;
        this.borrower = borrower;
        this.payer = payer;
    }

    /**
     * Default constructor, for object mapper
     */
    @SuppressWarnings("unused")
    public Loan() {}

    /**
     * Method to test whether two objects are equal
     *
     * @param obj   Object to compare 'this' to
     * @return true/false if equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Loan)) return false;
        return id == ((Loan) obj).id;
    }

    /**
     * Method to get the hashcode of the object
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return (int)id;
    }

    /**
     * Method to get the string representation of the object
     *
     * @return string representation of the object
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

}
