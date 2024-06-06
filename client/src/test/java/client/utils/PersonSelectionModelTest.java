package client.utils;

import commons.Person;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonSelectionModelTest {

    private PersonSelectionModel personSelectionModel;
    private Person person;
    @BeforeEach
    void setUp() {
        person = new Person("a", "b");
        personSelectionModel = new PersonSelectionModel(person, false);
    }

    @Test
    void getPersonTest() {
        assertEquals(person, personSelectionModel.getPerson());
    }

    @Test
    void isSelectedTest() {
        assertFalse(personSelectionModel.isSelected());
    }

    @Test
    void setSelectedTest() {
        personSelectionModel.setSelected(true);
        assertTrue(personSelectionModel.isSelected());
    }

    @Test
    void selectedPropertyTest() {
        BooleanProperty a = new SimpleBooleanProperty(false);
        assertEquals(a.get(), personSelectionModel.selectedProperty().get());
    }
}