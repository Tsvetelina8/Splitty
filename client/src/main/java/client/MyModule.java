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

import client.scenes.*;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

public class MyModule implements Module {

    /**
     * Configures the bindings for the application's controllers.
     * This method is called by the Guice framework to collect configuration information.
     * In our case, binds controllers as singletons (so only one instance of each is allowed).
     *
     * @param binder The Guice binder object used to define bindings.
     */
    @Override
    public void configure(Binder binder) {
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);

        binder.bind(AboutCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AddExpenseCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AddLanguageCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AddPersonCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AdminLoginCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AdminOverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(EditEventCtrl.class).in(Scopes.SINGLETON);
        binder.bind(EditTitleCtrl.class).in(Scopes.SINGLETON);
        binder.bind(SendInvitationCtrl.class).in(Scopes.SINGLETON);
        binder.bind(SettleDebtsCtrl.class).in(Scopes.SINGLETON);
        binder.bind(StartScreenCtrl.class).in(Scopes.SINGLETON);

    }
}