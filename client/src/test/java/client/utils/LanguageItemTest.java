package client.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class LanguageItemTest {

    private LanguageItem languageItem;
    @BeforeEach
    void setUp() {
        languageItem = new LanguageItem("English", "Engrish");
    }

    @Test
    void getLabelTest() {
        assertEquals("English", languageItem.getLabel());
    }

    @Test
    void labelPropertyTest() {
        assertEquals("English", languageItem.labelProperty().get());
    }

    @Test
    void setLabelTest() {
        languageItem.setLabel("Dutch");
        assertEquals("Dutch", languageItem.labelProperty().get());
    }

    @Test
    void getDefaultValueTest() {
        assertEquals("Engrish", languageItem.getDefaultValue());
    }

    @Test
    void defaultValuePropertyTest() {
        assertEquals("Engrish", languageItem.defaultValueProperty().get());
    }

    @Test
    void setDefaultValueTest() {
        languageItem.setDefaultValue("Duwutch");
        assertEquals("Duwutch", languageItem.defaultValueProperty().get());
    }

    @Test
    void getTranslationTest() {
        assertEquals("", languageItem.getTranslation());
    }

    @Test
    void translationPropertyTest() {
        assertEquals("", languageItem.translationProperty().get());
    }

    @Test
    void setTranslationTest() {
        languageItem.setTranslation("Sussybaka");
        assertEquals("Sussybaka", languageItem.translationProperty().get());
    }

    @Test
    void testEqualsTest() {
        assertEquals(languageItem, languageItem);
        LanguageItem l = new LanguageItem("English", "Engrish");
        assertEquals(languageItem, l);
    }

    @Test
    void testEqualsNullTest() {
        assertNotEquals(languageItem, null);
    }

    @Test
    void testEqualsFalseTest() {
        assertNotEquals(languageItem, new Object());
        assertNotEquals(languageItem, new LanguageItem("English", "Engrishaa"));
        assertNotEquals(languageItem, new LanguageItem("Englisha", "Engrish"));
        LanguageItem a = new LanguageItem("English", "Engrish");
        a.setTranslation("a");
        assertNotEquals(languageItem, a);
    }
}