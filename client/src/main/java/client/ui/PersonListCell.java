package client.ui;

import client.scenes.EditEventCtrl;
import commons.Person;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

import java.util.ResourceBundle;

public class PersonListCell extends ListCell<Person> {
    private EditEventCtrl editEventCtrl;

    private ResourceBundle resourceBundle;

    /**
     * Constructor to set the event edit screen
     * @param editEventCtrl The event edit screen to delete the participants from
     * @param bundle The resource bundle used for label translations
     */
    public PersonListCell(EditEventCtrl editEventCtrl, ResourceBundle bundle) {
        this.editEventCtrl = editEventCtrl;
        this.resourceBundle = bundle;
    }

    /**
     * Updates the items in the list with the persons and the button
     * @param item The person
     * @param empty The button
     */
    @Override
    protected void updateItem(Person item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            HBox hBox = new HBox(10);
            Button editButton = new Button(resourceBundle.getString("common-edit"));
            Button deleteButton = new Button(resourceBundle.getString("common-delete"));

            hBox.getChildren().addAll(editButton, deleteButton);

            deleteButton.setOnAction(event -> editEventCtrl.deleteParticipants(item));
            editButton.setOnAction(event -> editEventCtrl.editParticipant(item));
            setText(item.firstName + " " + item.lastName);
            setGraphic(hBox);
        }
    }
}