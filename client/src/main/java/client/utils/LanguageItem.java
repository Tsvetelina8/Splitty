package client.utils;

import javafx.beans.property.SimpleStringProperty;

/**
 * Auxiliary class for row representation in the TableView in Add Language scene
 */
public class LanguageItem {
    private final SimpleStringProperty label = new SimpleStringProperty();
    private final SimpleStringProperty defaultValue = new SimpleStringProperty();
    private final SimpleStringProperty translation = new SimpleStringProperty();

    /**
     * Constructor for Language Item
     * 
     * @param label         the label from .properties file
     * @param defaultValue  the default value (en_US translation)
     */
    public LanguageItem(String label, String defaultValue) {
        this.label.set(label);
        this.defaultValue.set(defaultValue);
        this.translation.set("");
    }

    /**
     * Getter for label string
     * @return label string
     */
    public String getLabel() {
        return label.get();
    }

    /**
     * Getter for label
     * @return label
     */
    public SimpleStringProperty labelProperty() {
        return label;
    }

    /**
     * Setter for label
     * @param label the label
     */
    public void setLabel(String label) {
        this.label.set(label);
    }

    /**
     * Getter for default value string
     * @return default value string
     */
    public String getDefaultValue() {
        return defaultValue.get();
    }

    /**
     * Getter for default value
     * @return default value
     */
    public SimpleStringProperty defaultValueProperty() {
        return defaultValue;
    }

    /**
     * Setter for default value
     * @param defaultValue the default value
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue.set(defaultValue);
    }

    /**
     * Getter for translation string
     * @return translation string
     */
    public String getTranslation() {
        return translation.get();
    }

    /**
     * Getter for translation
     * @return translation
     */
    public SimpleStringProperty translationProperty() {
        return translation;
    }

    /**
     * Setter for translation
     * @param translation the translation
     */
    public void setTranslation(String translation) {
        this.translation.set(translation);
    }

    /**
     * Equals method for LanguageItem
     * 
     * @param obj object to compare with
     * @return if they are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LanguageItem that = (LanguageItem) obj;
        return getLabel().equals(that.getLabel()) &&
               getDefaultValue().equals(that.getDefaultValue()) &&
               getTranslation().equals(that.getTranslation());
    }
}