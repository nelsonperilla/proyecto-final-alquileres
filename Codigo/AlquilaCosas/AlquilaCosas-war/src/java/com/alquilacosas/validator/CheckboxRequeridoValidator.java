/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.validator;

import java.text.MessageFormat;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author damiancardozo
 */
public class CheckboxRequeridoValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) 
            throws ValidatorException {
        
        if(value.equals(Boolean.FALSE)) {
            
            String requiredMessage = ((UIInput) component).getRequiredMessage();

            if (requiredMessage == null) {
                Object label = component.getAttributes().get("label");
                if (label == null || (label instanceof String && ((String) label).length() == 0)) {
                    label = component.getValueExpression("label");
                }
                if (label == null) {
                    label = component.getClientId(context);
                }
                requiredMessage = MessageFormat.format(UIInput.REQUIRED_MESSAGE_ID, label);
            }
            
            throw new ValidatorException(
                new FacesMessage(FacesMessage.SEVERITY_ERROR, requiredMessage, requiredMessage));
        }
    }
    
}
