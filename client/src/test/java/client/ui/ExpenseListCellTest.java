package client.ui;

import client.scenes.EditEventCtrl;
import client.scenes.MainCtrl;
import client.utils.AppConfig;
import commons.Event;
import commons.Expense;
import commons.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationExtension;

import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(ApplicationExtension.class)
class ExpenseListCellTest {
    @Mock
    private MainCtrl mainCtrl;
    @Mock
    AppConfig config;
    @Mock
    ResourceBundle bundle;

    @Mock
    private EditEventCtrl editEventCtrl;

    private ExpenseListCell expenseListCell;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mainCtrl.getConfig()).thenReturn(config);
        when(bundle.getString(Mockito.anyString())).thenReturn("test");
        expenseListCell = new ExpenseListCell(mainCtrl, "jeff", editEventCtrl, bundle);
    }

    @Test
    void updateItemTest() {
        expenseListCell.updateItem(new Expense("sexpense", "eee", LocalDateTime.MAX, new Person("joe", "biden"), 20), false);
        assertNotNull(expenseListCell.getGraphic());
    }

    @Test
    void updateItemTestFalse() {
        expenseListCell.updateItem(new Expense("sexpense", "eee", LocalDateTime.MAX, new Person("joe", "biden"), 20), true);
        assertNull(expenseListCell.getText());
        assertNull(expenseListCell.getGraphic());
    }

    @Test
    void attachEventTest() {
        assertEquals(expenseListCell, expenseListCell.attachEvent(new Event()));
    }

    @Test
    void editButtonTest() {
        expenseListCell.updateItem(new Expense("sexpense", "eee", LocalDateTime.MAX, new Person("joe", "biden"), 20), false);
        expenseListCell.getEditButton().fire();
        Mockito.verify(editEventCtrl, Mockito.times(1)).editExpense(Mockito.any());
    }

    @Test
    void editButtonTestNull() {
        expenseListCell.getEditButton().fire();
        Mockito.verify(mainCtrl, Mockito.never()).showAddExpense(Mockito.any(), Mockito.any());
    }

    @Test
    void deleteButtonTest() {
        expenseListCell.updateItem(new Expense("sexpense", "eee", LocalDateTime.MAX, new Person("joe", "biden"), 20), false);
        expenseListCell.getDeleteButton().fire();
        Mockito.verify(editEventCtrl, Mockito.times(1)).deleteExpense(Mockito.any());
    }

    @Test
    void deleteButtonTestNull() {
        expenseListCell.getDeleteButton().fire();
        Mockito.verify(editEventCtrl, Mockito.never()).deleteExpense(Mockito.any());
    }
}