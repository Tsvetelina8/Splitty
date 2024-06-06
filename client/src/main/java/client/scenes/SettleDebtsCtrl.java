package client.scenes;

import client.ui.DebtsListCell;
import client.utils.LanguageSwitchUtil;
import client.utils.ServerUtils;
import commons.Event;
import commons.Loan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.inject.Inject;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SettleDebtsCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private Label eventName;

    @FXML
    private ListView<Loan> debtsListView;

    @FXML
    private Menu languageIndicator;

    @FXML
    private Button back;

    private Event event;


    /**
     * @param server current server
     * @param mainCtrl main controller
     */
    @Inject
    public SettleDebtsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Constructor with specified scene elements.
     * Simplifies testing
     *
     * @param params array of parameters
     */
    public SettleDebtsCtrl(Object[] params) {
        this.server = (ServerUtils) params[0];
        this.mainCtrl = (MainCtrl) params[1];
        this.event = (Event) params[2];
        this.back = (Button) params[4];
        this.eventName = (Label) params[5];
        this.languageIndicator = (Menu) params[6];
        this.debtsListView = (ListView<Loan>) params[7];
    }

    /**
     * Set event
     * @param event Event entity
     */
    public void setEvent(Event event) {
        eventName.setText(event.getTitle());
        this.event = event;
        refresh();
    }


    /**
     * Method to initialize the elements of the Scene
     */
    @FXML
    public void initialize() {
        fillLanguageSwitch();
        LanguageSwitchUtil.initIndicator(languageIndicator, mainCtrl.getSelectedLocale());
        try {
            eventName.setText(event.getTitle());
        } catch (NullPointerException e) {}
    }

    /**
     * A getter for event
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
        ObservableList<Loan> debtsList = debtsListView.getItems();

        mainCtrl.switchLanguage(locale);
        LanguageSwitchUtil.initIndicator(languageIndicator, locale);

        debtsListView.setItems(debtsList);
        debtsListView.setCellFactory(debtsListView ->
                new DebtsListCell(mainCtrl, eventName.getText()).attatchEvent(event));
    }

    /**
     * Method called to refresh the event, loading loans
     */
    public void refresh() {
        event = server.findEventById(event.id);
        List<Loan> loansSettled = event.settleDebts();
        ObservableList<Loan> loans = FXCollections.observableArrayList(loansSettled);

        debtsListView.setItems(loans);
        debtsListView.setCellFactory(debtListView ->
                new DebtsListCell(mainCtrl, eventName.getText()).attatchEvent(event));
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
     * Method called when "Back" button is pressed
     */
    public void back() {
        mainCtrl.showEditEvent(event);
    }

    /**
     * Method to handle the keyboard key pressed event
     *
     * @param e event
     */
    public void keyPressed(KeyEvent e) {
        if (Objects.requireNonNull(e.getCode()) == KeyCode.ESCAPE) {
            back();
        }
    }


}
