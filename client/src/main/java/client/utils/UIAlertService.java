package client.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class UIAlertService {

    /**
     * Method to show alert
     *
     * @param alertType type of alert
     * @param title     title
     * @param header    header
     * @param content   content
     * @return The button pressed
     */
    public Optional<ButtonType> showAlert(Alert.AlertType alertType,
                                          String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }
}
