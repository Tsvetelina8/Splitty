package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@JsonPropertyOrder({ "ID", "Name", "Description",
    "Date", "Payer", "Loans", "Total amount"})
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("ID")
    public long id = 0;
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Description")
    public String description;
    @JsonProperty("Date")
    public LocalDateTime date;
    @ManyToOne
    @JsonProperty("Payer")
    public Person payer;
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty("Loans")
    public List<Loan> loans;
    @JsonProperty("Total Amount")
    public double totalAmount;

    /**
     * Default constructor, for object mapper
     */
    @SuppressWarnings("unused")
    public Expense() {}

    /**
     * Constructs an Expense object with given parameters
     *
     * @param name          name of the expense
     * @param description   description of the expense
     * @param date          date of the expense
     * @param payer    money giver of the expense
     * @param totalAmount   expense amount
     */
    public Expense(String name,
                   String description,
                   LocalDateTime date,
                   Person payer,
                   double totalAmount) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.payer = payer;
        this.totalAmount = totalAmount;
        loans = new ArrayList<>();
    }

    /**
     *Calculates the total amount owed by each person in the event.
     *Each person's owed amount is the sum of all loans they have taken.
     * @return A map where each person is mapped to the amount they own to the payer.
     */
    public Map<Person, Double> calculateOwedAmounts() {
        Map<Person, Double> owedAmounts = new HashMap<>();
        for(Loan l: loans) {
            owedAmounts.put(l.borrower, l.amount);
        }
        owedAmounts.merge(payer, -totalAmount, Double::sum);
        return owedAmounts;
    }

    /**
     * Method to test whether two objects are equal
     *
     * @param obj   Object to compare 'this' to
     * @return true/false if equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Expense)) return false;
        return id == ((Expense) obj).id;
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
