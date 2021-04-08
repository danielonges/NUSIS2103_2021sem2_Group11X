/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AppointmentEntity;
import java.util.concurrent.Future;
import javax.ejb.Remote;

/**
 *
 * @author leele
 */
@Remote
public interface EmailSessionBeanRemote {
    
    public Boolean emailCheckoutNotificationSync(AppointmentEntity appointmentEntity, String fromEmailAddress, String toEmailAddress);
    
    public Future<Boolean> emailCheckoutNotificationAsync(AppointmentEntity appointmentEntity, String fromEmailAddress, String toEmailAddress) throws InterruptedException;
}
