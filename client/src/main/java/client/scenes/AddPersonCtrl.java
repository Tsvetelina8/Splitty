package client.scenes;

import client.utils.LanguageSwitchUtil;
import client.utils.ServerUtils;
import client.utils.UIAlertService;
import commons.Event;
import commons.Person;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

import javax.inject.Inject;
import java.util.Locale;
import java.util.ResourceBundle;


public class AddPersonCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private Menu languageIndicator;

    @FXML
    private Button participantAdd;

    @FXML
    private Button participantCancel;

    @FXML
    private Label title;

    private Event event;

    private boolean isPut = false;

    private Person person;

    private UIAlertService uiAlertService;

    private ResourceBundle resourceBundle;

    /**
     * @param server current server
     * @param mainCtrl main controller
     */
    @Inject
    public AddPersonCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Constructor with specified scene elements.
     * Simplifies testing
     *
     * @param params array of parameters
     */
    public AddPersonCtrl(Object[] params) {
        this.server = (ServerUtils) params[0];
        this.mainCtrl = (MainCtrl) params[1];
        this.firstName = (TextField) params[2];
        this.lastName = (TextField) params[3];
        this.participantAdd = (Button) params[5];
        this.participantCancel = (Button) params[6];
        this.title = (Label) params[7];
        this.languageIndicator = (Menu) params[8];
        this.uiAlertService = (UIAlertService) params[9];
        this.resourceBundle = (ResourceBundle) params[10];
    }

    /**
     * Method to initialize the elements of the Scene
     */
    @FXML
    public void initialize() {
        initIcons();
        fillLanguageSwitch();
        LanguageSwitchUtil.initIndicator(languageIndicator, mainCtrl.getSelectedLocale());
        setDefaults();
        uiAlertService = new UIAlertService();
        resourceBundle = ResourceBundle.getBundle(
                "client.localization.Labels", mainCtrl.getSelectedLocale());
    }

    /**
     * A getter for event
     * @return event
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Set icons to suitable buttons
     */
    private void initIcons() {
        setButtonIcon(participantAdd, "/client/icons/icons8-add-48.png");
        setButtonIcon(participantCancel, "/client/icons/icons8-cancel-48.png");
    }

    /**
     * Set icon to button
     *
     * @param button   button
     * @param iconPath path to icon
     * @param width    width of icon on display (px)
     * @param height   height of icon on display (px)
     */
    private void setButtonIcon(javafx.scene.control.Button button, String iconPath,
                               double width, double height) {
        javafx.scene.image.Image icon = new Image(getClass().getResourceAsStream(iconPath));
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
    private void setButtonIcon(javafx.scene.control.Button button, String iconPath) {
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
        String fName = firstName.getText();
        String lName = lastName.getText();

        mainCtrl.switchLanguage(locale);
        LanguageSwitchUtil.initIndicator(languageIndicator, mainCtrl.getSelectedLocale());

        firstName.setText(fName);
        lastName.setText(lName);
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
        firstName.clear();
        lastName.clear();
    }

    /**
     * Method called when "Add" button is pressed
     */
    public void add() {
        String firstNameString = firstName.getText();
        String lastNameString = lastName.getText();
        if (firstNameString.isEmpty() || lastNameString.isEmpty()) {
            showCreateError();
        }
        if (isPut) {
            try {
                server.updatePerson(new Person(firstNameString, lastNameString), event, person.id);
            }
            catch (Exception e) {
                personDeleteAlert();
                return;
            }
        }
        else {
            server.addPerson(event,
                    new Person(firstNameString, lastNameString));
        }

        System.out.println("Add button pressed, " + firstName.getText() + " "
                + lastName.getText() + " added to event list.");
        for(Person p: event.getParticipants()) {
            System.out.print(p + " ");
        }
        if (mainCtrl != null) {
            mainCtrl.showEditEvent(event);
        }
    }

    /**
     * Setter for event, and if the scene is to put a person
     *
     * @param event
     * @param person
     */
    public void setEvent(Event event, Person person) {
        this.event = event;
        if(person == null) {
            isPut = false;
            setDefaults();
            title.setText("Add Person");

        }
        else {
            isPut = true;
            title.setText("Edit Person");
            firstName.setText(person.firstName);
            lastName.setText(person.lastName);
            this.person = person;
        }
    }

    /**
     * Method called when "Abort" button is pressed
     */
    public void cancel() {
        mainCtrl.showEditEvent(event);
    }

    /**
     * Method to show an alert when trying to add a person without all fields inputted
     */
    public void showCreateError() {
        uiAlertService.showAlert(Alert.AlertType.ERROR,
                resourceBundle.getString("add-person-alert-title"),
                resourceBundle.getString("add-person-alert-title"),
                resourceBundle.getString("add-person-alert-desc"));
    }

    /**
     * Shows the alert when the event is deleted, and redirects to start screen.
     */
    public void personDeleteAlert() {
        uiAlertService.showAlert(Alert.AlertType.INFORMATION,
                resourceBundle.getString("add-person-delete-alert-title"),
                resourceBundle.getString("add-person-delete-alert-title"),
                resourceBundle.getString("add-person-delete-alert-desc"));
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
                cancel();
                break;
            default:
                break;
        }
    }

}
