package client.scenes;

import client.utils.EmailUtils;
import client.utils.LanguageSwitchUtil;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SendInvitationCtrl {

    MainCtrl mainCtrl;
    ServerUtils server;
    EmailUtils emailUtils;

    Event event;

    @FXML
    Label inviteCodeLabel;
    @FXML
    Label eventNameLabel;
    @FXML
    TextArea emailInput;

    @FXML
    private Menu languageIndicator;

    /**
     * Constructor for SendInvitation scene
     *
     * @param server   server
     * @param mainCtrl main control
     * @param utils    email utilities
     */
    @Inject
    public SendInvitationCtrl(ServerUtils server, MainCtrl mainCtrl, EmailUtils utils) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.emailUtils = utils;
    }

    /**
     * Constructor for SendInvitation scene used for testing
     *
     * @param params object array containing all controls
     */
    public SendInvitationCtrl(Object[] params) {
        mainCtrl = (MainCtrl) params[0];
        server = (ServerUtils) params[1];
        event = (Event) params[2];
        inviteCodeLabel = (Label) params[3];
        eventNameLabel = (Label) params[4];
        emailInput = (TextArea) params[6];
        emailUtils = (EmailUtils) params[7];
        languageIndicator = (Menu) params[8];
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
        String sInput = emailInput.getText();

        mainCtrl.switchLanguage(locale);
        LanguageSwitchUtil.initIndicator(languageIndicator, locale);

        String text = inviteCodeLabel.getText();
        inviteCodeLabel.setText(text.replace("{0}", event.getInviteCode()));
        emailInput.setText(sInput);

        eventNameLabel.setText(event.getTitle());
    }

    /**
     * Method to initialize the control
     */
    @FXML
    public void initialize() {
        fillLanguageSwitch();
        LanguageSwitchUtil.initIndicator(languageIndicator, mainCtrl.getSelectedLocale());
    }

    /**
     * Method to send invitation emails on send button press
     */
    public void sendPressed() {
        List<String> emails = Arrays.stream(emailInput.getText().split("\\n")).toList();
        emailUtils.sendInvites(event, emails);
    }

    /**
     * Method to go back to edit event scene after back button press
     */
    public void backPressed() {
        mainCtrl.showEditEvent(event);
    }

    /**
     * Method to send a test email.
     */
    public void testPressed() {
        try {
            emailUtils.sendTest();
        } catch (Exception ignored) {}
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
     * Set event
     * @param event Event entity
     */
    public void setEvent(Event event) {
        eventNameLabel.setText(event.getTitle());

        String text = inviteCodeLabel.getText();
        if (this.event != null) text = text.replace(this.event.getInviteCode(), "{0}");
        inviteCodeLabel.setText(text.replace("{0}", event.getInviteCode()));

        this.event = event;
    }

    /**
     * Method to handle the keyboard key pressed event
     *
     * @param e event
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                break;
            case ESCAPE:
                mainCtrl.showEditEvent(event);
                break;
            default:
                break;
        }
    }
}
