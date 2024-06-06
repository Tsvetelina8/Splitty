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
package commons;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@JsonPropertyOrder({ "ID", "First Name", "Last Name"})
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("ID")
    public long id = 0;

    @JsonProperty("First Name")
    public String firstName;
    @JsonProperty("Last Name")
    public String lastName;

    /**
     * Default constructor, for object mapper
     */
    @SuppressWarnings("unused")
    public Person() {}

    /**
     * Constructs a Person object with the given first and last names
     *
     * @param firstName   first name of the person
     * @param lastName    last name of the person
     */
    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Copies a person and sets id to 0
     * @param person Person to copy
     */
    public Person(Person person) {
        this.id = 0;
        this.firstName = person.firstName;
        this.lastName = person.lastName;
    }

    /**
     * Method to test whether two objects are equal
     *
     * @param obj   Object to compare 'this' to
     * @return true/false if equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Person)) return false;
        return id == ((Person) obj).id;
    }

    /**
     * Method to get the hashcode of the object
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return (int)id;
    }

    /**
     * Method to get the string representation of the object
     *
     * @return string representation of the object
     */
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}