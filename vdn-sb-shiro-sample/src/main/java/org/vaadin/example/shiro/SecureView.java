/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.vaadin.example.shiro;

import org.vaadin.example.views.HelloView;
import org.vaadin.example.views.ErrorView;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Bela Oxmyx
 */
public abstract class SecureView extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    public ShiroSvc shiroSvc;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Check access to the view
        if (!isAccessAllowed(event.getNavigationTarget())) {
            if (!shiroSvc.subjectIsAuthenticated()) {
                event.rerouteTo(HelloView.class);
            } else {
                event.rerouteTo(ErrorView.class);
            }
        }

        otherBeforeEnter(event);

    }

    protected boolean isAccessAllowed(Class<?> viewClass) {
        // Implement your Shiro access checks here
        // Example: check if the subject has roles/permissions
        if (viewClass.isAnnotationPresent(RolesAllowed.class) && shiroSvc.subjectIsAuthenticated()) {
            try {
                RolesAllowed rolesAnnotation = viewClass.getAnnotation(RolesAllowed.class);
                for (String eachAllowedRole : rolesAnnotation.value()) {
                    if (shiroSvc.subjectHasRole(eachAllowedRole)) {
                        shiroSvc.setErrorMsg("");
                        return true;
                    }
                }
                //Subject doesn't have any of the allowed roles.
                //At a minimum, throw the appropriate error.  The error view will notify user that access is denied.
                throw new AuthorizationException("Access Denied");
            } catch (AuthorizationException e) {
                shiroSvc.setErrorMsg(e.getMessage());
                return false;
            }
        } // Handle other access annotations similarly
        else if (viewClass.isAnnotationPresent(PermitAll.class)) {
            return shiroSvc.subjectIsAuthenticated();
        } else if (viewClass.isAnnotationPresent(DenyAll.class)) {
            return false;
        } else if (viewClass.isAnnotationPresent(AnonymousAllowed.class)) {
            return true;
        }
        return false;
    }

    protected void otherBeforeEnter(BeforeEnterEvent event) {
        //Main BeforeEnter calls this method to enable subclasses to define their own BeforeEnter processes.
        //By default this method does nothing.
    }

}
