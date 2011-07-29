/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name="login")
@ViewScoped
public class LoginMBean implements Serializable {

    private String username, password;
    
    /** Creates a new instance of LoginMBean */
    public LoginMBean() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public void login() {
        
    }
}
