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

package client.scenes;

import client.ui.ExpenseListCell;
import client.ui.PersonListCell;
import client.utils.*;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Person;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;
import java.util.*;

public class EditEventCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private Label eventName;

    @FXML
    private ListView<Person> participants;

    @FXML
    private ListView<Expense> eventExpensesList;

    @FXML
    private ListView<Expense> eventExpensesListIncluding;

    @FXML
    private ListView<Expense> eventExpensesListFrom;

    @FXML
    private Tab eventExpensesFromX;

    @FXML
    private Tab eventExpensesIncludingX;

    @FXML
    private Tab eventExpensesAll;

    @FXML
    private Label inviteCodeLabel;

    @FXML
    private Menu languageIndicator;

    @FXML
    private Button eventAddExpense;

    @FXML
    private Button eventSettleDebts;

    @FXML
    private Button eventSendInvites;

    @FXML
    private Button addNewParticipant;

    @FXML
    private Button editTitle;

    @FXML
    private Button backButton;

    @FXML
    private Tooltip copyTip;

    @FXML
    private Label totalExpenses;

    @FXML
    private Label paidLabel;

    @FXML
    private Label owesLabel;

    private Event event;

    private Person selectedPerson;

    private ResourceBundle resourceBundle;

    private UIAlertService uiAlertService;

    private AppConfig appConfig;
    private EmailUtils emailUtils;

    /**
     * Constructor for EditEvent controller
     *
     * @param server   server
     * @param mainCtrl main controller
     * @param utils    email utilities
     */
    @Inject
    public EditEventCtrl(ServerUtils server, MainCtrl mainCtrl, EmailUtils utils) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.emailUtils = utils;
    }

    /**
     * This constructor is for testing
     * @param params of parameters
     */
    public EditEventCtrl(List<Object> params) {
        this.server = (ServerUtils) params.get(0);
        this.mainCtrl = (MainCtrl) params.get(1);
        this.event = (Event) params.get(2);
        this.resourceBundle = (ResourceBundle) params.get(3);
        this.participants = (ListView<Person>) params.get(4);
        this.inviteCodeLabel = (Label) params.get(5);
        this.eventName = (Label) params.get(6);
        this.eventExpensesList = (ListView<Expense>) params.get(7);
        this.eventExpensesListIncluding = (ListView<Expense>) params.get(8);
        this.eventExpensesListFrom = (ListView<Expense>) params.get(9);
        this.addNewParticipant = (Button) params.get(11);
        this.eventAddExpense = (Button) params.get(12);
        this.eventSettleDebts = (Button) params.get(13);
        this.eventSendInvites = (Button) params.get(14);
        this.editTitle = (Button) params.get(15);
        this.backButton = (Button) params.get(16);
        this.eventExpensesFromX = (Tab) params.get(17);
        this.eventExpensesIncludingX = (Tab) params.get(18);
        this.copyTip = (Tooltip) params.get(19);
        this.totalExpenses = (Label) params.get(20);
        this.paidLabel = (Label) params.get(21);
        this.owesLabel = (Label) params.get(22);
        this.uiAlertService = (UIAlertService) params.get(23);
        this.appConfig = (AppConfig) params.get(24);
        this.languageIndicator = (Menu) params.get(25);
        this.emailUtils = (EmailUtils) params.get(26);
        selectedPerson = null;
    }

    /**
     * Getter for event
     * @return event
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Getter for event name
     * @return eventName
     */
    public Label getEventName() {
        return eventName;
    }

    /**
     * Getter for participants
     * @return participants
     */
    public ListView<Person> getParticipants() {
        return participants;
    }

    /**
     * Getter for selectedPerson
     * @return the selected person
     */
    public Person getSelectedPerson() {
        return selectedPerson;
    }

    /**
     * Method to initialize the elements of the Scene
     */
    @FXML
    public void initialize() {
        appConfig = mainCtrl.getConfig();
        try {
            resourceBundle = ResourceBundle.getBundle(
                    "client.localization.Labels", mainCtrl.getSelectedLocale());
        } catch (Exception e) {
            e.printStackTrace();
        }

        participants.getSelectionModel().selectedItemProperty().
                addListener((observable, oldValue, newValue) -> {
                    if(newValue != null) {
                        selectedPerson = newValue;
                        chooseParticipant(newValue.toString());
                        showExpensesIncludingX();
                        showExpensesFromX();
                    }
                });

        fillForm();
        fillLanguageSwitch();
        initIcons();
        LanguageSwitchUtil.initIndicator(languageIndicator, mainCtrl.getSelectedLocale());
        deleteButtonInit();
        checkIfEmailValid();
        copyTip.setShowDelay(Duration.millis(10));
        copyTip.setHideDelay(Duration.millis(10));
        uiAlertService = new UIAlertService();
        try {
            eventName.setText(event.getTitle());
        } catch (NullPointerException ignored) {}

        server.registerForUpdatesEvent(q -> {
            Platform.runLater(() -> {
                eventName.setText(q.getTitle());
                event = q;
            });
        });
    }

    private void checkIfEmailValid() {
        if (emailUtils.isEmailSetUp()) return;
        eventSendInvites.setDisable(true);
    }

    /**
     * change title scene
     */
    @FXML
    private void changeTitle() {
        mainCtrl.showEditTitle(event.id, event);
    }

    /**
     * Adds the delete button to the participant list
     */
    public void deleteButtonInit() {
        participants.setCellFactory(cellFactory -> new PersonListCell(this, resourceBundle));
    }

    /**
     * Set icons to suitable buttons
     */
    private void initIcons() {
        setButtonIcon(addNewParticipant, "/client/icons/icons8-add-48.png");
        setButtonIcon(eventAddExpense, "/client/icons/icons8-add-48.png");
        setButtonIcon(eventSettleDebts, "/client/icons/icons8-tick-48.png");
        setButtonIcon(eventSendInvites, "/client/icons/icons8-send-48.png");
        setButtonIcon(editTitle, "/client/icons/icons8-edit-48.png");
        setButtonIcon(backButton, "/client/icons/icons8-back-48.png");
    }

    /**
     * Set icon to button
     * 
     * @param button   button
     * @param iconPath path to icon
     * @param width    width of icon on display (px)
     * @param height   height of icon on display (px)
     */
    private void setButtonIcon(Button button, String iconPath, double width, double height) {
        Image icon = new Image(getClass().getResourceAsStream(iconPath));
        ImageView iconView = new ImageView(icon);

        iconView.setFitHeight(height);
        iconView.setFitWidth(width);

        button.setGraphic(iconView);
    }

    /**
     * Set icon to button (overload to not copy-paste width and height)
     * 
     * @param button   button
     * @param iconPath path to icon
     */
    private void setButtonIcon(Button button, String iconPath) { 
        setButtonIcon(button, iconPath, 16, 16); 
    }

    /**
     * Set event
     * @param event Event entity
     */
    public void setEvent(Event event) {
        if (this.event == null) setInviteCodeLabel("{0}", event.getInviteCode());
        else setInviteCodeLabel(this.event.getInviteCode(), event.getInviteCode());

        eventName.setText(event.getTitle());
        this.event = event;
        refresh();
        chooseParticipant("x");
    }

    private void setInviteCodeLabel(String oldCode, String newCode) {
        String label = inviteCodeLabel.getText();
        label = label.replace(oldCode, "{0}");
        label = label.replace("{0}", newCode);
        inviteCodeLabel.setText(label);
        inviteCodeLabel.setOnMouseEntered(e ->
                inviteCodeLabel.setStyle("-fx-text-fill: blue; -fx-font-weight: bold"));
        inviteCodeLabel.setOnMouseExited(e ->{
            inviteCodeLabel.setStyle("-fx-text-fill: black; -fx-font-weight: normal");
            copyTip.setText(resourceBundle.getString("edit-event-copytip"));
        });
        inviteCodeLabel.setOnMouseClicked(e ->{
            copyTip.setText(resourceBundle.getString("edit-event-copytip-copied"));
            copyCodeToClipboard();
        });
    }

    /**
     * Method to populate form elements with options
     */
    private void fillForm() {
        eventExpensesList.setCellFactory(eventExpenses ->
                new ExpenseListCell(mainCtrl, eventName.getText(), this, resourceBundle)
                        .attachEvent(event));
        eventExpensesListIncluding.setCellFactory(eventExpenses ->
                new ExpenseListCell(mainCtrl, eventName.getText(), this, resourceBundle)
                        .attachEvent(event));
        eventExpensesListFrom.setCellFactory(eventExpenses ->
                new ExpenseListCell(mainCtrl, eventName.getText(), this, resourceBundle)
                        .attachEvent(event));
    }

    /**
     * Method to populate the language switch
     */
    public void fillLanguageSwitch() {
        try {
            languageIndicator.getItems().clear();
            for (Locale locale : mainCtrl.getLocales()) {
                String localeDisplayString = locale.getDisplayLanguage() +
                    " | " + locale;

                MenuItem menuItem = new MenuItem(localeDisplayString);
                menuItem.setOnAction(e -> switchLanguage(locale));
                languageIndicator.getItems().add(menuItem);
            }
        } catch (NullPointerException e) {
            // In tests all these buttons are null, so I made this try for now.
        }
    }

    private void switchLanguage(Locale locale) {
        String selected = selectedPerson != null ? selectedPerson.toString() : "x";
        List<Expense> allList = eventExpensesList.getItems();
        List<Expense> includingList = eventExpensesListIncluding.getItems();
        List<Expense> fromList = eventExpensesListFrom.getItems();
        List<Person> participantList = participants.getItems();

        mainCtrl.switchLanguage(locale);
        LanguageSwitchUtil.initIndicator(languageIndicator, mainCtrl.getSelectedLocale());

        try {
            resourceBundle = ResourceBundle.getBundle(
                    "client.localization.Labels", mainCtrl.getSelectedLocale());
        } catch (Exception e) {
            e.printStackTrace();
        }

        eventExpensesList.getItems().setAll(allList);
        eventExpensesListIncluding.getItems().setAll(includingList);
        eventExpensesListFrom.getItems().setAll(fromList);
        participants.getItems().setAll(participantList);
        setInviteCodeLabel("{0}", event.getInviteCode());
        chooseParticipant(selected);

        totalExpenses.setText(resourceBundle.
                getString("edit-event-total-expenses") + event.calculateTotalExpenses());
    }
    
    /**
     * Method called when MenuItem "Add Translation" gets clicked on
     */
    public void contributeLanguage() {
        mainCtrl.showAddLanguage();
    }

    /**
     * Method called each time another participant is chosen from the ChoiceBox.
     * Sets the X for all toggle buttons
     *
     * @param chosenParticipantName name of the newly chosen participant
     */
    private void chooseParticipant(String chosenParticipantName) {
        chosenParticipantName = chosenParticipantName.length() > 20
                ? chosenParticipantName.substring(0, 20) + "..." : chosenParticipantName;
        eventExpensesFromX.setText(
                resourceBundle.getString("edit-event-from-x") + chosenParticipantName);
        eventExpensesIncludingX.setText(
                resourceBundle.getString("edit-event-including-x") + chosenParticipantName);
        paidLabel.setText(String.format(resourceBundle.getString("edit-event-paid-for"),
                chosenParticipantName, event.calculatePaidAmounts(selectedPerson)));
        owesLabel.setText(String.format(resourceBundle.getString("edit-event-owes"),
                chosenParticipantName, event.calculateOwedAmountPerPerson(selectedPerson)));
    }

    /**
     * Method called when clicking the invite code label
     * Copies the invite code into the user's machine's clipboard
     */
    public void copyCodeToClipboard() {
        StringSelection selection = new StringSelection(event.getInviteCode());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

    /**
     * Method called when "Send Invites" button is pressed
     */
    public void sendInvites() {
        mainCtrl.showSendInvitation(event);
    }

    /**
     * Method called when "Add New" (participant) button is pressed
     */
    public void addNewParticipant() {
        mainCtrl.showAddPerson(event, null);
    }

    /**
     * Method called when the edit participant button is called
     * @param person The person to edit
     */
    public void editParticipant(Person person) {
        mainCtrl.showAddPerson(event, person);
    }

    /**
     * Method called when user presses "Edit" button next to expense.
     * @param expense expense to edit
     */
    public void editExpense(Expense expense) {
        mainCtrl.showAddExpense(event, expense);
    }

    /**
     * Method called when "Delete" button is pressed on person
     * @param person Person to delete
     */
    public void deleteParticipants(Person person) {
        if (person != null) {
            List<Person> persons = new ArrayList<>();
            for (Expense e : event.getExpenses()) {
                persons.add(e.payer);
                persons.addAll(e.loans.stream().map(loan -> loan.borrower).toList());
            }
            if (persons.contains(person)) {
                try {
                    ResourceBundle r = ResourceBundle.getBundle(
                        "client.localization.Labels", mainCtrl.getSelectedLocale());

                    uiAlertService.showAlert(Alert.AlertType.ERROR,
                        r.getString("edit-event-alert-title"), 
                        r.getString("edit-event-alert-header"),
                        r.getString("edit-event-alert-desc"));
                } catch (NullPointerException e) {
                    uiAlertService.showAlert(Alert.AlertType.ERROR,
                        "Cannot delete person", "Cannot delete person",
                        "Person is involved in an expense!" +
                            "\nSettle the debts before deleting the person.\n\n");
                }
            }
            else {
                server.deletePersonById(event, person.id);
            }
        }
        refresh();
    }

    /**
     * Method called when "Delete" button is pressed on expense
     * @param expense expense to delete
     */
    public void deleteExpense(Expense expense) {
        if (expense != null) {
            server.deleteExpense(event, expense);
        }
        refresh();
    }

    /**
     * Method called when "Add Expense" button is pressed
     */
    public void addExpense() {
        mainCtrl.showAddExpense(event, null);
    }

    /**
     * Method called when "Including X" button is pressed
     */
    public void showExpensesIncludingX() {
        eventExpensesListIncluding.getItems().clear();
        ObservableList<Expense> expenses = eventExpensesList.getItems();
        eventExpensesListIncluding.getItems().addAll(expenses);
        for(Expense e : expenses) {
            List<Person> persons = new ArrayList<>();
            persons.add(e.payer);
            persons.addAll(e.loans.stream().map(loan -> loan.borrower).toList());
            if(!persons.contains(selectedPerson)) eventExpensesListIncluding.getItems().remove(e);
        }
    }

    /**
     * Method called when "from X" button is pressed
     */
    public void showExpensesFromX() {
        eventExpensesListFrom.getItems().clear();
        ObservableList<Expense> expenses = eventExpensesList.getItems();
        for(Expense e : expenses) {
            if (e.payer.equals(selectedPerson)) eventExpensesListFrom.getItems().add(e);
        }
    }

    /**
     * Method called when "Settle Debts" button is pressed
     */
    public void settleDebts() {
        mainCtrl.showSettleDebts(event);
    }

    /**
     * Method called when ESC key or back button is pressed
     */
    public void showStartScreen() {
        mainCtrl.showStartScreen();
    }

    /**
     * Method called to refresh the event, loading participants and expenses
     */
    public void refresh() {
        try {
            resourceBundle = ResourceBundle.getBundle(
                    "client.localization.Labels", mainCtrl.getSelectedLocale());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            event = server.findEventById(event.id);
        }
        catch (Exception e) {
            if(mainCtrl.getPrimaryStage().getTitle().equals("Edit Event")) eventDeleteAlert();
            return;
        }
        List<Person> participants = event.getParticipants();
        this.participants.setItems(FXCollections.observableArrayList(participants));
        totalExpenses.setText(resourceBundle.
                getString("edit-event-total-expenses") + event.calculateTotalExpenses());

        ObservableList<Expense> expenses = FXCollections.observableArrayList(event.getExpenses());
        eventExpensesList.setItems(expenses);
        showExpensesIncludingX();
        showExpensesFromX();
    }


    /**
     * Shows the alert when the event is deleted, and redirects to start screen.
     */
    public void eventDeleteAlert() {
        uiAlertService.showAlert(Alert.AlertType.INFORMATION,
                resourceBundle.getString("edit-event-delete-alert-title"),
                resourceBundle.getString("edit-event-delete-alert-title"),
                resourceBundle.getString("edit-event-delete-alert-desc"));
        mainCtrl.showStartScreen();
    }

    /**
     * Sets the items of eventExpensesParticipant, for testing
     * @param person The person to add
     */
    public void addExpenseParticipant(Person person) {
        participants.getItems().add(person);
    }

    /**
     * method for about page
     */
    public void aboutPage() {
        mainCtrl.showAbout();
    }


    /**
     * Method to handle the keyboard key pressed event
     *
     * @param e event
     */
    public void keyPressed(KeyEvent e) {
        if (Objects.requireNonNull(e.getCode()) == KeyCode.ESCAPE) {
            showStartScreen();
        }
    }

    /**
     * To string method for debugging
     * @return string representation of some of the variables in this screen
     */
    public String toString() {
        String string = "Event: <" + event.toString() + ">";
        string += "Event Participants: " +  event.getParticipants().toString() + "\n";
        return string;
    }
}
