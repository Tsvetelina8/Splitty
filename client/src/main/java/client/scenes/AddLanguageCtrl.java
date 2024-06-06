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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.*;

import com.google.inject.Inject;

import client.utils.LanguageItem;
import client.utils.ServerUtils;
import client.utils.UIAlertService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class AddLanguageCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final UIAlertService uiAlertService;

    @FXML
    private TextField languageLocale;

    @FXML
    private TableView<LanguageItem> languageTable;

    @FXML
    private Label languageTitle;

    @FXML
    private TableColumn<LanguageItem, String> languageLabel;

    @FXML
    private TableColumn<LanguageItem, String> languageDefault;

    @FXML
    private TableColumn<LanguageItem, String> languageTranslation;


    /**
     * Constructor for the Add Language scene control
     * 
     * @param server    current server
     * @param mainCtrl  main controller
     */
    @Inject
    public AddLanguageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.uiAlertService = new UIAlertService();
    }

    /**
     * Constructor with specified scene elements.
     * Simplifies testing
     * 
     * @param params array of parameters
     */
    public AddLanguageCtrl(Object[] params) {
        this.server = (ServerUtils) params[0];
        this.mainCtrl = (MainCtrl) params[1];
        this.languageLocale = (TextField) params[2];
        this.languageTable = (TableView<LanguageItem>) params[3];
        this.languageTitle = (Label) params[4];
        this.languageLabel = (TableColumn<LanguageItem, String>) params[5];
        this.languageDefault = (TableColumn<LanguageItem, String>) params[6];
        this.languageTranslation = (TableColumn<LanguageItem, String>) params[7];
        this.uiAlertService = (UIAlertService) params[8];
    }

    /**
     * Method to initialize the elements of the scene.
     */
    @FXML
    public void initialize() {
        languageLabel.setCellValueFactory(new PropertyValueFactory<>("label"));
        languageDefault.setCellValueFactory(new PropertyValueFactory<>("defaultValue"));
        languageTranslation.setCellValueFactory(new PropertyValueFactory<>("translation"));

        languageTranslation.setCellFactory(TextFieldTableCell.forTableColumn());
        languageTranslation.setOnEditCommit(e -> {
            final LanguageItem item = e.getRowValue();
            item.setTranslation(e.getNewValue());
        });

        fillTable();
    }

    /**
     * Method to fill the table with labels and default values for a localization
     */
    private void fillTable() {
        String localizationPath;
        try {
            localizationPath = getClass()
                    .getResource("/client/localization/Labels_en_US.properties")
                    .toURI().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        Properties props = new Properties();
        try (InputStream input = new FileInputStream(localizationPath)) {
            props.load(input);
            
            ObservableList<LanguageItem> tableData = FXCollections.observableArrayList();
            for (String key : props.stringPropertyNames()) {
                String value = props.getProperty(key);
                tableData.add(new LanguageItem(key, value));
            }
            languageTable.setItems(tableData);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Method to handle submission of a localization.
     * Checks if selected locale is valid, creates new properties and stores in a file
     * Then proceeds to start screen
     */
    @FXML
    public void submitButton() {
        String localeString = languageLocale.getText();

        if (!isValidLocale(localeString)) {
            alertInvalidLocale();
            return;
        }

        if (incompleteTranslations()) {
            alertIncompleteTranslations();
            return;
        }

        String localizationPath;
        try {
            localizationPath = getClass().getResource("/client/localization").toURI().getPath()
                    + "/Labels_" + localeString + ".properties";
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        String comment = localeString + " localization";

        Properties props = new Properties();
        for (LanguageItem item : languageTable.getItems()) {
            props.setProperty(item.getLabel(), item.getTranslation());
        }

        try (OutputStream output = new FileOutputStream(localizationPath)) {
            props.store(output, comment);
            mainCtrl.saveTranslation(localeString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        clearTranslations();
        mainCtrl.showStartScreen();
    }

    /**
     * Function to clear translations before navigating to other scenes
     */
    private void clearTranslations() {
        for (LanguageItem item : languageTable.getItems()) {
            item.setTranslation("");
        }
    }

    @FXML
    private void cancelButton() {
        clearTranslations();
        mainCtrl.showStartScreen();
    }

    private boolean incompleteTranslations() {
        for (LanguageItem item : languageTable.getItems()) {
            if (item.getTranslation() == null || item.getTranslation().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to check validity of a locale typed by user
     * 
     * @param localeString locale string (like "en_US")
     * @return             if it is valid
     */
    private boolean isValidLocale(String localeString) {
        Set<String> validLocales = new HashSet<>(Arrays.stream(Locale.getAvailableLocales())
                                                       .map(x -> x.toString()).toList());

        return validLocales.contains(localeString) && !localeString.equals("") 
                                                   && localeString != null;
    }

    /**
     * Method to show an alert about selecting an invalid locale
     */
    private void alertInvalidLocale() {
        String title = "Such locale does not exist!";
        String header = "You have entered an invalid locale";
        String content = "Such locale does not exist!\n\nTry inserting something like 'en_GB'";

        uiAlertService.showAlert(AlertType.ERROR, title, header, content);
    }

    /**
     * Method to show an alert when translations are incomplete
     */
    private void alertIncompleteTranslations() {
        String title = "Incomplete Translations";
        String header = "Translations Missing";
        String content = "Please ensure all translations are entered before saving.";

        uiAlertService.showAlert(AlertType.WARNING, title, header, content);
    }

    /**
     * Method to handle the keyboard key pressed event
     *
     * @param e event
     */
    public void keyPressed(KeyEvent e) {
        if (Objects.requireNonNull(e.getCode()) == KeyCode.ESCAPE) {
            mainCtrl.showStartScreen();
        }
    }
}
