/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.vaadin.example.shiro;

/**
 *
 * @author NCI Admin
 */
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class VaadinConfiguration {

    @Autowired
    private VaadinSessionListener vaadinSessionListener;

    @PostConstruct
    public void init() {
        VaadinService vaadinSvc = VaadinService.getCurrent();   //vaadinSvc is null when line below runs.

        vaadinSvc.addSessionInitListener(event -> {
            VaadinSession session = event.getSession();
            session.getService().addSessionDestroyListener(vaadinSessionListener);
        });

    }
}
