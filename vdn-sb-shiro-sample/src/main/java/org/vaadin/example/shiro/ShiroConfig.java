/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.vaadin.example.shiro;

import org.vaadin.example.shiro.util.FileSvc;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.vaadin.example.Application;

/**
 *
 * @author Bela Oxmyx
 */
@Configuration
public class ShiroConfig {

    private static Logger log = LoggerFactory.getLogger(Application.class);

    HashMap<String, String> userPWs;
    HashMap<String, List<String>> userRoles;

    @Bean
    public SecurityManager defaultWebSecurityManager(MyCustomRealm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // Add your realms, session managers, etc.
        securityManager.setRealm(realm);
        return securityManager;
    }

    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean(SecurityManager securityManager) {
        MethodInvokingFactoryBean bean = new MethodInvokingFactoryBean();
        bean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        bean.setArguments(new Object[]{securityManager});
        return bean;
    }

    @Bean
    public MyCustomRealm myCustomRealm() {
        MyCustomRealm realm = new MyCustomRealm();
        setupPWsAndRoles();
        realm.setUserPWs(userPWs);
        realm.setUserRoles(userRoles);
        return realm;
    }

    @Autowired
    FileSvc fileSvc;

    private void setupPWsAndRoles() {
        userPWs = new HashMap<>();
        userRoles = new HashMap<>();

        List<String> shiroIni = fileSvc.readFile("shiro.ini");
        boolean userInfo = false;
        for (String eachLine : shiroIni) {
            if (!userInfo && eachLine.contains("[users]")) {
                userInfo = true;
            } else if (userInfo && !eachLine.contains("[")) {
                //Treat as user info.
                //jill.coder = moonunit, admin
                String[] info = eachLine.split(", ");
                String[] creds = info[0].split(" = ");
                String username = creds[0];
                String password = creds[1];
                userPWs.put(username, password);
                if (info.length > 1) {
                    List<String> roles = new ArrayList<>();
                    for (String item : info) {
                        if (!item.equals(info[0])) {
                            roles.add(item);
                        }
                        userRoles.put(username, roles);
                    }
                }
            } else {
                //End of user info section.
                userInfo = false;
            }
        }

    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        // need to accept POSTs from the login form
        chainDefinition.addPathDefinition("/login", "authc");
        chainDefinition.addPathDefinition("/logout", "logout");
        return chainDefinition;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        // configure URL rules and filters
        return shiroFilter;
    }

    @ControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(AuthorizationException.class)
        public ResponseEntity<String> handleAuthorizationException(AuthorizationException ex) {
            // Log the error or collect it as needed
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
    }

}
