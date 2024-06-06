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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import client.Main;
import client.utils.AppConfig;
import commons.Event;
import commons.Expense;
import commons.Person;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainCtrl {

    public Stage primaryStage;

    private AddExpenseCtrl addExpenseCtrl;
    private Scene addExpense;
    private StartScreenCtrl startScreenCtrl;
    private Scene startScreen;

    private EditEventCtrl editEventCtrl;
    private Scene editEvent;

    private AdminOverviewCtrl adminOverviewCtrl;
    private Scene adminOverview;

    private AddLanguageCtrl addLanguageCtrl;
    private Scene addLanguage;

    private AdminLoginCtrl adminLoginCtrl;
    private Scene adminLogin;

    private SettleDebtsCtrl settleDebtsCtrl;
    private Scene settleDebts;

    private AddPersonCtrl addPersonCtrl;
    private Scene addPerson;

    private AboutCtrl aboutCtrl;
    private Scene about;

    private SendInvitationCtrl sendInvitationCtrl;
    private Scene sendInvitation;

    private EditTitleCtrl editTitleCtrl;
    private Scene editTitle;

    private Main client;
    private List<Locale> locales = new ArrayList<>();

    /**
     * Initialises primary stage and all scene controllers
     * @param paramsKey A list containing all the scene object
     * @param paramsValue A list containing all the scene parents
     * @param primaryStage The primary stage
     */
    public void initialize(List<Object> paramsKey, List<Parent> paramsValue, Stage primaryStage) {
        this.addExpenseCtrl = (AddExpenseCtrl) paramsKey.get(0);
        this.addExpense = new Scene(paramsValue.get(0));

        this.editEventCtrl = (EditEventCtrl) paramsKey.get(1);
        this.editEvent = new Scene(paramsValue.get(1));

        this.startScreenCtrl = (StartScreenCtrl) paramsKey.get(2);
        this.startScreen = new Scene(paramsValue.get(2));

        this.adminOverviewCtrl = (AdminOverviewCtrl) paramsKey.get(3);
        this.adminOverview = new Scene(paramsValue.get(3));

        this.addLanguageCtrl = (AddLanguageCtrl) paramsKey.get(4);
        this.addLanguage = new Scene(paramsValue.get(4));

        this.adminLoginCtrl = (AdminLoginCtrl) paramsKey.get(5);
        this.adminLogin = new Scene(paramsValue.get(5));

        this.addPersonCtrl = (AddPersonCtrl) paramsKey.get(6);
        this.addPerson = new Scene(paramsValue.get(6));

        this.settleDebtsCtrl = (SettleDebtsCtrl) paramsKey.get(7);
        this.settleDebts = new Scene(paramsValue.get(7));

        this.aboutCtrl = (AboutCtrl) paramsKey.get(8);
        this.about = new Scene(paramsValue.get(8));

        this.sendInvitationCtrl = (SendInvitationCtrl) paramsKey.get(9);
        this.sendInvitation = new Scene(paramsValue.get(9));

        this.editTitleCtrl = (EditTitleCtrl) paramsKey.get(10);
        this.editTitle = new Scene(paramsValue.get(10));

        this.primaryStage = primaryStage;

        primaryStage.getIcons().add(new Image(
                MainCtrl.class.getResourceAsStream("/client/icons/icons8-split-48.png")));

        showStartScreen();
        primaryStage.show();
    }

    /**
     * Method to show the AddExpense Scene.
     *
     * @param event event to which adding the expense
     * @param expense the expense to put
     */
    public void showAddExpense(Event event, Expense expense) {
        primaryStage.setTitle("Add / Edit Expense");
        primaryStage.setScene(addExpense);
        addExpense.setOnKeyPressed(e -> addExpenseCtrl.keyPressed(e));
        addExpenseCtrl.setEvent(event, expense);
    }

    /**
     * Method to show the EditEvent Scene.
     * @param event Event entity
     */
    public void showEditEvent(Event event) {
        primaryStage.setTitle("Edit Event");
        primaryStage.setScene(editEvent);
        editEvent.setOnKeyPressed(e -> editEventCtrl.keyPressed(e));
        editEventCtrl.setEvent(event);
    }

    /**
     * Method to show StartScreen Scene
     */
    public void showStartScreen() {
        primaryStage.setTitle("Start Screen");
        primaryStage.setScene(startScreen);
        startScreen.setOnKeyPressed(e -> startScreenCtrl.keyPressed(e));
    }

    /**
     * Method to show the AdminOverview Scene
     */
    public void showAdminOverview() {
        adminOverviewCtrl.refreshEvents();
        primaryStage.setTitle("Admin overview");
        primaryStage.setScene(adminOverview);
        adminOverview.setOnKeyPressed(e -> adminOverviewCtrl.keyPressed(e));
        adminOverview.setOnKeyReleased(e -> adminOverviewCtrl.keyReleased(e));
    }

    /**
     * Method to show the AddLanguage Scene
     */
    public void showAddLanguage() {
        primaryStage.setTitle("Add language");
        primaryStage.setScene(addLanguage);
        addLanguage.setOnKeyPressed(e -> addLanguageCtrl.keyPressed(e));
    }

    /**
     * Method to show the AdminLogin Scene
     */
    public void showAdminLogin() {
        primaryStage.setTitle("Admin login");
        primaryStage.setScene(adminLogin);
        adminLogin.setOnKeyPressed(e -> adminLoginCtrl.keyPressed(e));
    }

    /**
     * shows title edit screen
     * @param id id of event
     * @param event event itself
     */
    public void showEditTitle(long id, Event event) {
        primaryStage.setTitle("Change event title");
        editTitleCtrl.setId(id);
        editTitleCtrl.setEvent(event);
        primaryStage.setScene(editTitle);
        editTitle.setOnKeyPressed(e -> editTitleCtrl.keyPressed(e));
    }

    /**
     * show about page
     */
    public void showAbout() {
        primaryStage.setTitle("About page");
        primaryStage.setScene(about);
        about.setOnKeyPressed(e -> aboutCtrl.keyPressed(e));
    }


    /**
     * Method to show the AddPerson Scene
     * @param event the event to which the person is added
     * @param person The person to edit
     */
    public void showAddPerson(Event event, Person person) {
        primaryStage.setTitle("Add participant");
        primaryStage.setScene(addPerson);
        addPerson.setOnKeyPressed(e -> addPersonCtrl.keyPressed(e));
        addPersonCtrl.setEvent(event, person);
    }

    /**
     * Method to show the SettleDebts Scene
     * @param event the event related to the debts
     */
    public void showSettleDebts(Event event) {
        primaryStage.setTitle("Open Debts");
        primaryStage.setScene(settleDebts);
        settleDebts.setOnKeyPressed(e -> settleDebtsCtrl.keyPressed(e));
        settleDebtsCtrl.setEvent(event);
    }

    /**
     * Method to show the SendInvitation Scene
     * @param event the event to invite to
     */
    public void showSendInvitation(Event event) {
        primaryStage.setTitle("Send invitation");
        primaryStage.setScene(sendInvitation);
        sendInvitation.setOnKeyPressed(e -> sendInvitationCtrl.keyPressed(e));
        sendInvitationCtrl.setEvent(event);
    }

    /**
     * Method to switch app's language to another during runtime.
     * 
     * @param newLocale new locale (specifies translation)
     */
    public void switchLanguage(Locale newLocale) {
        List<Event> events = startScreenCtrl.getEventListTitles().getItems();

        client.getConfig().saveSelectedLocale(newLocale);
        Parent[] parents = client.getLocalizedParents(newLocale);

        addExpense.setRoot(parents[0]);
        editEvent.setRoot(parents[1]);
        startScreen.setRoot(parents[2]);
        adminOverview.setRoot(parents[3]);
        addLanguage.setRoot(parents[4]);
        adminLogin.setRoot(parents[5]);
        addPerson.setRoot(parents[6]);
        settleDebts.setRoot(parents[7]);
        about.setRoot(parents[8]);
        sendInvitation.setRoot(parents[9]);
        editTitle.setRoot(parents[10]);

        startScreenCtrl.setEvents(events);
    }

    /**
     * Setter for client field. Makes some methods from Main accessible from mainCtrl
     * 
     * @param client instance of Main (client.Main)
     */
    public void setClient(Main client) {
        this.client = client;
    }

    /**
     * Getter for client field (for testing)
     * @return the main client
     */
    public Main getClient() {
        return client;
    }

    /**
     * Getter for locales available in the app
     * 
     * @return list of locales
     */
    public List<Locale> getLocales() {
        return locales;
    }

    /**
     * Getter for global config for the app
     *
     * @return the appConfig
     */
    public AppConfig getConfig() {
        return getClient().getConfig();
    }

    /**
     * Setter for locales available in the app
     * This method is called in Main, has to be done before loading the scenes
     * 
     * @param locales list of locales
     */
    public void setLocales(List<Locale> locales) {
        this.locales = locales;
    }

    /**
     * Method to refresh Edit Event Scene
     */
    public void refreshEditEvent() {
        editEventCtrl.refresh();
    }

    /**
     * Method to refresh Start Screen Scene
     */
    public void refreshStartScreen() {
        startScreenCtrl.refresh();
    }

    /**
     * Method to refresh Add / Edit Expense Scene
     */
    public void refreshAddExpense() {
        addExpenseCtrl.refresh();
    }

    /**
     * Method te refresh admin overview
     */
    public void refreshAdminOverview() {
        adminOverviewCtrl.refreshEventsFull();
    }

    /**
     * Method to save translation to config so that it is immediately
     * available in the app. Also, automatically repopulates
     * language switches in scenes to feature new translation.
     * @param localeStr the string with locales
     */
    public void saveTranslation(String localeStr) {
        client.getConfig().addAvailableLocale(localeStr);
        addExpenseCtrl.fillLanguageSwitch();
        startScreenCtrl.fillLanguageSwitch();
    }

    /**
     * Method to get the selected locale
     * 
     * @return  currently selected locale
     */
    public Locale getSelectedLocale() {
        return client.getSelectedLocate();
    }


    /**
     * Returns the primary stage
     * @return the primary stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}