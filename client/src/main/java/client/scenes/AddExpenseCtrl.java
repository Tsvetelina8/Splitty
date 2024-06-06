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

import client.ui.PersonCheckBoxListCell;
import client.utils.AppConfig;
import client.utils.LanguageSwitchUtil;
import client.utils.PersonSelectionModel;
import client.utils.ServerUtils;
import client.utils.UIAlertService;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Loan;
import commons.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class AddExpenseCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private Event event;

    @FXML
    private ChoiceBox<Person> payer;

    @FXML
    private DatePicker expenseDate;

    @FXML
    private TextField expenseName;

    @FXML
    private Spinner<Double> expenseValue;

    @FXML
    private ChoiceBox<String> expenseCurrency;

    @FXML
    private RadioButton equallyRadio;

    @FXML
    private RadioButton selectPeopleRadio;

    @FXML
    private ListView<PersonSelectionModel> selectPeopleListView;

    @FXML
    private TextField expenseType;

    @FXML
    private Menu languageIndicator;

    @FXML
    private Button expenseAdd;

    @FXML
    private Button expenseAbort;

    @FXML
    private Label title;

    private Boolean isPut = false;

    private UIAlertService alertService;

    private ResourceBundle resourceBundle;

    private Expense putExpense;

    private AppConfig appConfig;

    /**
     * Constructor for injection
     * @param server current server
     * @param mainCtrl main controller
     */
    @Inject
    public AddExpenseCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Constructor used for testing
     * @param params javafx objects array
     */
    public AddExpenseCtrl(Object[] params) {
        this.server = (ServerUtils) params[0];
        this.mainCtrl = (MainCtrl) params[1];
        this.payer = (ChoiceBox<Person>) params[2];
        this.expenseDate = (DatePicker) params[3];
        this.expenseName = (TextField) params[4];
        this.expenseValue = (Spinner<Double>) params[5];
        this.expenseCurrency = (ChoiceBox<String>) params[6];
        this.equallyRadio = (RadioButton) params[7];
        this.selectPeopleRadio = (RadioButton) params[8];
        this.expenseType = (TextField) params[9];
        this.expenseAdd = (Button) params[11];
        this.expenseAbort = (Button) params[12];
        this.selectPeopleListView = (ListView<PersonSelectionModel>) params[13];
        this.title = (Label) params[14];
        this.alertService = (UIAlertService) params[15];
        this.resourceBundle = (ResourceBundle) params[16];
        this.appConfig = (AppConfig) params[17];
        this.languageIndicator = (Menu) params[18];
    }


    /**
     * Method to initialize the elements of the Scene
     */
    @FXML
    public void initialize() {
        appConfig = mainCtrl.getConfig();
        resourceBundle = ResourceBundle.getBundle(
                "client.localization.Labels", Locale.of(appConfig.getSelectedLocale()));

        ToggleGroup group = new ToggleGroup();
        equallyRadio.setToggleGroup(group);
        selectPeopleRadio.setToggleGroup(group);
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) ->
                selectPeopleListView.setDisable(group.getSelectedToggle() == equallyRadio));

        fillForm();
        setDefaults();
        initIcons();
        fillLanguageSwitch();
        try {
            LanguageSwitchUtil.initIndicator(languageIndicator, mainCtrl.getSelectedLocale());
        } catch (NullPointerException e) {
            // In testslanguageIndicator is null
        };
        alertService = new UIAlertService();
    }

    /**
     * Set icons to suitable buttons
     */
    private void initIcons() {
        setButtonIcon(expenseAdd, "/client/icons/icons8-add-48.png");
        setButtonIcon(expenseAbort, "/client/icons/icons8-cancel-48.png");
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
     * Set icon to button (overload to not copypaste width and height)
     * 
     * @param button   button
     * @param iconPath path to icon
     */
    private void setButtonIcon(Button button, String iconPath) { 
        setButtonIcon(button, iconPath, 16, 16); 
    }

    /**
     * Method to populate form elements with options
     */
    private void fillForm() {
        refresh();
        expenseCurrency.getItems().addAll("EUR", "USD");

        selectPeopleListView.setCellFactory(selectPeopleListView -> new PersonCheckBoxListCell());
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
        int payerIndex = payer.getSelectionModel().getSelectedIndex();
        String nameString = expenseName.getText();
        boolean equally = equallyRadio.isSelected();
        boolean few = selectPeopleRadio.isSelected();
        int currencyIndex = expenseCurrency.getSelectionModel().getSelectedIndex();
        LocalDate date = expenseDate.getValue();
        List<PersonSelectionModel> persons = selectPeopleListView.getItems();
        String typeString = expenseType.getText();

        mainCtrl.switchLanguage(locale);
        LanguageSwitchUtil.initIndicator(languageIndicator, mainCtrl.getSelectedLocale());

        payer.getSelectionModel().select(payerIndex);
        expenseName.setText(nameString);
        equallyRadio.setSelected(equally);
        selectPeopleRadio.setSelected(few);
        expenseCurrency.getSelectionModel().select(currencyIndex);
        expenseDate.setValue(date);
        selectPeopleListView.getItems().setAll(persons);
        expenseType.setText(typeString);
    }

    /**
     * Method called when MenuItem "Add Translation" gets clicked on
     */
    public void contributeLanguage() {
        mainCtrl.showAddLanguage();
    }

    /**
     * method for about page
     */
    public void aboutPage() {
        mainCtrl.showAbout();
    }

    /**
     * Method to set defaults in the form
     */
    private void setDefaults() {
        expenseName.clear();
        expenseDate.setValue(LocalDate.now());
        
        SpinnerValueFactory<Double> expenseValueFactory = 
            new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000, 0, 1);
        expenseValue.setValueFactory(expenseValueFactory);

        expenseCurrency.getSelectionModel().selectFirst();
    }

    /**
     * Checks all the values for empty or missing inputs
     * @return If everything in filled in
     */
    public boolean checkValues() {
        if (payer.getValue() == null) {
            alertService.showAlert(Alert.AlertType.WARNING,
                    resourceBundle.getString("create-expense-alert1-title"),
                    resourceBundle.getString("create-expense-alert1-title"),
                    resourceBundle.getString("create-expense-alert1-content"));
            return false;
        }
        if (expenseName.getText().isBlank()) {
            alertService.showAlert(Alert.AlertType.WARNING,
                    resourceBundle.getString("create-expense-alert1-title"),
                    resourceBundle.getString("create-expense-alert1-title"),
                    resourceBundle.getString("create-expense-alert2-content"));
            return false;
        }
        if (expenseValue.getValue() == null ||  expenseValue.getValue().isNaN()
                || expenseValue.getValue() == 0) {
            alertService.showAlert(Alert.AlertType.WARNING,
                    resourceBundle.getString("create-expense-alert1-title"),
                    resourceBundle.getString("create-expense-alert1-title"),
                    resourceBundle.getString("create-expense-alert3-content"));
            return false;
        }
        return true;
    }
    /**
     * Method called when "Add" button is pressed
     */
    public void add() {
        if(!checkValues()) return;


        Expense expense = new Expense(expenseName.getText(), "description",
                expenseDate.getValue().atStartOfDay(), payer.getValue(), expenseValue.getValue());
        if(isPut) {
            expense.id = putExpense.id;
            expense = server.editExpense(event, expense);
        }
        else {
            expense = server.addExpense(event, expense);
        }
        List<Person> borrowers;
        if(equallyRadio.isSelected()) {
            borrowers = event.getParticipants();
        }
        else {
            borrowers = selectPeopleListView.getItems().stream()
                    .filter(PersonSelectionModel::isSelected)
                    .map(PersonSelectionModel::getPerson)
                    .collect(Collectors.toList());
        }
        for (Person p : borrowers) {
            Loan loan = new Loan(expenseValue.getValue()/(double)borrowers.size(),
                    payer.getValue(), p);
            server.addLoan(event, expense, loan);
        }
        mainCtrl.showEditEvent(event);
    }

    /**
     * Method called when "Abort" button is pressed
     */
    public void abort() {
        mainCtrl.showEditEvent(event);
    }

    /**
     * Method to handle keys pressed
     * 
     * @param e key that is being pressed
     *
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                add();
                break;
            case ESCAPE:
                abort();
                break;
            default:
                break;
        }
    }


    /**
     * Change which event the expense is being added to
     * @param event event object
     * @param expense the expense object for put operations
     */
    public void setEvent(Event event, Expense expense) {
        putExpense = expense;
        this.event = event;
        List<Person> participants = event.getParticipants();
        this.payer.setItems(FXCollections.observableArrayList(participants));

        ObservableList<PersonSelectionModel> people = FXCollections.observableArrayList();
        for (Person p : participants) {
            people.add(new PersonSelectionModel(p, false));
        }

        selectPeopleListView.setItems(people);
        selectPeopleListView.setCellFactory(selectPeopleListView -> new PersonCheckBoxListCell());

        if(expense == null) {
            isPut = false;
            expenseDate.setValue(LocalDate.from(LocalDate.now()));
            expenseName.clear();
            if (expenseValue.getValueFactory() != null)
                expenseValue.getValueFactory().setValue(0.0);
            title.setText(resourceBundle.getString("create-expense-title-add"));
        }
        else {
            isPut = true;
            payer.getSelectionModel().select(expense.payer);
            expenseDate.setValue(LocalDate.from(expense.date));
            expenseName.setText(expense.name);
            if (expenseValue.getValueFactory() != null)
                expenseValue.getValueFactory().setValue(expense.totalAmount);
            title.setText(resourceBundle.getString("create-expense-title-edit"));
        }
        expenseType.clear();
    }

    /**
     * @return event
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Refreshes AddExpense scene
     */
    public void refresh() {
        if (event != null) {
            setEvent(server.findEventById(event.id), null);
        }
        resourceBundle = ResourceBundle.getBundle(
                "client.localization.Labels", Locale.of(appConfig.getSelectedLocale()));
    }
}
