package client.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import commons.Event;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

public class JsonController {

    private ObjectMapper objectMapper;
    private FileChooser fileChooser;

    private UIAlertService alertService;
    private ResourceBundle resourceBundle;
    /**
     * Initialises objectmapper and filechooser.
     * Objectmapper writes the dates in ISO8601
     * Filechooser filters on JSON only
     * @param objectMapper The objectmapper to initialize
     * @param fileChooser the fileChooser to initialize
     * @param alertService the alertService to inject
     * @param resourceBundle the resourceBundle to inject
     */
    public JsonController(ObjectMapper objectMapper, FileChooser fileChooser,
                          UIAlertService alertService, ResourceBundle resourceBundle) {
        this.objectMapper = objectMapper;
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        this.fileChooser = fileChooser;
        FileChooser.ExtensionFilter filter =
                new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(filter);

        this.alertService = alertService;
        this.resourceBundle = resourceBundle;
    }

    /**
     * Exports the selected event with JFileChooser,
     * and makes it .json if it isn't in the correct format yet.
     * @param e Event to save
     * @throws IOException Exception if file doesn't exist
     */
    public void exportEvent(Event e) throws IOException {
        fileChooser.setTitle(resourceBundle.getString("json-controller-export"));
        fileChooser.setInitialFileName(e.getTitle() + ".json");
        File saveFile = fileChooser.showSaveDialog(null);
        if (saveFile != null) {
            objectMapper.writeValue(saveFile, e);
            System.out.println("File saved at: " + saveFile.getAbsolutePath());
        }
        else{
            System.out.println("File save cancelled");
        }
    }

    /**
     * Creates an event out of the chosen file with the fileChooser, using the objectMapper
     * @return Returns the event to import
     */
    public Event importEvent(){
        fileChooser.setTitle(resourceBundle.getString("json-controller-import"));
        File selectedSave = fileChooser.showOpenDialog(null);
        if (selectedSave != null) {
            System.out.println("File imported at: " + selectedSave.toURI());
            try{
                return objectMapper.readValue(selectedSave, Event.class);
            }
            catch (Exception e) {
                alertService.showAlert(Alert.AlertType.ERROR,
                        resourceBundle.getString("json-controller-alert-title"),
                        resourceBundle.getString("admin-overview-alert1-title"),
                        resourceBundle.getString("json-controller-alert-content"));
                return null;
            }
        }
        else{
            System.out.println("Import cancelled");
            return null;
        }
    }
}
