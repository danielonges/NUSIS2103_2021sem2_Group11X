/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.mdb;

import ejb.session.stateless.AdminEntitySessionBeanLocal;
import ejb.session.stateless.AppointmentEntitySessionBeanLocal;
import ejb.session.stateless.EmailSessionBeanLocal;
import entity.AppointmentEntity;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import util.exception.AppointmentNotFoundException;

/**
 *
 * @author leele
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/queueApplication"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class MessageDrivenBean implements MessageListener {

    @EJB(name = "AppointmentEntitySessionBeanLocal")
    private AppointmentEntitySessionBeanLocal appointmentEntitySessionBeanLocal;

    @EJB(name = "EmailSessionBeanLocal")
    private EmailSessionBeanLocal emailSessionBeanLocal;

    @EJB(name = "AdminEntitySessionBeanLocal")
    private AdminEntitySessionBeanLocal adminEntitySessionBeanLocal;

    @PostConstruct
    public void postConstruct() {
    }

    @PreDestroy
    public void preDestroy() {
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof MapMessage) {
                MapMessage mapMessage = (MapMessage) message;
                String toEmailAddress = mapMessage.getString("toEmailAddress");
                String fromEmailAddress = mapMessage.getString("fromEmailAddress");
                Long appointmentId = (Long) mapMessage.getLong("appointmentId");
                AppointmentEntity appointmentEntity = appointmentEntitySessionBeanLocal.retrieveAppointmentEntityByAppointmentId(appointmentId);

                emailSessionBeanLocal.emailCheckoutNotificationSync(appointmentEntity, fromEmailAddress, toEmailAddress);

                System.out.println("Message sent!");
                System.err.println("********** CheckoutNotificationMdb.onMessage: " + appointmentEntity.getAppointmentId() + "; " + toEmailAddress + "; " + fromEmailAddress);
            }
        } catch (AppointmentNotFoundException | JMSException ex) {

            System.err.println("CheckoutNotificationMdb.onMessage(): " + ex.getMessage());
        }
    }
}
