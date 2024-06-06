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
package client;

import static com.google.inject.Guice.createInjector;
import com.sun.javafx.application.PlatformImpl.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Locale.Builder;
import java.util.ResourceBundle;

import client.scenes.*;
import client.utils.AppConfig;
import client.utils.AppWebSocketClient;
import client.utils.AppWebSocketService;
import client.utils.interfaces.WebSocketService;

import com.google.inject.Injector;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.util.Pair;

public class Main extends Application {

    private Injector injector = createInjector(new MyModule());
    private MyFXML fxml = new MyFXML(injector);

    private Pair<AddExpenseCtrl, Parent> addExpense;
    private Pair<EditEventCtrl, Parent> editEvent;
    private Pair<StartScreenCtrl, Parent> startScreen;
    private Pair<AdminOverviewCtrl, Parent> adminOverview;
    private Pair<AddLanguageCtrl, Parent> addLanguage;
    private Pair<AdminLoginCtrl, Parent> adminLogin;
    private Pair<AddPersonCtrl, Parent> addPerson;
    private Pair<SettleDebtsCtrl, Parent> settleDebts;
    private Pair<AboutCtrl, Parent> about;
    private Pair<SendInvitationCtrl, Parent> sendInvitation;
    private Pair<EditTitleCtrl, Parent> editTitle;

    private AppConfig config = new AppConfig();

    private AppWebSocketClient webSocketClient;

    /**
     * The main method that launches the JavaFX application.
     * 
     * @param args Command line arguments.
     * @throws URISyntaxException if a URI syntax error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    /**
     * Default constructor
     */
    public Main() {
        this.injector = createInjector(new MyModule());
        this.fxml = new MyFXML(injector);
        // this(createInjector(new MyModule()), new MyFXML(INJECTOR));
    }

    /**
     * Constructor (for tests)
     * 
     * @param injector injector
     * @param fxml     fxml loader
     */
    public Main(Injector injector, MyFXML fxml) {
        this.injector = injector;
        this.fxml = fxml;
    }

    /**
     * Starts the JavaFX application and sets up the primary stage.
     * This method initializes controllers and views for different parts of the application.
     * 
     * @param primaryStage The primary stage for this application.
     * @throws IOException if an I/O error occurs during loading FXML files.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        var mainCtrl = injector.getInstance(MainCtrl.class);

        mainCtrl.setClient(this);
        mainCtrl.setLocales(getAvailableLocales());

        loadScenes(getSelectedLocate());

        List<Object> paramsKey = new ArrayList<>();
        paramsKey.add(addExpense.getKey());
        paramsKey.add(editEvent.getKey());
        paramsKey.add(startScreen.getKey());
        paramsKey.add(adminOverview.getKey());
        paramsKey.add(addLanguage.getKey());
        paramsKey.add(adminLogin.getKey());
        paramsKey.add(addPerson.getKey());
        paramsKey.add(settleDebts.getKey());
        paramsKey.add(about.getKey());
        paramsKey.add(sendInvitation.getKey());
        paramsKey.add(editTitle.getKey());

        List<Parent> paramsValue = new ArrayList<>();
        paramsValue.add(addExpense.getValue());
        paramsValue.add(editEvent.getValue());
        paramsValue.add(startScreen.getValue());
        paramsValue.add(adminOverview.getValue());
        paramsValue.add(addLanguage.getValue());
        paramsValue.add(adminLogin.getValue());
        paramsValue.add(addPerson.getValue());
        paramsValue.add(settleDebts.getValue());
        paramsValue.add(about.getValue());
        paramsValue.add(sendInvitation.getValue());
        paramsValue.add(editTitle.getValue());

        mainCtrl.initialize(paramsKey, paramsValue, primaryStage);

        initializeWebSocket(mainCtrl);
    }

    /**
     * Method to initialize the web socket connection
     * 
     * @param mainCtrl
     */
    public void initializeWebSocket(MainCtrl mainCtrl) {
        try {
            URI serverUri = new URI(config.getWebSocketUrl());
            webSocketClient = new AppWebSocketClient(null, mainCtrl);
            WebSocketService webSocketService = new AppWebSocketService(serverUri, webSocketClient);
            webSocketClient.setWebSocketService(webSocketService);
            webSocketClient.initialize();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mainCtrl.primaryStage.setOnCloseRequest(event -> {
            startScreen.getKey().stop();
        });
    }

    /**
     * Method called when the application is closed.
     * We should explicitly close the websocket connection.
     */
    @Override
    public void stop() {
        if (this.webSocketClient != null) {
            this.webSocketClient.close();
        }
    }

    /**
     * Method to load the scenes and their controls. Moving to this function allows
     * reloading scenes during language switch
     * 
     * @param locale (new) locale, with which scenes are to be loaded
     */
    private void loadScenes(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("client.localization.Labels", locale);

        addExpense = fxml.load(AddExpenseCtrl.class, bundle, 
            "client", "scenes", "AddExpense.fxml");
        editEvent = fxml.load(EditEventCtrl.class, bundle, 
            "client", "scenes", "EditEvent.fxml");
        startScreen = fxml.load(StartScreenCtrl.class, bundle, 
            "client", "scenes", "StartScreen.fxml");
        adminOverview = fxml.load(AdminOverviewCtrl.class, bundle, 
            "client", "scenes", "AdminOverview.fxml");
        addLanguage = fxml.load(AddLanguageCtrl.class, bundle, 
            "client", "scenes", "AddLanguage.fxml");
        adminLogin = fxml.load(AdminLoginCtrl.class, bundle, 
            "client", "scenes", "AdminLogin.fxml");
        addPerson = fxml.load(AddPersonCtrl.class, bundle,
            "client", "scenes", "AddPerson.fxml");
        settleDebts = fxml.load(SettleDebtsCtrl.class, bundle,
                "client", "scenes", "SettleDebts.fxml");
        about = fxml.load(AboutCtrl.class, bundle,
                "client", "scenes", "About.fxml");
        sendInvitation = fxml.load(SendInvitationCtrl.class, bundle,
                "client", "scenes", "SendInvitation.fxml");
        editTitle = fxml.load(EditTitleCtrl.class, bundle,
                "client", "scenes", "EditTitle.fxml");
    }

    /**
     * Reload the scenes with new locale and retrieve them as Parent
     * 
     * @param newLocale new locale
     * @return          array of Parent objects representing scenes
     */
    public Parent[] getLocalizedParents(Locale newLocale) {
        loadScenes(newLocale);

        Parent[] parents = {addExpense.getValue(), editEvent.getValue(),
                startScreen.getValue(), adminOverview.getValue(), addLanguage.getValue(),
                adminLogin.getValue(), addPerson.getValue(), settleDebts.getValue(),
                about.getValue(), sendInvitation.getValue(), editTitle.getValue()};

        return parents;
    }

    /**
     * Getter for app's config (to be accessible in other classes)
     * 
     * @return config
     */
    public AppConfig getConfig() {
        return config;
    }

    /**
     * Setter for app's config (for tests mostly)
     * 
     * @param config
     */
    public void setConfig(AppConfig config) {
        this.config = config;
    }

    /**
     * Method to get available locales as list of Locale
     * 
     * @return list of Locale objects that are available
     */
    public List<Locale> getAvailableLocales() {
        List<Locale> availableLocales = new ArrayList<>();

        String[] localeStrings = getConfig().getAvailableLocales();

        Builder builder = new Locale.Builder();

        for (String ls : localeStrings) {
            String[] localeTokens = ls.split("_");
            String language = localeTokens[0];
            String region = localeTokens[1];

            Locale locale = builder.setLanguage(language).setRegion(region).build();
            availableLocales.add(locale);
        }

        return availableLocales;
    }

    /**
     * Retrieve the selected locale from config
     * 
     * @return the Locale object representing selected locale
     */
    public Locale getSelectedLocate() {
        String[] localeTokens = config.getSelectedLocale().split("_");
        String language = localeTokens[0];
        String region = localeTokens[1];

        Locale locale = new Locale.Builder().setLanguage(language).setRegion(region).build();
        return locale;
    }
}