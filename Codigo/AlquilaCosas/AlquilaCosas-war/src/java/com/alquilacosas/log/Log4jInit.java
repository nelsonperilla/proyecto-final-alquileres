/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.alquilacosas.log;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author damiancardozo
 */
@WebServlet(name="Log4JInitServlet", urlPatterns={"/Log4JInitServlet"})
public class Log4jInit extends HttpServlet {

    @Override
    public void init() throws ServletException {
        String prefix =  getServletContext().getRealPath("/");
        String file = getInitParameter("log4j-init-file");
        // if the log4j-init-file is not set, then no point in trying
        if(file != null) {
            PropertyConfigurator.configure(prefix + file);
        }
        else {
            BasicConfigurator.configure();
        }
        Logger.getLogger(Log4jInit.class).setLevel(Level.INFO);
        Logger.getLogger(Log4jInit.class).info("\n ------------ Comenzo ejecucion AlquilaCosas ------------");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    } 

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet for initializing log4j.";
    }// </editor-fold>

}
