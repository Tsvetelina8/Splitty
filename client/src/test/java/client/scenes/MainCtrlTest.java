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

import client.Main;
import client.utils.AppConfig;
import commons.Event;
import commons.Expense;
import commons.Person;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MainCtrlTest {

    private MainCtrl sut;

    @Mock
    private Stage primaryStage;

    @Mock
    private AddExpenseCtrl addExpenseCtrl;
    @Mock
    private Parent addExpense;

    @Mock
    private StartScreenCtrl startScreenCtrl;
    @Mock
    private Parent startScreen;

    @Mock
    private EditEventCtrl editEventCtrl;
    @Mock
    private Parent editEvent;

    @Mock
    private AdminOverviewCtrl adminOverviewCtrl;
    @Mock
    private Parent adminOverview;

    @Mock
    private AddLanguageCtrl addLanguageCtrl;
    @Mock
    private Parent addLanguage;

    @Mock
    private AdminLoginCtrl adminLoginCtrl;
    @Mock
    private Parent adminLogin;

    @Mock
    private SettleDebtsCtrl settleDebtsCtrl;
    @Mock
    private Parent settleDebts;

    @Mock
    private AddPersonCtrl addPersonCtrl;
    @Mock
    private Parent addPerson;

    @Mock
    private AboutCtrl aboutCtrl;
    @Mock
    private Parent about;

    @Mock
    private SendInvitationCtrl sendInvitationCtrl;
    @Mock
    private Parent sendInvitation;

    @Mock
    private EditTitleCtrl editTitleCtrl;
    @Mock
    private Parent editTitle;

    @Mock
    private Main client;
    private List<Locale> locales = new ArrayList<>();

    @BeforeEach
    public void setup() throws IOException {
        MockitoAnnotations.openMocks(this);
        sut = new MainCtrl();
        sut.setClient(client);
        when(client.getConfig()).thenReturn(new AppConfig());
        when(primaryStage.getIcons()).thenReturn(FXCollections.observableArrayList(new ArrayList<>()));
        List<Object> paramsKey  = new ArrayList<>();
        paramsKey.add(addExpenseCtrl);
        paramsKey.add(editEventCtrl);
        paramsKey.add(startScreenCtrl);
        paramsKey.add(adminOverviewCtrl);
        paramsKey.add(addLanguageCtrl);
        paramsKey.add(adminLoginCtrl);
        paramsKey.add(addPersonCtrl);
        paramsKey.add(settleDebtsCtrl);
        paramsKey.add(aboutCtrl);
        paramsKey.add(sendInvitationCtrl);
        paramsKey.add(editTitleCtrl);

        when(addExpense.getStyleClass()).thenReturn(FXCollections.observableArrayList());
        when(editEvent.getStyleClass()).thenReturn(FXCollections.observableArrayList());
        when(startScreen.getStyleClass()).thenReturn(FXCollections.observableArrayList());
        when(adminOverview.getStyleClass()).thenReturn(FXCollections.observableArrayList());
        when(addLanguage.getStyleClass()).thenReturn(FXCollections.observableArrayList());
        when(adminLogin.getStyleClass()).thenReturn(FXCollections.observableArrayList());
        when(addPerson.getStyleClass()).thenReturn(FXCollections.observableArrayList());
        when(settleDebts.getStyleClass()).thenReturn(FXCollections.observableArrayList());
        when(about.getStyleClass()).thenReturn(FXCollections.observableArrayList());
        when(sendInvitation.getStyleClass()).thenReturn(FXCollections.observableArrayList());
        when(editTitle.getStyleClass()).thenReturn(FXCollections.observableArrayList());
        when(startScreenCtrl.getEventListTitles()).thenReturn(new ListView<>());

        List<Parent> paramsValue  = new ArrayList<>();
        paramsValue.add(addExpense);
        paramsValue.add(editEvent);
        paramsValue.add(startScreen);
        paramsValue.add(adminOverview);
        paramsValue.add(addLanguage);
        paramsValue.add(adminLogin);
        paramsValue.add(addPerson);
        paramsValue.add(settleDebts);
        paramsValue.add(about);
        paramsValue.add(sendInvitation);
        paramsValue.add(editTitle);

        sut.initialize(paramsKey, paramsValue, primaryStage);
    }

    @Test
    void initializeTest() {
        verify(primaryStage, times(1)).show();
    }

    @Test
    void testShowAddExpense() {
        sut.showAddExpense(new Event(), new Expense());
        verify(addExpenseCtrl, times(1)).setEvent(any(), any());
    }

    @Test
    void testShowEditEvent() {
        sut.showEditEvent(new Event());
        verify(editEventCtrl, times(1)).setEvent(any());
    }

    @Test
    void showStartScreen() {
        sut.showStartScreen();
        verify(primaryStage, times(2)).setScene(any());
    }

    @Test
    void showAdminOverview() {
        sut.showAdminOverview();
        verify(primaryStage, times(2)).setScene(any());
    }

    @Test
    void showAddLanguage() {
        sut.showAddLanguage();
        verify(primaryStage, times(2)).setScene(any());
    }

    @Test
    void showAdminLogin() {
        sut.showAdminLogin();
        verify(primaryStage, times(2)).setScene(any());
    }

    @Test
    void showEditTitle() {
        sut.showEditTitle(1, new Event());
        verify(primaryStage, times(2)).setScene(any());
    }

    @Test
    void showAbout() {
        sut.showAbout();
        verify(primaryStage, times(2)).setScene(any());
    }

    @Test
    void showAddPerson() {
        sut.showAddPerson(new Event(), new Person());
        verify(primaryStage, times(2)).setScene(any());
    }

    @Test
    void showSettleDebts() {
        sut.showSettleDebts(new Event());
        verify(primaryStage, times(2)).setScene(any());
    }

    @Test
    void showSendInvitation() {
        sut.showSendInvitation(new Event());
        verify(primaryStage, times(2)).setScene(any());
    }

    @Test
    void switchLanguage() {
        Parent[] p =  new Parent[]{
            addExpense, editEvent, startScreen, adminOverview, addLanguage,
                    adminLogin, addPerson, settleDebts, about, sendInvitation, editTitle};
        when(client.getLocalizedParents(any())).thenReturn(p);
        sut.switchLanguage(Locale.of("en_US"));
        verify(client, times(1)).getConfig();
    }

    @Test
    void setClient() {
        sut.setClient(client);
        assertEquals(sut.getClient(), client);
    }

    @Test
    void getLocales() {
        assertEquals(sut.getLocales(), locales);
    }

    @Test
    void setLocales() {
        sut.setLocales(locales);
        assertEquals(sut.getLocales(), locales);
    }

    @Test
    void refreshEditEvent() {
        sut.refreshEditEvent();
        verify(editEventCtrl, times(1)).refresh();
    }

    @Test
    void refreshStartScreen() {
        sut.refreshStartScreen();
        verify(startScreenCtrl, times(1)).refresh();
    }

    @Test
    void refreshAddExpense() {
        sut.refreshAddExpense();
        verify(addExpenseCtrl, times(1)).refresh();
    }

    @Test
    void refreshAdminOverview() {
        sut.refreshAdminOverview();
        verify(adminOverviewCtrl, times(1)).refreshEventsFull();
    }

    @Test
    void saveTranslation() {
        sut.saveTranslation("a");
        verify(addExpenseCtrl, times(1)).fillLanguageSwitch();
    }
}