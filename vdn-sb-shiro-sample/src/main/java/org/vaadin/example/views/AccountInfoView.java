/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import javax.annotation.security.RolesAllowed;
import org.apache.shiro.subject.Subject;
import org.vaadin.example.shiro.SecureView;

/**
 *
 * @author Bela Oxmyx
 */
@Route("acctInfo")
@RolesAllowed("admin")
public class AccountInfoView extends SecureView {
    H1 h1PageTitle = new H1();
    String pageTitle = "Account Info Page for: ";
    String username;
    Button btnHome = new Button("Home");
    Button btnLogout = new Button("Logout");
    
//    @Autowired
//    ShiroSvc shiroSvc;

    public AccountInfoView() {
//        this.username = username;

        btnHome.addClickListener(e -> {
            btnHome.getUI().ifPresent(ui -> {
                ui.navigate("");
            });
        });
        
        btnLogout.addClickListener(e -> {
            shiroSvc.logout();
            btnLogout.getUI().ifPresent(ui -> {
                ui.navigate("");
            });
        });
        
        add(h1PageTitle, btnHome, btnLogout);
    }
    
    @Override
    public void otherBeforeEnter(BeforeEnterEvent event) {
        updateUserName();
        
    }

    private void updateUserName() {
        username = shiroSvc.getUserName();

        h1PageTitle.setText(pageTitle + this.username);

    }

}
