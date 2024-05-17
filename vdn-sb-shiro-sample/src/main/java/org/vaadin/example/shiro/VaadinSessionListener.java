/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.vaadin.example.shiro;

/**
 *
 * @author NCI Admin
 */
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.SessionDestroyEvent;
import com.vaadin.flow.server.SessionDestroyListener;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VaadinSessionListener implements SessionDestroyListener {

    @Autowired
    private SessionManager sessionManager;

    @Override
    public void sessionDestroy(SessionDestroyEvent event) {
        VaadinSession vaadinSession = event.getSession();
        Session shiroSession = getShiroSession(vaadinSession);
        if (shiroSession != null) {
            shiroSession.stop();
        }
    }

    private Session getShiroSession(VaadinSession vaadinSession) {
        // Retrieve Shiro session ID from Vaadin session attribute
        String shiroSessionId = (String) vaadinSession.getAttribute("shiroSessionId");
        if (shiroSessionId != null) {
            return sessionManager.getSession(new DefaultSessionKey(shiroSessionId));
        }
        return null;
    }
}

