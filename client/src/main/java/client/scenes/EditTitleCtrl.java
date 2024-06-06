package client.scenes;

import client.utils.ServerUtils;
import client.utils.UIAlertService;
import com.google.inject.Inject;
import commons.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class EditTitleCtrl implements Initializable {

    private long id;
    private final ServerUtils server;

    private final MainCtrl mainCtrl;
    private Event event;

    @FXML
    public Button changeButton;

    @FXML
    private TextField textField;

    private UIAlertService uiAlertService;

    private ResourceBundle resourceBundle;
    /**
     * Constructs a EditTitleCtrl with the given server utility and main controller.
     *
     * @param server   The server utility for handling server-side operations.
     * @param mainCtrl The main controller of the application.
     */
    @Inject
    public EditTitleCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Constructor for testing
     * @param textField the text field to inject
     * @param mainCtrl the mainCtrl
     * @param server the server
     * @param uiAlertService the alertService
     */
    public EditTitleCtrl(ServerUtils server, MainCtrl mainCtrl,
                         TextField textField, UIAlertService uiAlertService) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.textField = textField;
        this.uiAlertService = uiAlertService;
        resourceBundle = ResourceBundle.getBundle(
                "client.localization.Labels", mainCtrl.getSelectedLocale());
    }

    /**
     * sets event for the ctrl
     * @param event event that is assigned
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Getter for event, for testing
     * @return the event
     */
    public Event getEvent() {
        return event;
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
        uiAlertService = new UIAlertService();
        resourceBundle = ResourceBundle.getBundle(
                "client.localization.Labels", mainCtrl.getSelectedLocale());
    }

    /**
     * set the title
     */
    public void setTitle() {
        try {
            server.updateTitle(textField.getText(), id);
        }
        catch (Exception e) {
            eventDeleteAlert();
            return;
        }
        showEditEvent();
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
     * Method called when ESC key or back button is pressed
     */
    public void showEditEvent() {
        event.setTitle(textField.getText());
        mainCtrl.showEditEvent(event);
    }

    /**
     * @param id id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for id, for testing purposes
     * @return the id
     */
    public long getId() {
        return id;
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
                setTitle();
                break;
            case ESCAPE:
                showEditEvent();
                break;
            default:
                break;
        }
    }
}

