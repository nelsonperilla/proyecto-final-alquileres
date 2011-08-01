/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.common;

/**
 *
 * @author damiancardozo
 */
public class AlquilaCosasException extends Exception {
    
    public AlquilaCosasException() {
        this("AlquilaCosasException");
    }
    
    public AlquilaCosasException(String msg) {
        super(msg);
    }
    
}
