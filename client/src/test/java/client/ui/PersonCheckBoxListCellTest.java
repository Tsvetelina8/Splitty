package client.ui;

import client.utils.PersonSelectionModel;
import commons.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class PersonCheckBoxListCellTest {

    @Test
    void updateItemTest() {
        PersonCheckBoxListCell personCheckBoxListCell = new PersonCheckBoxListCell();
        personCheckBoxListCell.updateItem(new PersonSelectionModel(new Person("a", "a"), true), false);
        assertNotNull(personCheckBoxListCell.getGraphic());
        assertEquals(personCheckBoxListCell.getText(),"a a");
    }

    @Test
    void updateItemNull() {
        PersonCheckBoxListCell personCheckBoxListCell = new PersonCheckBoxListCell();
        personCheckBoxListCell.updateItem(null, false);
        assertNull(personCheckBoxListCell.getGraphic());
        assertNull(personCheckBoxListCell.getText());
    }

    @Test
    void updateItemEmpty() {
        PersonCheckBoxListCell personCheckBoxListCell = new PersonCheckBoxListCell();
        personCheckBoxListCell.updateItem(new PersonSelectionModel(new Person("a", "a"), true), true);
        assertNull(personCheckBoxListCell.getGraphic());
        assertNull(personCheckBoxListCell.getText());
    }
}