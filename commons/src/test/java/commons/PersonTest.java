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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PersonTest {

    @Test
    public void checkConstructor() {
        var p = new Person("f", "l");
        assertEquals("f", p.firstName);
        assertEquals("l", p.lastName);
    }

    @Test
    public void checkConstructorPerson() {
        Person person = new Person("f", "l");
        Person person2 = new Person(person);
        assertEquals("f", person.firstName);
        assertEquals("l", person2.lastName);
        assertEquals(0, person2.id);
    }

    @Test
    public void equalsHashCode() {
        var a = new Person("a", "b");
        var b = new Person("a", "b");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var a = new Person("a", "b");
        var b = new Person("a", "c");
        b.id = 1;
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hasToString() {
        var actual = new Person("a", "b").toString();
        assertTrue(actual.equalsIgnoreCase("a b"));
    }
}