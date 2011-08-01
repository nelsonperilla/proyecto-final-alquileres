/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.mdb;

import com.alquilacosas.common.NotificacionEmail;
import java.util.Date;
import java.util.Properties;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author damiancardozo
 */
@MessageDriven(mappedName = "jms/notificacionEmailQueue", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty( propertyName="destinationName", propertyValue="jms/ShippingRequestQueue")
})
public class NotificadorMDB implements MessageListener {
    
    @Resource
    private MessageDrivenContext context;
    
    private static final String SMTP_HOST_NAME = "smtp.gmail.com";
    private static final String SMTP_AUTH_USER = "alquilacosas@gmail.com";
    private static final String SMTP_AUTH_PWD  = "tesisutn";
    
    public NotificadorMDB() {
    }
    
    @Override
    public void onMessage(Message message) {
        ObjectMessage msg = (ObjectMessage) message;
        try {
            NotificacionEmail notificacion = (NotificacionEmail) msg.getObject();
            String destinatario = notificacion.getDestinatario();
            String asunto = notificacion.getAsunto();
            String texto = notificacion.getTexto();
            sendEmail(destinatario, asunto, texto);
        } catch (Exception e) {
            System.out.println("excepcion al tratar de enviar email!!!" + e);
        }
    }
    
    public void sendEmail(String destinatario, String asunto, String texto) {
        Transport transport = null;
        try {
            
            Properties properties = System.getProperties();
            properties.setProperty("mail.smtp.host", SMTP_HOST_NAME);
            properties.put("mail.transport.protocol", "smtp");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.starttls.required", "true");
            
            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getDefaultInstance(properties, auth);
            transport = session.getTransport();
            
            MimeMessage mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(SMTP_AUTH_USER));
            mail.addRecipient(RecipientType.TO, new InternetAddress(destinatario));
            mail.setSubject(asunto);
            mail.setSentDate(new Date());
            //mail.setText(texto);
            
            BodyPart mdp = new MimeBodyPart ();
            mdp.setContent (texto, "text/html");
            Multipart mm = new MimeMultipart ();
            mm.addBodyPart (mdp);
            
            mail.setContent(mm);
            mail.saveChanges();
            
            transport.connect();
            transport.sendMessage(mail, mail.getRecipients(RecipientType.TO));
            transport.close();
            
        } catch (MessagingException e) {
            System.out.println("excepcion al enviar el mail!! " + e);
        }
    }
    
    
    private class SMTPAuthenticator extends javax.mail.Authenticator {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
           String username = SMTP_AUTH_USER;
           String password = SMTP_AUTH_PWD;
           return new PasswordAuthentication(username, password);
        }
    }
    
}
