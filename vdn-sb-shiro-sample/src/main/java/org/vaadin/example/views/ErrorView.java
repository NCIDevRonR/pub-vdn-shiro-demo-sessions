/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import javax.annotation.security.PermitAll;
import org.vaadin.example.shiro.SecureView;

/**
 *
 * @author Bela Oxmyx
 */
@Route("error")
@PermitAll
public class ErrorView extends SecureView {

    H1 h1Errors = new H1();

    Button btnGoHome = new Button("Go Home");
    
    
//    @Autowired
//    ShiroSvc shiroSvc;

    public ErrorView() {
        btnGoHome.addClickListener(e -> {
            btnGoHome.getUI().ifPresent(ui -> {
                ui.navigate("");
            });
        });
        
        add(h1Errors, btnGoHome);
    }
    
    @Override
    public void otherBeforeEnter(BeforeEnterEvent event) {
        //Display any errors generated from Shiro.
        h1Errors.setText(shiroSvc.getErrorMsg());
        
    }

    
}
