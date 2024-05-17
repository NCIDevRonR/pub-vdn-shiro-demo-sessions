/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.vaadin.example.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.apache.shiro.subject.Subject;
import org.vaadin.example.shiro.SecureView;

/**
 *
 * @author Bela Oxmyx
 */
@Route("")
@AnonymousAllowed
public class HelloView extends SecureView {

    H1 h1Greeting = new H1();
    String hello = "Hello ";
    String anon = "Bunky";
    String greeting;
    String username;

    Button btnLogin = new Button("Login");
    Button btnLogout = new Button("Logout");
    HorizontalLayout logBtnsHLayout = new HorizontalLayout();
    Button btnAcctInfo = new Button("Account Info");
    Label lblAdminRqd = new Label("(requires admin role)");
    HorizontalLayout acctInfoHLayout = new HorizontalLayout();

//    @Autowired
//    ShiroSvc shiroSvc;

    public HelloView() {
//        VaadinRequest request = VaadinRequest.getCurrent();
//        username = shiroSvc.getUserName();
//        if (username == null) {
//            greeting = hello + anon;
//        } else {
//            greeting = hello + username;
//        }
//        h1Greeting.setText(greeting);
        add(h1Greeting);
        btnLogin.addClickListener(e -> {
            //Open the login page.
            btnLogin.getUI().ifPresent(ui -> {
                ui.navigate("login");
            });
        });

        logBtnsHLayout.add(btnLogin, btnLogout);
        add(logBtnsHLayout);

        btnLogout.addClickListener(e -> {
            shiroSvc.logout();
            UI.getCurrent().getPage().reload();
        });

        btnAcctInfo.addClickListener(e -> {
            btnAcctInfo.getUI().ifPresent(ui -> {
                ui.navigate("acctInfo");
            });
        });

        acctInfoHLayout.add(btnAcctInfo, lblAdminRqd);
        add(acctInfoHLayout);
    }

    private void displayLoggingBtns(boolean loggedInUser) {
        //Check whether a user is logged in.  Replace boolean with Shiro check.
        btnLogin.setVisible(!loggedInUser);
        btnLogout.setVisible(loggedInUser);
    }

    public H1 getH1Greeting() {
        return h1Greeting;
    }

    public void setH1Greeting(H1 h1Greeting) {
        this.h1Greeting = h1Greeting;
    }

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }

    public String getAnon() {
        return anon;
    }

    public void setAnon(String anon) {
        this.anon = anon;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void otherBeforeEnter(BeforeEnterEvent event) {
        updateUserName();

        boolean loggedInUser = (!username.equalsIgnoreCase("World"));

        //Attach this call to Shiro login state.
        displayLoggingBtns(loggedInUser);
        
    }

    private void updateUserName() {
        username = shiroSvc.getUserName();
        if (username == null) {
            greeting = hello + anon;
        } else {
            greeting = hello + username;
        }        
        h1Greeting.setText(greeting);
    }

}
