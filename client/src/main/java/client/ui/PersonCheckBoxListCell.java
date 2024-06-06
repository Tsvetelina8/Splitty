package client.ui;

import client.utils.PersonSelectionModel;
import commons.Person;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;


/**
 * Custom ListCell for adding Person to the ListView for expense distribution
 * (with a checkbox next to it)
 */
public class PersonCheckBoxListCell extends ListCell<PersonSelectionModel> {
    @Override
    protected void updateItem(PersonSelectionModel item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            CheckBox checkBox = new CheckBox();
            checkBox.selectedProperty().bindBidirectional(item.selectedProperty());
            Person person = item.getPerson();
            setText(person.firstName + " " + person.lastName);
            setGraphic(checkBox);
        }
    }
}