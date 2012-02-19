/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.common;

/**
 *
 * @author damiancardozo
 */
public class SeguridadException extends Exception {
    
    public SeguridadException() {
        super("SecurityException");
    }
    
    public SeguridadException(String msg) {
        super(msg);
    }
    
}
