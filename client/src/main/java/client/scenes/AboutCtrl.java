package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;

import javax.inject.Inject;

public class AboutCtrl {

    private final MainCtrl mainCtrl;
    @FXML
    public Button backy;

    /**
     * @param mainCtrl mainCtrl
     */
    @Inject
    public AboutCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * back button callback
     */
    @FXML
    private void backButton() {
        mainCtrl.showStartScreen();
    }


    /**
     * Method called when ESC key or back button is pressed
     */
    public void showStartScreen() {
        mainCtrl.showStartScreen();
    }

    /**
     * Method to handle the keyboard key pressed event
     *
     * @param e event
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ESCAPE:
                showStartScreen();
                break;
            default:
                break;
        }
    }
}
