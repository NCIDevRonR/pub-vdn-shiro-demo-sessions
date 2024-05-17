/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.vaadin.example.shiro;

/**
 *
 * @author Bela Oxmyx
 */
import java.util.HashMap;
import java.util.List;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.authz.SimpleAuthorizationInfo;

public class MyCustomRealm extends AuthorizingRealm {

    HashMap<String, String> userPWs;
    HashMap<String, List<String>> userRoles;

    public void setUserPWs(HashMap<String, String> userPWs) {
        this.userPWs = userPWs;
    }
    
    public void setUserRoles(HashMap<String, List<String>> userRoles) {
        this.userRoles = userRoles;
    }
    

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // Implement logic to retrieve user's roles and permissions
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        String username = (String) principals.getPrimaryPrincipal();
        // Assign roles based on username
        if (userRoles.containsKey(username)) {
            List<String> roles = userRoles.get(username);
            for (String eachRole : roles) {
                authorizationInfo.addRole(eachRole);
            }
        }

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken credentials = (UsernamePasswordToken) token;
        String username = credentials.getUsername();
        char[] chPassword = credentials.getPassword();
        String password = new String(chPassword);
        // Implement logic to verify the username and password
        if ((userPWs.containsKey(username)) && (userPWs.get(username).matches(password))) {
            return new SimpleAuthenticationInfo(username, password, getName());
        } else {
            throw new AuthenticationException("User not found");
        }
    }
}
