/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.vaadin.example.views;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.shiro.SecureView;
import org.vaadin.example.shiro.ShiroSvc;

/**
 *
 * @author Bela Oxmyx
 */
@Route("login")
@AnonymousAllowed
public class LoginView extends SecureView {

    String[][] aryInfo = {{"USERNAME", "PASSWORD", "ROLES"}, {"joe.coder", "frankzappa", "user"}, {"jill.coder", "moonunit", "admin"}};
    TextField txtUserName = new TextField("Username");
    TextArea txaInfo = new TextArea();
    PasswordField pwdPassword = new PasswordField("Password");
    Button btnLogin = new Button("Login");

//    @Autowired
//    @Qualifier("securityManager")
//    private SecurityManager securityManager;
    public LoginView(@Autowired ShiroSvc shiroSvc) {
        StringBuilder sbInfo = new StringBuilder();
        for (String[] eachRow : aryInfo) {
            for (String eachColumn : eachRow) {
                sbInfo.append(eachColumn);
                sbInfo.append('\t');
            }
            sbInfo.append('\n');
        }
        txaInfo.setWidth(400, Unit.PIXELS);
        txaInfo.setValue(sbInfo.toString());
        btnLogin.addClickListener(e -> {
            shiroSvc.login(txtUserName.getValue(), pwdPassword.getValue());

            //Whether or not login succeeds, navigate back to Hello view.
            btnLogin.getUI().ifPresent(ui -> {
                ui.navigate("");
            });
        });

        add(txaInfo, txtUserName, pwdPassword, btnLogin);

    }

}
