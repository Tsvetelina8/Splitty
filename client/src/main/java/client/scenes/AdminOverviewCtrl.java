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

import client.utils.AppConfig;
import client.utils.JsonController;
import client.utils.LanguageSwitchUtil;
import client.utils.ServerUtils;
import client.utils.UIAlertService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import commons.Event;
import jakarta.ws.rs.core.Response;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TableView<Event> eventList;
    @FXML
    private TableColumn<Event, String> eventTitle;
    @FXML
    private TableColumn<Event, LocalDateTime> eventCreationDate;
    @FXML
    private TableColumn<Event, LocalDateTime> eventLastActivity;

    @FXML
    private Menu languageIndicator;

    private JsonController jsonController;

    private Event selectedEvent;
    private boolean ctrlPressed = false;
    private UIAlertService alertService;
    private ResourceBundle resourceBundle;
    private AppConfig appConfig;

    /**
     * Constructs a AdminOverviewCtrl with the given server utility and main controller.
     *
     * @param server   The server utility for handling server-side operations.
     * @param mainCtrl The main controller of the application.
     */
    @Inject
    public AdminOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Constructor used for testing
     * @param params An array consisting of all the parameters
     */
    public AdminOverviewCtrl(Object[] params) {
        this.server = (ServerUtils) params[0];
        this.mainCtrl = (MainCtrl) params[1];
        this.eventList = (TableView<Event>) params[2];
        this.eventTitle = (TableColumn<Event, String>) params[3];
        this.eventCreationDate = (TableColumn<Event, LocalDateTime>) params[4];
        this.eventLastActivity = (TableColumn<Event, LocalDateTime>) params[5];
        this.jsonController = (JsonController) params[6];
        this.selectedEvent = (Event) params[7];
        this.alertService = (UIAlertService) params[8];
        this.ctrlPressed = (boolean) params[9];
        this.resourceBundle = (ResourceBundle) params[10];
        this.appConfig = (AppConfig) params[11];
        this.languageIndicator = (Menu) params[12];
    }

    /**
     * Initializes the controller class.
     * This method is called automatically after the FXML fields have been populated.
     * Sets up the table columns and their data sources.
     *
     * @param location  The location used to resolve relative paths for the root object.
     * @param resources The resources used to localize the root object.
     */
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        appConfig = mainCtrl.getConfig();
        resourceBundle = ResourceBundle.getBundle(
                "client.localization.Labels", Locale.of(appConfig.getSelectedLocale()));
        initEventList();
        fillLanguageSwitch();
        refreshEvents();

        if (resources != null)
            LanguageSwitchUtil.initIndicator(languageIndicator, mainCtrl.getSelectedLocale());
            
        jsonController = new JsonController(new ObjectMapper(), new FileChooser(),
                alertService, resourceBundle);
        alertService = new UIAlertService();
    }

    /**
     * Initialises the list of events in the server in a TableView.
     * It also adds a listener for the selected event.
     */
    private void initEventList(){
        eventTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        eventCreationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        eventLastActivity.setCellValueFactory(new PropertyValueFactory<>("lastActivity"));

        eventTitle.setId("title");
        eventCreationDate.setId("creationDate");
        eventLastActivity.setId("lastActivity");

        eventTitle.setText(resourceBundle.getString("admin-overview-title"));
        eventCreationDate.setText(resourceBundle.getString("admin-overview-creation-date"));
        eventLastActivity.setText(resourceBundle.getString("admin-overview-last-activity"));

        var listener = new ChangeListener<Event>() {
            @Override
            public void changed(ObservableValue<? extends Event> observable,
                                Event oldValue,
                                Event newValue) {
                selectedEvent = eventList.getSelectionModel().getSelectedItem();
                System.out.println("Current selected item: " + selectedEvent);
            }
        };
        eventList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        eventList.getSelectionModel()
                .selectedItemProperty()
                .addListener(listener);

        eventList.setRowFactory(rowFactory -> {
            TableRow<Event> row = new TableRow<>();
            row.setOnMouseClicked(clickEvent -> {
                if (clickEvent.getClickCount() == 2 && (!row.isEmpty()) ) {
                    mainCtrl.showEditEvent(row.getItem());
                }
            });
            return row ;
        });
    }

    /**
     * Refreshes the event view by adding all new events
     */
    public void refreshEvents() {
        for (Event e : server.getAllEvents()) {
            if (!eventList.getItems().contains(e)) eventList.getItems().add(e);
        }
    }

    /**
     * Refreshes the event view by adding deleting and adding all events
     */
    public void refreshEventsFull() {
        eventList.setItems(FXCollections.observableArrayList(server.getAllEvents()));
        resourceBundle = ResourceBundle.getBundle(
                "client.localization.Labels", Locale.of(appConfig.getSelectedLocale()));
    }

    /**
     * Deletes the selected event
     */
    public void deleteEvent(){
        if(selectedEvent != null) {
            if (deleteConfirmation().equals(ButtonType.OK)) {
                List<Integer> selectedItems =
                        eventList.getSelectionModel()
                                .getSelectedCells()
                                .stream()
                                .map(TablePosition::getRow)
                                .toList()
                                .reversed();
                System.out.println(selectedItems);

                for (Integer i : selectedItems) {
                    if (server.deleteEventById(eventList.getItems().get(i).id) == 204){
                        eventList.getItems().remove(eventList.getItems().get(i));
                    }
                }
            }
        }
    }

    /**
     * Imports an event from JSON
     */
    public void importEvent() throws IOException {
        Event newImport = jsonController.importEvent();
        if (newImport != null) {
            Response response = server.importEvent(newImport);
            if(response.getStatus() == 400){
                importError();
            }
            else {
                eventList.getItems().add(response.readEntity(Event.class));
            }
        }
    }

    /**
     * Shows the import error alert
     */
    public void importError() {
        alertService.showAlert(Alert.AlertType.ERROR,
                resourceBundle.getString("admin-overview-alert1-title"),
                resourceBundle.getString("admin-overview-alert1-title"),
                resourceBundle.getString("admin-overview-alert1-content"));
        ctrlPressed = false;
    }

    /**
     * Shows the delete confirmation alert
     *
     * @return The confirmation to delete the event
     */
    public ButtonType deleteConfirmation() {
        ctrlPressed = false;
        return alertService.showAlert(Alert.AlertType.CONFIRMATION,
                resourceBundle.getString("admin-overview-alert2-title"),
                resourceBundle.getString("admin-overview-alert2-header"),
                resourceBundle.getString("admin-overview-alert2-content")).get();
    }


    /**
     * Exports an event to JSON
     */
    public void exportEvent() throws IOException {
        if(selectedEvent != null) {
            jsonController.exportEvent(selectedEvent);
        }
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
            e.printStackTrace();
        }
    }

    private void switchLanguage(Locale locale) {
        List<Event> serverEvents = eventList.getItems();

        mainCtrl.switchLanguage(locale);
        LanguageSwitchUtil.initIndicator(languageIndicator, mainCtrl.getSelectedLocale());

        eventList.getItems().setAll(serverEvents);
    }

    /**
     * Method called when ESC key or back button is pressed
     */
    public void showStartScreen() {
        mainCtrl.showStartScreen();
    }

    /**
     * Gets the event list
     * @return the event list
     */
    public TableView<Event> getEventList() {
        return eventList;
    }

    /**
     * Gets the selected event
     * @return the selected event
     */
    public Event getSelectedEvent() {
        return selectedEvent;
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
     * Returns ctrlPressed
     * @return if ctrl is pressed
     */
    public boolean getCtrlPressed() {
        return ctrlPressed;
    }

    /**
     * Method to handle keys pressed
     *
     * @param e key that is being pressed
     *
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case DELETE:
                deleteEvent();
                break;
            case ESCAPE:
                showStartScreen();
                break;
            case I:
                if(ctrlPressed) {
                    try {
                        importEvent();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                break;
            case E:
                if(ctrlPressed) {
                    try {
                        exportEvent();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                break;
            case D:
                if(ctrlPressed) deleteEvent();
                break;
            case CONTROL:
                ctrlPressed = true;
                break;
            default:
                break;
        }
    }

    /**
     * Method to handle keys released
     *
     * @param e key that is being released
     *
     */
    public void keyReleased(KeyEvent e) {
        if (Objects.requireNonNull(e.getCode()) == KeyCode.CONTROL) {
            ctrlPressed = false;
        }
    }
}
