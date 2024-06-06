package client.ui;

import client.scenes.EditEventCtrl;
import client.utils.AppConfig;
import commons.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationExtension;

import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(ApplicationExtension.class)
class PersonListCellTest {
    @Mock
    ResourceBundle bundle;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(bundle.getString(Mockito.anyString())).thenReturn("test");
    }
    @Test
    void updateItemTest() {
        EditEventCtrl editEventCtrl = Mockito.mock(EditEventCtrl.class);
        PersonListCell personListCell = new PersonListCell(editEventCtrl, bundle);
        personListCell.updateItem(new Person("hoi", "hey"), false);
        assertEquals(personListCell.getText(), "hoi hey");
    }

    @Test
    void updateItemTestFalse() {
        EditEventCtrl editEventCtrl = Mockito.mock(EditEventCtrl.class);
        PersonListCell personListCell = new PersonListCell(editEventCtrl, bundle);
        personListCell.updateItem(new Person("hoi", "hey"), true);
        assertNull(personListCell.getText());
        assertNull(personListCell.getGraphic());
    }

    @Test
    void updateItemTestFalseNull() {
        EditEventCtrl editEventCtrl = Mockito.mock(EditEventCtrl.class);
        PersonListCell personListCell = new PersonListCell(editEventCtrl, bundle);
        personListCell.updateItem(null, false);
        assertNull(personListCell.getText());
        assertNull(personListCell.getGraphic());
    }
}