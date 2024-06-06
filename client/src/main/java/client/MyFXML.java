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

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ResourceBundle;

import com.google.inject.Injector;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Builder;
import javafx.util.BuilderFactory;
import javafx.util.Callback;
import javafx.util.Pair;

public class MyFXML {

    private Injector injector;

    /**
     * Constructs a new MyFXML instance with a given injector
     * 
     * @param injector
     */
    public MyFXML(Injector injector) {
        this.injector = injector;
    }

    /**
     * Loads an FXML file and returns a pair containing the controller and the root parent.
     * This method is used to load the FXML file corresponding to the specified 
     * controller class and parts.
     * 
     * @param <T>    The type of the controller class
     * @param c      The Class object of the controller type
     * @param bundle The resource bundle with translations
     * @param parts  The parts of the path to the FXML file.
     * @return       A Pair object containing the controller (of type T) and the parent 
     *               node of the loaded FXML.
     * @throws RuntimeException If there is an IOException during the loading of FXML file
     */
    public <T> Pair<T, Parent> load(Class<T> c, ResourceBundle bundle, String... parts) {
        try {
            var loader = new FXMLLoader(getLocation(parts), bundle, 
                null, new MyFactory(), StandardCharsets.UTF_8);
            Parent parent = loader.load();
            T ctrl = loader.getController();
            return new Pair<>(ctrl, parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private URL getLocation(String... parts) {
        var path = Path.of("", parts).toString();
        return MyFXML.class.getClassLoader().getResource(path);
    }

    private class MyFactory implements BuilderFactory, Callback<Class<?>, Object> {

        @Override
        @SuppressWarnings("rawtypes")
        public Builder<?> getBuilder(Class<?> type) {
            return new Builder() {
                @Override
                public Object build() {
                    return injector.getInstance(type);
                }
            };
        }

        @Override
        public Object call(Class<?> type) {
            return injector.getInstance(type);
        }
    }
}