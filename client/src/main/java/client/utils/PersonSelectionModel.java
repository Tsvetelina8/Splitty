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

package client.utils;

import commons.Person;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;


/**
 * Auxiliary data model for Person that allows it to be displayed in a ListView in AddExpense Scene
 * next to a respective CheckBox
 */
public class PersonSelectionModel {

    private final Person person;
    private final BooleanProperty selected = new SimpleBooleanProperty();

    /**
     * Constructor for PersonSelectionModel
     * 
     * @param person      Person object
     * @param selected    If it is selected (if the checkbox should be checked)
     */
    public PersonSelectionModel(Person person, boolean selected) {
        this.person = person;
        this.selected.set(selected);
    }

    /**
     * Getter method for underlying person
     * 
     * @return person
     */
    public Person getPerson() {
        return person;
    }

    /**
     * Getter method for if person is selected
     * 
     * @return if the person is selected
     */
    public boolean isSelected() {
        return selected.get();
    }

    /**
     * Setter method - for selecting a person (checking a checkbox)
     * 
     * @param selected to select / unselect
     */
    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    /**
     * Getter method for property object itself
     * 
     * @return property
     */
    public BooleanProperty selectedProperty() {
        return selected;
    }
}