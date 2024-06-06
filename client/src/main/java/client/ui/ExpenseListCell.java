/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package client.ui;

import client.scenes.EditEventCtrl;
import com.google.inject.Inject;

import client.scenes.MainCtrl;
import commons.Event;
import commons.Expense;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


/**
 * Custom ListCell for adding Expense to the ListView during Event Edit
 * (allows for better formatting + button to edit to the right)
 */
public class ExpenseListCell extends ListCell<Expense> {

    private String eventName;
    @FXML
    private HBox hbox;
    @FXML
    private Text dateText;
    @FXML
    private Text detailsText;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    private Event event;

    private final MainCtrl mainCtrl;

    private EditEventCtrl editEventCtrl;

    private ResourceBundle resourceBundle;

    /**
     * Constructor for ExpenseListCell. Sets the necessary visual constraints: 
     * alignment, spacings, etc.
     * Also binds "Edit" button action to editExpense method.
     *
     * @param mainCtrl main controller (injection to allow changing
     *                 scenes if user presses "Edit" button)
     * @param eventName name of event
     * @param editEventCtrl edit event (injection to allow deleting expenses
     * @param bundle resource bundle that is used for localization
     */
    @Inject
    public ExpenseListCell(MainCtrl mainCtrl,
                           String eventName,
                           EditEventCtrl editEventCtrl,
                           ResourceBundle bundle) {
        super();
        resourceBundle = bundle;

        this.mainCtrl = mainCtrl;
        this.eventName = eventName;
        this.editEventCtrl = editEventCtrl;

        hbox = new HBox(10);
        dateText = new Text();
        detailsText = new Text();
        editButton = new Button(resourceBundle.getString("common-edit"));
        deleteButton = new Button(resourceBundle.getString("common-delete"));

        hbox.getChildren().addAll(dateText, detailsText, editButton, deleteButton);
        HBox.setHgrow(detailsText, Priority.ALWAYS);

        dateText.setTextAlignment(TextAlignment.LEFT);
        detailsText.setTextAlignment(TextAlignment.CENTER);
        editButton.setTextAlignment(TextAlignment.RIGHT);

        editButton.setOnAction(event -> {
            Expense expense = getItem();
            if (expense != null) {
                editEventCtrl.editExpense(expense);
            }
        });

        deleteButton.setOnAction(event -> {
            Expense expense = getItem();
            if (expense != null) {
                deleteExpense(expense);
            }
        });
    }

    @Override
    protected void updateItem(Expense expense, boolean empty) {
        super.updateItem(expense, empty);
        setText(null);
        if (empty) {
            setGraphic(null);
        } else {
            dateText.setText(expense.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            detailsText.setText(String.format(resourceBundle.getString("edit-event-expense-text"),
                    expense.payer.firstName, expense.totalAmount, expense.name));
            setGraphic(hbox);
        }
    }

    /**
     * Bind this expense cell to an event it belongs to.
     * Used to correctly switch to the required scene on button presses.
     *
     * @param event event the expense belongs to
     * @return itself
     */
    public ExpenseListCell attachEvent(Event event) {
        this.event = event;
        return this;
    }

    /**
     * Method called when user presses "Delete" button next to expense.
     * @param expense expense to edit
     */
    public void deleteExpense(Expense expense) {
        editEventCtrl.deleteExpense(expense);
    }

    /**
     * Gets the edit button, for testing purposes
     * @return the edit button
     */
    public Button getEditButton() {
        return editButton;
    }

    /**
     * Gets the delete button, for testing purposes
     * @return the delete button
     */
    public Button getDeleteButton() {
        return deleteButton;
    }
}