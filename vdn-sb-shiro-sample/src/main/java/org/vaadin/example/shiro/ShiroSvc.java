/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.vaadin.example.shiro;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.VaadinSession;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import java.util.Collection;
import java.util.Map;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Service;

@Service
public class ShiroSvc {

    private static final String ERROR_PATH = "/error";

    private String errorMsg = "";

    @SuppressWarnings("Duplicates")
    public String getUserName() {

        String name = "World";

        Subject subject = SecurityUtils.getSubject();

        PrincipalCollection principalCollection = subject.getPrincipals();

        if (principalCollection != null && !principalCollection.isEmpty()) {
            Collection<Map> principalMaps = subject.getPrincipals().byType(Map.class);
            if (CollectionUtils.isEmpty(principalMaps)) {
                name = subject.getPrincipal().toString();
            } else {
                name = (String) principalMaps.iterator().next().get("username");
            }
        }

        return name;
    }

    public boolean subjectHasRole(String role) {
        boolean result = false;

        Subject subject = SecurityUtils.getSubject();

        if (subject != null) {
            result = subject.hasRole(role);
        }

        return result;
    }

    public boolean subjectIsAuthenticated() {
        boolean result = false;

        Subject subject = SecurityUtils.getSubject();

        if (subject != null) {
            result = subject.isAuthenticated();
        }

        return result;
    }

    public String getErrorPath() {
        return ERROR_PATH;
    }

    public void setErrorMsg(String message) {
        errorMsg = message;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void login(String username, String password) {
        Subject currentSubject = SecurityUtils.getSubject();
        if (!currentSubject.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            try {
                Session oldSession = currentSubject.getSession(false);
                //Stop any existing session.
                if (oldSession != null) {
                    oldSession.stop();
                }
                currentSubject.login(token);

//                //Create a NEW session for this user.
//                currentSubject.getSession(true);
                Session shiroSession = currentSubject.getSession();
                String shiroSessionID = shiroSession.getId().toString();
                VaadinSession.getCurrent().setAttribute("shiroSessionId", shiroSessionID);
                System.out.println("Shiro Session ID:  " + shiroSessionID + ".");
            } catch (AuthenticationException a) {
                //Notify the subject that the login has failed.
                Notification.show("Login failed!");
            }
        }
    }

    public void logout() {
        Subject currentSubject = SecurityUtils.getSubject();
        if (currentSubject != null) {
            currentSubject.logout();
        }
        
    }

}
