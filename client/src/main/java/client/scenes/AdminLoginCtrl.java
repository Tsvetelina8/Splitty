package client.scenes;

import client.utils.LanguageSwitchUtil;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class AdminLoginCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Menu languageIndicator;

    private Alert alert;

    /**
     * Constructs a AdminOverviewCtrl with the given server utility and main controller.
     *
     * @param server   The server utility for handling server-side operations.
     * @param mainCtrl The main controller of the application.
     */
    @Inject
    public AdminLoginCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        alert = new Alert(Alert.AlertType.ERROR);
    }

    /**
     * Constructor used for testing
     *
     * @param server   The server utility for handling server-side operations.
     * @param mainCtrl The main controller of the application.
     * @param alert Alert to inject
     * @param passwordField PasswordField to inject
     * @param languageIndicator Menu field for language switching
     */
    public AdminLoginCtrl(ServerUtils server, MainCtrl mainCtrl,
                          Alert alert, PasswordField passwordField,
                          Menu languageIndicator) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.alert = alert;
        this.passwordField = passwordField;
        this.languageIndicator = languageIndicator;
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
        fillLanguageSwitch();
        if (resources != null)
            LanguageSwitchUtil.initIndicator(languageIndicator, mainCtrl.getSelectedLocale());
    }

    /**
     * Login to the admin screen
     */
    public void loginAdmin() {
        if(server.verifyPassword(passwordField.getText()).equals("Ok")) {
            passwordField.clear();
            mainCtrl.showAdminOverview();
        }
        else {
            alert.setTitle("Wrong password!");
            alert.setHeaderText("Wrong password!");
            alert.setContentText("You entered the wrong password!\n\nTry again.");
            alert.showAndWait();
        }
    }

    /**
     * Method called when ESC key or back button is pressed
     */
    public void showStartScreen() {
        mainCtrl.showStartScreen();
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
        String pwd = passwordField.getText();

        mainCtrl.switchLanguage(locale);
        LanguageSwitchUtil.initIndicator(languageIndicator, mainCtrl.getSelectedLocale());

        passwordField.setText(pwd);
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
                loginAdmin();
                break;
            case ESCAPE:
                showStartScreen();
                break;
            default:
                break;
        }
    }
}
