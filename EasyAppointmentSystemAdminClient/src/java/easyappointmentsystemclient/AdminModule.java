/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easyappointmentsystemclient;

import ejb.session.stateless.AdminEntitySessionBeanRemote;
import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;

/**
 *
 * @author leele
 */
public class AdminModule {
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private AdminEntitySessionBeanRemote adminEntitySessionBeanRemote;
    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;
    
    AdminModule(AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote, AdminEntitySessionBeanRemote adminEntitySessionBeanRemote, CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote) {
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.adminEntitySessionBeanRemote = adminEntitySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
    }

    public AdminModule() {
    }
    
    

    void addBusinessCategory() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void removeBusinessCategory() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void sendReminderEmail() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
