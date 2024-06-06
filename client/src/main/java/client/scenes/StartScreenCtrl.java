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

import client.utils.LanguageSwitchUtil;
import client.utils.ServerUtils;
import client.utils.UIAlertService;
import com.google.inject.Inject;
import commons.Event;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class StartScreenCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField newEvent;

    @FXML
    private TextField joinEvent;

    @FXML
    private ListView<Event> eventListTitles;

    @FXML
    private Menu languageIndicator;

    @FXML
    private Button editEvent;

    @FXML
    private Button deleteEvent;

    @FXML
    private Button joinEventButton;

    @FXML
    private Button createEventButton;

    @FXML
    private Button adminLoginButton;

    private Event selectedEvent;

    private UIAlertService alertService;

    private boolean ctrlPressed = false;

    /**
     * Constructs a StartScreenCtrl with the given server utility and main controller.
     * 
     * @param server current server
     * @param mainCtrl main controller
     */
    @Inject
    public StartScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        alertService = new UIAlertService();
    }

    /**
     * This constructor is for testing
     * @param params The objects to be injected
     */
    public StartScreenCtrl(Object[] params) {
        this.mainCtrl = (MainCtrl) params[1];
        this.server = (ServerUtils) params[0];
        this.eventListTitles = (ListView<Event>) params[2];
        this.newEvent = (TextField) params[3];
        this.joinEvent = (TextField) params[4];
        this.selectedEvent = (Event) params[5];
        this.alertService = (UIAlertService) params[6];
        this.languageIndicator = (Menu) params[7];
    }

    /**
     * Getter for selected event
     * @return selected
     */
    public Event getSelectedEvent() {
        return selectedEvent;
    }

    /**
     * Getter for event list titles
     * @return event list
     */
    public ListView<Event> getEventListTitles() {
        return eventListTitles;
    }

    /**
     * Getter for new event text field
     * @return event list
     */
    public TextField getNewEvent() {
        return newEvent;
    }

    /**
     * Getter for join event text field
     * @return event list
     */
    public TextField getJoinEvent() {
        return joinEvent;
    }

    /**
     * setter for events
     * @param events events
     */
    public void setEvents(List<Event> events) {
        this.eventListTitles.setItems(FXCollections.observableArrayList(events));
    }

    /**
     * Initializes the controller class. 
     * This method is called automatically after the FXML fields have been populated.
     *
     * @param location  The location used to resolve relative paths for the root object.
     * @param resources The resources used to localize the root object.
     */
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        initListView();
        setDefaults();
        initIcons();
        fillLanguageSwitch();
        if (resources != null)
            LanguageSwitchUtil.initIndicator(languageIndicator, mainCtrl.getSelectedLocale());
        server.registerForUpdatesEvent(q -> {
            eventListTitles.getItems().add(q);
        });

    }

    /**
     * stop method that kills the thread that handles long polling
     */
    public void stop() {
        server.stop();
    }

    private void initListView() {
        eventListTitles.setCellFactory(cellFactory -> new ListCell<>() {
            @Override
            protected void updateItem(Event event, boolean empty) {
                super.updateItem(event, empty);
                setText(event == null ? null : event.getTitle());
            }
        });

        eventListTitles.getItems().clear();
        refresh();
        var listener = new ChangeListener<Event>() {
            @Override
            public void changed(ObservableValue<? extends Event> observable,
                                Event oldValue,
                                Event newValue) {
                selectedEvent = eventListTitles.getSelectionModel().getSelectedItem();
                System.out.println("Current selected item: " + selectedEvent);
            }
        };

        eventListTitles.getSelectionModel()
                 .selectedItemProperty()
                 .addListener(listener);

        eventListTitles.setOnMouseClicked(clickEvent -> {
            if (clickEvent.getClickCount() == 2 && (eventListTitles != null) ) {
                mainCtrl.showEditEvent(selectedEvent);
            }
        });

        eventListTitles.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER && (eventListTitles != null) ) {
                mainCtrl.showEditEvent(selectedEvent);
            }
        });
    }

    /**
     * Resets the scene to Default values
     */
    private void setDefaults() {
        newEvent.clear();
        joinEvent.clear();
    }

    /**
     * Set icons to suitable buttons
     */
    private void initIcons() {
        try {
            setButtonIcon(createEventButton, "/client/icons/icons8-add-48.png");
            setButtonIcon(editEvent, "/client/icons/icons8-edit-48.png");
            setButtonIcon(deleteEvent, "/client/icons/icons8-delete-48.png");
            setButtonIcon(joinEventButton, "/client/icons/icons8-join-48.png");
            setButtonIcon(adminLoginButton, "/client/icons/icons8-login-48.png");
        } catch (NullPointerException e) {
            // In tests all these buttons are null, so I made this try for now.
        }
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
        List<Event> viewedEvents = eventListTitles.getItems();
        String createString = newEvent.getText();
        String joinString = joinEvent.getText();

        mainCtrl.switchLanguage(locale);

        LanguageSwitchUtil.initIndicator(languageIndicator, mainCtrl.getSelectedLocale());
        eventListTitles.getItems().setAll(viewedEvents);
        newEvent.setText(createString);
        joinEvent.setText(joinString);
    }

    /**
     * Method called when MenuItem "Add Translation" gets clicked on
     */
    public void contributeLanguage() {
        mainCtrl.showAddLanguage();
    }

    /**
     * Method triggered by add button
     */
    public void addEvent() {
        try {
            Event addedEvent = server.createEvent(newEvent.getText());
            eventListTitles.getItems().add(addedEvent);
            System.out.println("Add button pressed, " + addedEvent.getTitle()
                    + " added to event list.");

            if (mainCtrl != null) mainCtrl.showEditEvent(addedEvent);
        }
        catch (Exception e) {
            e.printStackTrace();
            showCreateError();
        }
        refresh();
    }

    /**
     * Method triggered by join button
     */
    public void joinEvent() {
        System.out.println("Join button pressed");
        System.out.println("Invite code: "+ joinEvent.getText());

        try {
            Event addedEvent = server.findEventByCode(joinEvent.getText());
            System.out.println("Get event");
            System.out.println(addedEvent.toString());
            if (eventListTitles.getItems().contains(addedEvent)) return;
            eventListTitles.getItems().add(addedEvent);

            if (mainCtrl != null) mainCtrl.showEditEvent(addedEvent);
        }
        catch (Exception e) {
            showJoinError();
        }
    }

    /**
     * Method to show an alert when trying to join event with invalid code
     */
    public void showJoinError() {
        try {
            ResourceBundle r = ResourceBundle.getBundle(
                "client.localization.Labels", mainCtrl.getSelectedLocale());

            alertService.showAlert(Alert.AlertType.ERROR,
                    r.getString("start-screen-alert-code-title"),
                    r.getString("start-screen-alert-code-header"),
                    r.getString("start-screen-alert-code-desc"));
        } catch (NullPointerException e) {
            alertService.showAlert(Alert.AlertType.ERROR, "Invalid code",
                "Invalid code.","No event found with the given invite code.");
        }
    }

    /**
     * Method to show an alert when trying to create event with invalid title
     */
    public void showCreateError() {
        ResourceBundle r = ResourceBundle.getBundle(
            "client.localization.Labels", mainCtrl.getSelectedLocale());

        alertService.showAlert(Alert.AlertType.ERROR,
                r.getString("start-screen-alert-create-title"),
                r.getString("start-screen-alert-create-header"),
                r.getString("start-screen-alert-create-desc"));
    }

    /**
     * Method triggered by delete button
     */
    public void deleteEvent() {
        System.out.println("Event to delete: " + selectedEvent);
        eventListTitles.getItems().remove(selectedEvent);
    }

    /**
     * Method triggered by edit button
     */
    public void editEvent() {
        if (selectedEvent!=null) {
            mainCtrl.showEditEvent(selectedEvent);
        }
    }

    /**
     * Redirects to the admin login screen
     */
    public void loginAdmin() {
        mainCtrl.showAdminLogin();
    }

    /**
     * method for about page
     */
    public void aboutPage() {
        mainCtrl.showAbout();
    }

    /**
     * Handles the event of a key being pressed while in AddQuote Scene.
     * 
     * @param e Event instance.
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                if(newEvent.isFocused()) addEvent();
                else if(joinEvent.isFocused()) joinEvent();
                break;
            case L:
                if(ctrlPressed) loginAdmin();
                break;
            case D:
                if(ctrlPressed) deleteEvent();
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

    /**
     * Gets ctrlpressed
     * @return If ctrl is pressed
     */
    public boolean getCtrlPressed() {
        return ctrlPressed;
    }


    /**
     * Method called to refresh the start screen, loading participants and expenses
     */
    public void refresh() {
        List<Event> events = server.getAllEvents();
        eventListTitles.getItems().removeIf(e -> !events.contains(e));
        for (Event e : events) {
            if (eventListTitles.getItems().contains(e)){
                eventListTitles.getItems().remove(e);
                eventListTitles.getItems().add(e);
            }
        }
    }
}
