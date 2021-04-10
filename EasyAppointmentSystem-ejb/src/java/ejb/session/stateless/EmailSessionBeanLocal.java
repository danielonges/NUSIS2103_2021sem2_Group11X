/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AppointmentEntity;
import java.util.concurrent.Future;
import javax.ejb.Local;

/**
 *
 * @author leele
 */
@Local
public interface EmailSessionBeanLocal {
    
    public Boolean emailCheckoutNotificationSync(AppointmentEntity appointmentEntity, String fromEmailAddress, String toEmailAddress);

 
    
   
}
